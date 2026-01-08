package com.chknkv.feature.welcome.model.presentation.createPasscode

import androidx.compose.runtime.Stable

/**
 * Ui result for CreatePasscode screen
 *
 * @property enteredDigits Entered passcode numbers
 * @property savedPasscode
 * @property isConfirming Is re-enter the password
 * @property isAlertEnabled Visibility of AlertAction
 */
@Stable
data class CreatePasscodeUiResult(
    val enteredDigits: List<Int> = emptyList(),
    val savedPasscode: String? = null,
    val isConfirming: Boolean = false,
    val isAlertEnabled: Boolean = false
)
