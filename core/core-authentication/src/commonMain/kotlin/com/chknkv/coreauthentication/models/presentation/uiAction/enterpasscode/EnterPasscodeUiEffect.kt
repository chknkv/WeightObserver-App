package com.chknkv.coreauthentication.models.presentation.uiAction.enterpasscode

/**
 * One-time UI effects for the Enter Passcode screen.
 */
sealed interface EnterPasscodeUiEffect {

    /** Emitted when the entered passcode is incorrect. */
    data object InvalidPasscode : EnterPasscodeUiEffect
}
