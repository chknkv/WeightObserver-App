package com.chknkv.feature.main.model.presentation

import androidx.compose.runtime.Stable

/**
 * Represents the possible user actions on the Main Screen (MVI Intent).
 */
@Stable
sealed interface MainAction {

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

    /**
     * Actions specific to the Detailed Statistic area.
     */
    sealed interface DetailedStatisticAction : MainAction {

        /** Action to hide the detailed statistic bottom sheet. */
        data object HideDetailedStatistic : DetailedStatisticAction

        /** Action to load more weight records (pagination). */
        data object LoadMoreWeights : DetailedStatisticAction
    }

    /**
     * Actions specific to the Settings area.
     */
    sealed interface SettingsAction : MainAction {

        /** Action to sign out the current user. */
        data object SignOut : SettingsAction

        /** Action to hide the settings bottom sheet. */
        data object HideSettings : SettingsAction

        /** Action to show clear data confirmation dialog. */
        data object ShowClearDataConfirmation : SettingsAction

        /** Action to hide clear data confirmation dialog. */
        data object HideClearDataConfirmation : SettingsAction
    }

    /**
     * Actions specific to the Add Measurement area.
     */
    sealed interface AddMeasurementAction : MainAction {

        /** Action to save the currently generated weight record to the database. */
        data object SaveWeight : AddMeasurementAction

        /** Action to hide the add measurement bottom sheet. */
        data object HideAddMeasurement : AddMeasurementAction

        /** Action to update the weight input. */
        data class UpdateWeightInput(val input: String) : AddMeasurementAction
    }
}
