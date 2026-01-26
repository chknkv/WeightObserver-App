package com.chknkv.coreauthentication.models.presentation.uiResult.createpasscode

import androidx.compose.runtime.Stable

/**
 * UI state for the Create Passcode screen.
 *
 * @param enteredDigits Currently entered digits.
 * @param savedPasscode First passcode entry (when confirming).
 * @param isConfirming True when user is repeating the passcode.
 * @param isAlertEnabled True when skip-confirmation alert is visible.
 */
@Stable
data class CreatePasscodeUiResult(
    val enteredDigits: List<Int> = emptyList(),
    val savedPasscode: String? = null,
    val isConfirming: Boolean = false,
    val isAlertEnabled: Boolean = false
)
