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

        /** Action to show the information bottom sheet. */
        data object ShowInformation : SettingsAction

        /** Action to hide the information bottom sheet. */
        data object HideInformation : SettingsAction

        /** Action to show the language selection bottom sheet. */
        data object ShowLanguageSelection : SettingsAction

        /** Action to hide the language selection bottom sheet. */
        data object HideLanguageSelection : SettingsAction

        /** Action to show the passcode settings bottom sheet. */
        data object ShowPasscodeSettings : SettingsAction

        /** Action to hide the passcode settings bottom sheet. */
        data object HidePasscodeSettings : SettingsAction
    }

    /**
     * Actions specific to the Passcode Settings area.
     */
    sealed interface PasscodeSettingsAction : MainAction {

        /** Action when a digit on the passcode keyboard is clicked. */
        data class DigitClick(val digit: Int) : PasscodeSettingsAction

        /** Action when the delete button on the passcode keyboard is clicked. */
        data object DeleteClick : PasscodeSettingsAction

        /** Action to skip passcode creation. */
        data object SkipCreate : PasscodeSettingsAction

        /** Action to show the alert confirming skipping passcode creation. */
        data object ShowSkipAlert : PasscodeSettingsAction

        /** Action to hide the alert confirming skipping passcode creation. */
        data object HideSkipAlert : PasscodeSettingsAction

        /** Action to show the alert for forgotten passcode (reset option). */
        data object ShowForgotAlert : PasscodeSettingsAction

        /** Action to hide the alert for forgotten passcode. */
        data object HideForgotAlert : PasscodeSettingsAction

        /** Action to reset the passcode (from the forgot alert). */
        data object ResetPasscode : PasscodeSettingsAction
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
