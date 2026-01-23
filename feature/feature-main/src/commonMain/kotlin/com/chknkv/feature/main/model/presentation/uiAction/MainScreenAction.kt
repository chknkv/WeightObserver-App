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
}
