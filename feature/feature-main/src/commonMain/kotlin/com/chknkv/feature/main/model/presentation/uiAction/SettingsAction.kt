package com.chknkv.feature.main.model.presentation.uiAction

import com.chknkv.feature.main.model.presentation.MainAction

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
