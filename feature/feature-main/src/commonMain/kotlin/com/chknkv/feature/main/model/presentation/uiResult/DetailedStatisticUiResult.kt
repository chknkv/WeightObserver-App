package com.chknkv.feature.main.model.presentation.uiResult

import androidx.compose.runtime.Stable
import com.chknkv.feature.main.model.domain.WeightRecordWithTrend

import kotlinx.datetime.LocalDate

/**
 * Represents the UI state of the Detailed Statistic screen.
 *
 * @property records The list of weight records with trend data.
 * @property isLoading Indicates whether data is currently being loaded.
 * @property isEndReached Indicates whether the end of the data list has been reached (no more records to load).
 * @property isDeleteAlertVisible Indicates whether the delete confirmation alert is visible.
 * @property deleteAlertDate The date of the record to be deleted (when alert is visible).
 */
@Stable
data class DetailedStatisticUiResult(
    val records: List<WeightRecordWithTrend> = emptyList(),
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false,
    val isDeleteAlertVisible: Boolean = false,
    val deleteAlertDate: LocalDate? = null
)
