package com.chknkv.feature.welcome.model.presentation.enterPasscode

import androidx.compose.runtime.Stable

/**
 * Ui result for EnterPasscode screen
 *
 * @property enteredDigits Entered passcode numbers
 * @property isError Indicates if the entered passcode was incorrect
 */
@Stable
data class EnterPasscodeUiResult(
    val enteredDigits: List<Int> = emptyList(),
    val isError: Boolean = false,
    val isShowForgotDialog: Boolean = false
)
