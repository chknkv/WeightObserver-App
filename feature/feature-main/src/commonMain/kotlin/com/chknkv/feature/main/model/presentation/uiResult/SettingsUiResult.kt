package com.chknkv.feature.main.model.presentation.uiResult

import androidx.compose.runtime.Stable

/**
 * Represents the UI state of the Settings screen.
 *
 * @property isClearDataConfirmationVisible Indicates whether the clear data confirmation dialog is visible.
 * @property isInformationVisible Indicates whether the information bottom sheet is visible.
 * @property isLanguageSelectionVisible Indicates whether the language selection bottom sheet is visible.
 * @property isPasscodeSettingsVisible Indicates whether the passcode settings bottom sheet is visible.
 * @property passcodeSettingsUiResult The UI state for the passcode settings.
 */
@Stable
data class SettingsUiResult(
    val isClearDataConfirmationVisible: Boolean = false,
    val isInformationVisible: Boolean = false,
    val isLanguageSelectionVisible: Boolean = false,
    val isPasscodeSettingsVisible: Boolean = false,
    val passcodeSettingsUiResult: PasscodeSettingsUiResult = PasscodeSettingsUiResult()
)
