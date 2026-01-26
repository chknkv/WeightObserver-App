package com.chknkv.coreauthentication.models.presentation.uiAction.enterpasscode

/**
 * User actions for the Enter Passcode screen.
 */
sealed interface EnterPasscodeUiAction {

    /** Initial load; emitted once when the screen is first shown. */
    data object Init : EnterPasscodeUiAction

    /** User tapped a digit key (0–9). [digit] is the pressed digit. */
    data class NumberClick(val digit: Int) : EnterPasscodeUiAction

    /** User tapped the backspace/delete key. */
    data object DeleteClick : EnterPasscodeUiAction

    /** User confirmed "forgot passcode" in the dialog; consumer should clear and navigate away. */
    data object ForgotPasscode : EnterPasscodeUiAction

    /** User tapped "forgot passcode"; show confirmation dialog. */
    data object ShowForgotDialog : EnterPasscodeUiAction

    /** User dismissed or declined "forgot passcode" dialog. */
    data object HideForgotDialog : EnterPasscodeUiAction

    /** User chose to authenticate via biometrics (e.g. fingerprint/Face ID). */
    data object TryBiometric : EnterPasscodeUiAction
}
