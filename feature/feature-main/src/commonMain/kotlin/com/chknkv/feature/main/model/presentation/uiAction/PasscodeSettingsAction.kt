package com.chknkv.feature.main.model.presentation.uiAction

import com.chknkv.feature.main.model.presentation.MainAction

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
