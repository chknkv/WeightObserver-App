package com.chknkv.feature.main.model.presentation.uiAction

import com.chknkv.feature.main.model.presentation.MainAction
import kotlinx.datetime.LocalDate

/**
 * Actions specific to the Detailed Statistic area.
 */
sealed interface DetailedStatisticAction : MainAction {

    /** Action to hide the detailed statistic bottom sheet. */
    data object HideDetailedStatistic : DetailedStatisticAction

    /** Action to load more weight records (pagination). */
    data object LoadMoreWeights : DetailedStatisticAction

    /** Action to delete a weight record. */
    data class DeleteWeight(val date: LocalDate) : DetailedStatisticAction

    /** Action to show the delete confirmation alert. */
    data class ShowDeleteAlert(val date: LocalDate) : DetailedStatisticAction

    /** Action to hide the delete confirmation alert. */
    data object HideDeleteAlert : DetailedStatisticAction
}
