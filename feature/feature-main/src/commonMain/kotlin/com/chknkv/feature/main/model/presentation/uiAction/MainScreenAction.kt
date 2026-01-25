package com.chknkv.feature.main.model.presentation.uiAction

import com.chknkv.feature.main.model.presentation.MainAction

/**
 * Actions specific to the Main Screen itself.
 */
sealed interface MainScreenAction : MainAction {

    /** Initial action to setup the screen. */
    data object Init : MainScreenAction

    /** Action to show the settings bottom sheet. */
    data object ShowSettings : MainScreenAction

    /** Action to show the add measurement bottom sheet. */
    data object ShowAddMeasurement : MainScreenAction

    /** Action to show the detailed statistic bottom sheet. */
    data object ShowDetailedStatistic : MainScreenAction

    /** Swipe chart left (toward today). Enabled only when `chartWindowIndex` > 0. */
    data object ChartSwipeLeft : MainScreenAction

    /** Swipe chart right (into past). Enabled only when `hasOlderWindow` for current index. */
    data object ChartSwipeRight : MainScreenAction
}
