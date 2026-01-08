package com.chknkv.feature.welcome.model.presentation.createPasscode

import androidx.compose.runtime.Stable

/**
 * Ui actions for CreatePasscode screen
 */
@Stable
sealed interface CreatePasscodeUiAction {

    /** Initial action */
    data object Init : CreatePasscodeUiAction

    /** On back pressed action */
    data object Back : CreatePasscodeUiAction

    /** Show AlertAction with the ability to stop creating passcode */
    data object ShowAlert : CreatePasscodeUiAction

    /** Dismiss AlertAction with the ability to stop creating passcode*/
    data object DismissAlert : CreatePasscodeUiAction

    /**
     * Enter digit for current passcode
     *
     * @param digit Entered digit for current passcode
     */
    data class NumberClick(val digit: Int) : CreatePasscodeUiAction

    /** Delete digit from current passcode */
    data object DeleteClick : CreatePasscodeUiAction

    /** Skip passcode creation */
    data object Skip : CreatePasscodeUiAction
}

