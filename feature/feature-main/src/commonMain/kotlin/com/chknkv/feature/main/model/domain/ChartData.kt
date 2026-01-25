package com.chknkv.feature.main.model.domain

import androidx.compose.runtime.Stable
import com.chknkv.coresession.WeightRecord
import kotlinx.datetime.LocalDate

/**
 * Data for the 30-day weight chart on the main screen.
 *
 * @property points Weight records in [startDate]..[endDate], sorted by date ascending.
 * @property lineTrend Trend of the most recent point vs the previous (determines line color).
 * @property startDate First day on X axis (today - 31). Axis is fixed; missing days have no point.
 * @property endDate Last day on X axis (today).
 */
@Stable
data class ChartData(
    val points: List<WeightRecord> = emptyList(),
    val lineTrend: WeightTrend = WeightTrend.FLAT,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
)
