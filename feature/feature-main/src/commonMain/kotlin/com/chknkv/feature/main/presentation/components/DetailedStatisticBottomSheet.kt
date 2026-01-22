package com.chknkv.feature.main.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chknkv.coredesignsystem.buttons.AcButton
import com.chknkv.coredesignsystem.buttons.AcButtonStyle
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Body1
import com.chknkv.coredesignsystem.typography.Footnote1Secondary
import com.chknkv.coredesignsystem.typography.Headline3
import com.chknkv.coreutils.toFormattedString
import com.chknkv.feature.main.model.domain.WeightRecordWithTrend
import com.chknkv.feature.main.model.domain.WeightTrend
import com.chknkv.feature.main.model.presentation.DetailedStatisticUiResult
import com.chknkv.feature.main.model.presentation.MainAction
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import weightobserver_project.feature.feature_main.generated.resources.Res
import weightobserver_project.feature.feature_main.generated.resources.ic_trending_down
import weightobserver_project.feature.feature_main.generated.resources.ic_trending_flat
import weightobserver_project.feature.feature_main.generated.resources.ic_trending_up
import weightobserver_project.feature.feature_main.generated.resources.main_weight_unit
import weightobserver_project.feature.feature_main.generated.resources.statistic_medical_disclaimer
import weightobserver_project.feature.feature_main.generated.resources.statistic_no_data
import weightobserver_project.feature.feature_main.generated.resources.statistic_ok
import weightobserver_project.feature.feature_main.generated.resources.statistic_title

/**
 * Displays a bottom sheet with detailed statistics.
 *
 * @param onDismissRequest Callback invoked when the user dismisses the bottom sheet.
 * @param modifier Modifier to be applied to the bottom sheet layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedStatisticBottomSheet(
    uiResult: DetailedStatisticUiResult,
    onAction: (MainAction) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false,
            shouldDismissOnClickOutside = false
        ),
        scrimColor = AcTokens.BottomSheetScrim.getThemedColor(),
        containerColor = AcTokens.Background1.getThemedColor(),
        contentColor = AcTokens.Background1.getThemedColor(),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = AcTokens.BottomSheetHook.getThemedColor()
            )
        }
    ) {
        BoxWithConstraints {
            val maxHeight = maxHeight
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .heightIn(max = maxHeight * 0.75f)
            ) {
                Headline3(
                    text = stringResource(Res.string.statistic_title),
                    modifier = Modifier.fillMaxWidth()
                )

                Footnote1Secondary(
                    text = stringResource(Res.string.statistic_medical_disclaimer),
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 8.dp)
                )

                if (uiResult.records.isEmpty() && !uiResult.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Body1(
                            text = stringResource(Res.string.statistic_no_data),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                    ) {
                        items(items = uiResult.records) { record ->
                            WeightRecordWidget(record)
                        }

                        item {
                            if (uiResult.isLoading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.dp,
                                        color = AcTokens.TextPrimary.getThemedColor()
                                    )
                                }
                            } else if (!uiResult.isEndReached && uiResult.records.isNotEmpty()) {
                                LaunchedEffect(Unit) {
                                    onAction(MainAction.DetailedStatisticAction.LoadMoreWeights)
                                }
                            }
                        }
                    }
                }

                AcButton(
                    text = stringResource(Res.string.statistic_ok),
                    style = AcButtonStyle.Transparent,
                    onClick = onDismissRequest,
                )
            }
        }
    }
}

@Composable
private fun WeightRecordWidget(item: WeightRecordWithTrend) {
    val record = item.record
    val (iconRes, iconColorToken) = when (item.trend) {
        WeightTrend.UP -> Res.drawable.ic_trending_up to AcTokens.IconTrendUp
        WeightTrend.DOWN -> Res.drawable.ic_trending_down to AcTokens.IconTrendDown
        WeightTrend.FLAT -> Res.drawable.ic_trending_flat to AcTokens.IconTrendFlat
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = iconColorToken.getThemedColor()
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Body1(
                    modifier = Modifier.weight(1f),
                    text = record.date.toFormattedString(),
                    maxLines = 1
                )

                Text(
                    text = "${record.weight} ${stringResource(Res.string.main_weight_unit)}",
                    style = TextStyle(
                        color = AcTokens.TextPrimary.getThemedColor(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 36.dp),
            color = AcTokens.Divider.getThemedColor()
        )
    }
}
