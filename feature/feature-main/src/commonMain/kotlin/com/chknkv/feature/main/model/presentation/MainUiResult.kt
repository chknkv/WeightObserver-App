package com.chknkv.feature.main.model.presentation

import androidx.compose.runtime.Stable
import com.chknkv.coresession.WeightRecord
import com.chknkv.feature.main.model.presentation.uiResult.DetailedStatisticUiResult
import com.chknkv.feature.main.model.presentation.uiResult.MeasurementUiResult
import com.chknkv.feature.main.model.presentation.uiResult.SettingsUiResult

/**
 * Represents the UI state of the Main Screen.
 *
 * @property lastSavedWeight
 * @property savedWeights The list of weight records retrieved from the database.
 * @property isSettingVisible Indicates whether the settings bottom sheet is currently visible.
 */
@Stable
data class MainUiResult(
    val lastSavedWeight: WeightRecord? = null,
    val savedWeights: List<WeightRecord> = emptyList(),
    val isSettingVisible: Boolean = false,
    val isAddMeasurementVisible: Boolean = false,
    val isDetailedStatisticVisible: Boolean = false,
    val settingsUiResult: SettingsUiResult = SettingsUiResult(),
    val measurementUiResult: MeasurementUiResult = MeasurementUiResult(),
    val detailedStatisticUiResult: DetailedStatisticUiResult = DetailedStatisticUiResult()
)
