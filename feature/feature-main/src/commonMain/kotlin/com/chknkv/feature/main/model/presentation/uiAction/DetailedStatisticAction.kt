package com.chknkv.feature.main.model.presentation.uiAction

import com.chknkv.feature.main.model.presentation.MainAction

/**
 * Actions specific to the Detailed Statistic area.
 */
sealed interface DetailedStatisticAction : MainAction {

    /** Action to hide the detailed statistic bottom sheet. */
    data object HideDetailedStatistic : DetailedStatisticAction

    /** Action to load more weight records (pagination). */
    data object LoadMoreWeights : DetailedStatisticAction
}
