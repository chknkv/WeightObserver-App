package com.chknkv.feature.main.model.presentation.uiResult

import androidx.compose.runtime.Stable
import com.chknkv.feature.main.model.domain.WeightRecordWithTrend

/**
 * Represents the UI state of the Detailed Statistic screen.
 *
 * @property records The list of weight records with trend data.
 * @property isLoading Indicates whether data is currently being loaded.
 * @property isEndReached Indicates whether the end of the data list has been reached (no more records to load).
 */
@Stable
data class DetailedStatisticUiResult(
    val records: List<WeightRecordWithTrend> = emptyList(),
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false
)
