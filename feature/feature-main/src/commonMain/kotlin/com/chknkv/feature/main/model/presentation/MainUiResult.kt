package com.chknkv.feature.main.model.presentation

import androidx.compose.runtime.Stable
import com.chknkv.coresession.WeightRecord
import com.chknkv.feature.main.model.domain.ChartData
import com.chknkv.feature.main.model.presentation.uiResult.DetailedStatisticUiResult
import com.chknkv.feature.main.model.presentation.uiResult.MeasurementUiResult
import com.chknkv.feature.main.model.presentation.uiResult.SettingsUiResult

/**
 * Represents the UI state of the Main Screen.
 *
 * @property lastSavedWeight
 * @property savedWeights The list of weight records retrieved from the database.
 * @property chartData Data for the 30-day weight chart (points + line trend).
 * @property chartWindowIndex Current 30-day window (0 = [today-30, today], 1 = [today-60, today-31], …).
 * @property chartCanSwipeRight True if `hasOlderWindow` for current index (swipe right = older data).
 * @property isSettingVisible Indicates whether the settings bottom sheet is currently visible.
 */
@Stable
data class MainUiResult(
    val lastSavedWeight: WeightRecord? = null,
    val savedWeights: List<WeightRecord> = emptyList(),
    val chartData: ChartData = ChartData(),
    val chartWindowIndex: Int = 0,
    val chartCanSwipeRight: Boolean = false,
    val isSettingVisible: Boolean = false,
    val isAddMeasurementVisible: Boolean = false,
    val isDetailedStatisticVisible: Boolean = false,
    val settingsUiResult: SettingsUiResult = SettingsUiResult(),
    val measurementUiResult: MeasurementUiResult = MeasurementUiResult(),
    val detailedStatisticUiResult: DetailedStatisticUiResult = DetailedStatisticUiResult()
)
