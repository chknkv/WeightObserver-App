package com.chknkv.feature.main.model.presentation.uiResult

import androidx.compose.runtime.Stable

/**
 * Represents the UI state of the Passcode Settings screen.
 *
 * @property step The current step in the passcode flow (e.g., Enter, Create, Confirm).
 * @property enteredDigits The list of digits currently entered by the user.
 * @property isError Indicates whether an error state should be displayed (e.g., incorrect passcode).
 * @property savedPasscode The currently saved passcode, if any.
 * @property isSkipAlertVisible Indicates whether the "Skip Passcode Creation" alert is visible.
 * @property isForgotAlertVisible Indicates whether the "Forgot Passcode" alert is visible.
 */
@Stable
data class PasscodeSettingsUiResult(
    val step: PasscodeStep = PasscodeStep.Init,
    val enteredDigits: List<Int> = emptyList(),
    val isError: Boolean = false,
    val savedPasscode: String? = null,
    val isSkipAlertVisible: Boolean = false,
    val isForgotAlertVisible: Boolean = false
)

/**
 * Represents one-time side effects for the Passcode Settings screen.
 */
sealed interface PasscodeSettingsUiEffect {

    /** Effect triggered when the entered passcode is incorrect. */
    data object InvalidPasscode : PasscodeSettingsUiEffect

    /** Effect triggered when the confirmation passcode does not match the created passcode. */
    data object PasswordsDoNotMatch : PasscodeSettingsUiEffect

    /** Effect triggered when the passcode is successfully created/saved. */
    data object PasscodeCreated : PasscodeSettingsUiEffect
}

/**
 * Represents the different steps in the passcode flow.
 */
enum class PasscodeStep {
    /** Initial state. */
    Init,

    /** User is entering an existing passcode to access settings. */
    Enter,

    /** User is creating a new passcode (entering for the first time). */
    Create,

    /** User is confirming the new passcode (entering for the second time). */
    Confirm
}
