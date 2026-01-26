package com.chknkv.coreauthentication.models.domain.handles.enterpasscode

/**
 * Callbacks for the Enter Passcode flow.
 *
 * @param onAuthSuccess Invoked when user successfully authenticates (passcode or biometric); consumer navigates to Main.
 * @param onForgotPasscode Invoked when user chooses "forgot passcode"; consumer clears and navigates to welcome/login.
 */
data class EnterPasscodeHandles(
    val onAuthSuccess: () -> Unit,
    val onForgotPasscode: () -> Unit
)
