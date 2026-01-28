package com.chknkv.feature.main.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import com.chknkv.coredesignsystem.theming.AcTokens
import org.jetbrains.compose.resources.stringResource
import weightobserver_project.feature.feature_main.generated.resources.Res
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.feature.main.model.domain.ChartData
import com.chknkv.feature.main.model.domain.WeightTrend
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import weightobserver_project.feature.feature_main.generated.resources.main_weight_unit

private const val ChartHeightDp = 128
private const val YLabelsWidthDp = 28
private const val XLabelsHeightDp = 18
private const val YSteps = 5
private const val XSteps = 5
private const val PlotPadPx = 8f
private const val FillAlpha = 0.32f
private const val GridAlpha = 0.45f
private val GridDashPattern = floatArrayOf(6f, 4f)
private val SinglePointRadiusDp = 4.dp
private val SwipeThresholdDp = 50.dp
private val TooltipPaddingDp = 8.dp
private val TooltipCornerDp = 6.dp
private val TooltipGapDp = 4.dp
private const val TooltipLongPressMs = 500L
private val TooltipConnectorDashPattern = floatArrayOf(6f, 4f)
private val TooltipRaiseDp = 18.dp
private const val TouchEdgeEpsilon = 2.5f

/**
 * Line chart for the last 31 days of weight (Y = weight kg, X = date).
 * X axis fixed today-31..today; missing days have no point, line connects consecutive.
 * Line color: green (↓), red (↑), or gray (→) per [ChartData.lineTrend].
 * Includes X and Y axes with labels. Supports horizontal swipe (30-day windows).
 * Long-press (and optional drag) on the plot shows a tooltip at the top with date and weight,
 * a dashed connector to the point on the chart; tooltip is visible only while held.
 */
@Composable
fun WeightChart(
    chartData: ChartData,
    modifier: Modifier = Modifier,
    canSwipeLeft: Boolean = false,
    canSwipeRight: Boolean = false,
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()

    val lineColor = when (chartData.lineTrend) {
        WeightTrend.UP -> AcTokens.IconTrendUp.getThemedColor()
        WeightTrend.DOWN -> AcTokens.IconTrendDown.getThemedColor()
        WeightTrend.FLAT -> AcTokens.IconTrendFlat.getThemedColor()
    }
    val axisColor = AcTokens.Divider.getThemedColor()
    val gridColor = axisColor.copy(alpha = GridAlpha)
    val singlePointColor = AcTokens.IconTrendFlat.getThemedColor()

    val startDate = chartData.startDate
    val endDate = chartData.endDate
    val points = chartData.points

    // Мемоизация вычислений для оптимизации
    val (minY, maxY) = remember(points) {
        if (points.isNotEmpty()) {
            val minWeight = points.minOf { it.weight }
            val maxWeight = points.maxOf { it.weight }
            val rawMin = minWeight - 25.0
            val rawMax = maxWeight + 25.0
            (rawMin.coerceAtLeast(0.0)) to (rawMax.coerceAtMost(1000.0))
        } else {
            0.0 to 1000.0
        }
    }
    val rangeY = remember(minY, maxY) { (maxY - minY).coerceAtLeast(1.0) }
    val stepY = remember(rangeY) { rangeY / (YSteps - 1) }
    val yLabels = remember(maxY, stepY) {
        (0 until YSteps).map { maxY - it * stepY }
    }

    val startEpoch = remember(startDate) { startDate?.toEpochDays() ?: 0L }
    val endEpoch = remember(endDate) { endDate?.toEpochDays() ?: 0L }
    val rangeEpoch = remember(startEpoch, endEpoch) { (endEpoch - startEpoch).coerceAtLeast(1) }
    val xLabels = remember(startEpoch, rangeEpoch) {
        (0 until XSteps).map { i ->
            LocalDate.fromEpochDays(startEpoch + rangeEpoch * i / (XSteps - 1))
        }
    }

    val axisLabelStyle = TextStyle(
        color = AcTokens.TextSecondary.getThemedColor(),
        fontSize = 11.sp,
        lineHeight = 14.sp
    )

    var plotWidth by remember { mutableStateOf(0f) }
    var plotHeight by remember { mutableStateOf(0f) }
    var chartAreaW by remember { mutableStateOf(0f) }
    var chartAreaH by remember { mutableStateOf(0f) }
    var tooltip by remember { mutableStateOf<Tooltip?>(null) }
    val density = LocalDensity.current
    val padStartPx = with(density) { 6.dp.toPx() }

    fun touchToDate(touchX: Float): LocalDate? {
        if (startDate == null || endDate == null || chartAreaW <= 0f) return null
        val left = padStartPx + PlotPadPx
        val right = padStartPx + chartAreaW - PlotPadPx
        val chartW = (right - left).coerceAtLeast(1f)
        val x = touchX.coerceIn(left, right)
        val t = ((x - left) / chartW).coerceIn(0f, 1f)
        val epoch = when {
            t <= TouchEdgeEpsilon / chartW -> startEpoch
            t >= 1f - TouchEdgeEpsilon / chartW -> endEpoch
            else -> (startEpoch + (rangeEpoch * t).roundToLong()).coerceIn(startEpoch, endEpoch)
        }
        return LocalDate.fromEpochDays(epoch)
    }

    fun weightFor(date: LocalDate): Double? = points.find { it.date == date }?.weight

    fun interpolatedWeightFor(d: LocalDate): Double? {
        weightFor(d)?.let { return it }
        if (points.size < 2 || startDate == null || endDate == null) return null
        val prev = points.filter { it.date < d }.maxByOrNull { it.date.toEpochDays() } ?: return null
        val next = points.filter { it.date > d }.minByOrNull { it.date.toEpochDays() } ?: return null
        val pe = prev.date.toEpochDays()
        val ne = next.date.toEpochDays()
        val range = (ne - pe).toFloat().coerceAtLeast(1f)
        val t = ((d.toEpochDays() - pe).toFloat() / range).coerceIn(0f, 1f)
        return prev.weight + (next.weight - prev.weight) * t.toDouble()
    }

    fun chartPositionFor(date: LocalDate): Offset? {
        if (startDate == null || endDate == null || chartAreaW <= 0f || chartAreaH <= 0f) return null
        val left = PlotPadPx
        val right = chartAreaW - PlotPadPx
        val top = PlotPadPx
        val bottom = chartAreaH - PlotPadPx
        val chartW = (right - left).coerceAtLeast(1f)
        val chartH = (bottom - top).coerceAtLeast(1f)
        val rangeEpochF = rangeEpoch.toFloat().coerceAtLeast(1f)
        val rangeYF = rangeY.toFloat().coerceAtLeast(1f)
        val weight = interpolatedWeightFor(date) ?: return null
        val epoch = date.toEpochDays()
        val x = left + ((epoch - startEpoch).toFloat() / rangeEpochF) * chartW
        val y = top + (1f - (weight - minY).toFloat() / rangeYF) * chartH
        return Offset(x, y)
    }

    Column(modifier = modifier.fillMaxWidth().height((ChartHeightDp + XLabelsHeightDp + 4).dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(ChartHeightDp.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.width(YLabelsWidthDp.dp).height(ChartHeightDp.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                for (value in yLabels) {
                    Text(
                        text = formatYLabel(value),
                        modifier = Modifier,
                        style = axisLabelStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(ChartHeightDp.dp)
                    .padding(start = 6.dp)
                    .onSizeChanged {
                        plotWidth = it.width.toFloat()
                        plotHeight = it.height.toFloat()
                    }
                    .pointerInput(
                        canSwipeLeft,
                        canSwipeRight,
                        startDate,
                        endDate,
                        startEpoch,
                        endEpoch,
                        rangeEpoch,
                        chartAreaW,
                        chartAreaH
                    ) {
                        val thresholdPx = SwipeThresholdDp.toPx()
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            var totalDeltaX = 0f
                            var lastX = down.position.x
                            val longPressJob: Job = coroutineScope.launch {
                                delay(TooltipLongPressMs)
                                touchToDate(down.position.x)?.let { d ->
                                    chartPositionFor(d)?.let { pos ->
                                        tooltip = Tooltip(d, weightFor(d), pos)
                                    }
                                }
                            }
                            do {
                                val e = awaitPointerEvent()
                                for (c in e.changes) {
                                    if (!c.pressed) continue
                                    totalDeltaX += c.position.x - lastX
                                    lastX = c.position.x
                                    touchToDate(c.position.x)?.let { d ->
                                        chartPositionFor(d)?.let { pos ->
                                            if (tooltip != null) tooltip = Tooltip(d, weightFor(d), pos)
                                        }
                                    }
                                }
                            } while (e.changes.any { it.pressed })
                            longPressJob.cancel()
                            tooltip = null
                            when {
                                totalDeltaX > thresholdPx && canSwipeRight -> onSwipeRight()
                                totalDeltaX < -thresholdPx && canSwipeLeft -> onSwipeLeft()
                            }
                        }
                    }
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .onSizeChanged {
                            chartAreaW = it.width.toFloat()
                            chartAreaH = it.height.toFloat()
                        }
                ) {
                    val left = PlotPadPx
                    val right = size.width - PlotPadPx
                    val top = PlotPadPx
                    val bottom = size.height - PlotPadPx
                    val chartW = (right - left).coerceAtLeast(1f)
                    val chartH = (bottom - top).coerceAtLeast(1f)
                    val rangeEpochF = rangeEpoch.toFloat().coerceAtLeast(1f)
                    val rangeYF = rangeY.toFloat().coerceAtLeast(1f)

                    drawLine(
                        color = axisColor,
                        start = Offset(left, top),
                        end = Offset(left, bottom),
                        strokeWidth = 1.dp.toPx()
                    )
                    drawLine(
                        color = axisColor,
                        start = Offset(left, bottom),
                        end = Offset(right, bottom),
                        strokeWidth = 1.dp.toPx()
                    )

                    val dashEffect = PathEffect.dashPathEffect(GridDashPattern, 0f)
                    for (i in 1 until YSteps - 1) {
                        val value = yLabels[i]
                        val y = top + (1f - (value - minY).toFloat() / rangeYF) * chartH
                        drawLine(
                            color = gridColor,
                            start = Offset(left, y),
                            end = Offset(right, y),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = dashEffect
                        )
                    }
                    for (i in 1 until XSteps - 1) {
                        val x = left + (i.toFloat() / (XSteps - 1)) * chartW
                        drawLine(
                            color = gridColor,
                            start = Offset(x, top),
                            end = Offset(x, bottom),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = dashEffect
                        )
                    }

                    if (startDate != null && endDate != null && points.size >= 2) {
                        val path = Path()
                        val first = points.first()
                        val x0 = left + ((first.date.toEpochDays() - startEpoch).toFloat() / rangeEpochF) * chartW
                        val y0 = top + (1f - (first.weight - minY).toFloat() / rangeYF) * chartH
                        path.moveTo(x0, y0)

                        var lastX = x0
                        for (i in 1 until points.size) {
                            val p = points[i]
                            val x = left + ((p.date.toEpochDays() - startEpoch).toFloat() / rangeEpochF) * chartW
                            val y = top + (1f - (p.weight - minY).toFloat() / rangeYF) * chartH
                            path.lineTo(x, y)
                            lastX = x
                        }

                        val fillPath = Path().apply {
                            moveTo(x0, y0)
                            for (i in 1 until points.size) {
                                val p = points[i]
                                val x = left + ((p.date.toEpochDays() - startEpoch).toFloat() / rangeEpochF) * chartW
                                val y = top + (1f - (p.weight - minY).toFloat() / rangeYF) * chartH
                                lineTo(x, y)
                            }
                            lineTo(lastX, bottom)
                            lineTo(x0, bottom)
                            close()
                        }
                        drawPath(
                            path = fillPath,
                            color = lineColor.copy(alpha = FillAlpha)
                        )
                        drawPath(
                            path = path,
                            color = lineColor,
                            style = Stroke(
                                width = 1.5.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    } else if (startDate != null && endDate != null && points.size == 1) {
                        val p = points.first()
                        val x = left + ((p.date.toEpochDays() - startEpoch).toFloat() / rangeEpochF) * chartW
                        val y = top + (1f - (p.weight - minY).toFloat() / rangeYF) * chartH
                        drawCircle(
                            color = singlePointColor,
                            radius = SinglePointRadiusDp.toPx(),
                            center = Offset(x, y)
                        )
                    }
                }

                tooltip?.let { t ->
                    val padPx = with(density) { TooltipPaddingDp.toPx() }
                    val gapPx = with(density) { TooltipGapDp.toPx() }
                    val raisePx = with(density) { TooltipRaiseDp.toPx() }
                    val tipH = 36f
                    val tipW = 72f
                    val lineX = t.linePosition.x
                    val lineY = t.linePosition.y
                    val maxTx = (chartAreaW - tipW - padPx).coerceAtLeast(padPx)
                    val tx = (lineX - tipW / 2f).coerceIn(padPx, maxTx)
                    val ty = (padPx - raisePx).coerceAtLeast(0f)
                    val xAxisY = chartAreaH - PlotPadPx
                    val tooltipBg = AcTokens.ButtonStandard.getThemedColor()
                    val tooltipText = AcTokens.TextButtonStandard.getThemedColor()
                    val tooltipStyle = axisLabelStyle.copy(color = tooltipText)
                    val tipBottomY = ty + tipH + 2f * padPx + gapPx
                    val yStart = minOf(tipBottomY, lineY)
                    val connectorDash = PathEffect.dashPathEffect(TooltipConnectorDashPattern, 0f)
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawLine(
                            color = tooltipBg,
                            start = Offset(lineX, yStart),
                            end = Offset(lineX, xAxisY),
                            strokeWidth = 1.5.dp.toPx(),
                            pathEffect = connectorDash
                        )
                    }
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset { IntOffset(tx.roundToInt(), ty.roundToInt()) }
                            .padding(TooltipPaddingDp),
                        shape = RoundedCornerShape(TooltipCornerDp),
                        color = tooltipBg
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                            Text(
                                text = t.date.toChartAxisString(),
                                style = tooltipStyle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = t.weight?.let { "${formatYLabel(it)} ${stringResource(Res.string.main_weight_unit)}" } ?: "—",
                                style = tooltipStyle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = (YLabelsWidthDp + 6 + 8).dp,
                    end = 8.dp,
                    top = 4.dp
                )
                .height(XLabelsHeightDp.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (date in xLabels) {
                Text(
                    text = date.toChartAxisString(),
                    modifier = Modifier,
                    style = axisLabelStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private data class Tooltip(val date: LocalDate, val weight: Double?, val linePosition: Offset)

private fun formatYLabel(value: Double): String =
    if (value == value.toLong().toDouble()) "${value.toLong()}" else "${(value * 10).toLong() / 10.0}"

private fun LocalDate.toChartAxisString(): String =
    "${day}.${month.number.toString().padStart(2, '0')}"
