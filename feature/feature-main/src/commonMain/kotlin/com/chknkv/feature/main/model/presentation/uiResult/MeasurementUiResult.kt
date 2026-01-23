package com.chknkv.feature.main.model.presentation.uiResult

import androidx.compose.runtime.Stable

/**
 * Represents the UI state of the Add Measurement screen.
 *
 * @property rawInput The raw text input for the weight measurement.
 * @property isSaveEnabled Indicates whether the save button is enabled.
 */
@Stable
data class MeasurementUiResult(
    val rawInput: String = "",
    val isSaveEnabled: Boolean = false
)
