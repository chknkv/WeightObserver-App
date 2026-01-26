package com.chknkv.coreauthentication.models.presentation.uiResult.enterpasscode

import androidx.compose.runtime.Stable

/**
 * UI state for the Enter Passcode screen.
 *
 * @param enteredDigits Currently entered digits.
 * @param isError True when last entered passcode was incorrect.
 * @param isShowForgotDialog True when "forgot passcode" confirmation is visible.
 * @param isBiometricAvailable True when biometric auth is enabled and available.
 */
@Stable
data class EnterPasscodeUiResult(
    val enteredDigits: List<Int> = emptyList(),
    val isError: Boolean = false,
    val isShowForgotDialog: Boolean = false,
    val isBiometricAvailable: Boolean = false
)
