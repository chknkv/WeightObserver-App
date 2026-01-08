package com.chknkv.feature.welcome.model.presentation.enterPasscode

import androidx.compose.runtime.Stable

/**
 * Ui actions for EnterPasscode screen
 */
@Stable
sealed interface EnterPasscodeUiAction {

    /** Initial action */
    data object Init : EnterPasscodeUiAction

    /**
     * Enter digit for passcode
     *
     * @param digit Entered digit
     */
    data class NumberClick(val digit: Int) : EnterPasscodeUiAction

    /** Delete digit from passcode */
    data object DeleteClick : EnterPasscodeUiAction

    /** Forgot passcode action - clears saved passcode and goes to login/welcome */
    data object ForgotPasscode : EnterPasscodeUiAction

    /** Show forgot passcode confirmation dialog */
    data object ShowForgotDialog : EnterPasscodeUiAction

    /** Hide forgot passcode confirmation dialog */
    data object HideForgotDialog : EnterPasscodeUiAction
}

