package com.chknkv.feature.main.model.presentation

import androidx.compose.runtime.Stable
import com.chknkv.coresession.WeightRecord
import com.chknkv.feature.main.model.domain.WeightRecordWithTrend

/**
 * Represents the UI state of the Main Screen.
 *
 * @property lastSavedWeight
 * @property savedWeights The list of weight records retrieved from the database.
 * @property isSettingVisible Indicates whether the settings bottom sheet is currently visible.
 */
@Stable
data class MainScreenUiResult(
    val lastSavedWeight: WeightRecord? = null,
    val savedWeights: List<WeightRecord> = emptyList(),
    val isSettingVisible: Boolean = false,
    val isAddMeasurementVisible: Boolean = false,
    val isDetailedStatisticVisible: Boolean = false,
    val settingsUiResult: SettingsUiResult = SettingsUiResult(),
    val measurementUiResult: MeasurementUiResult = MeasurementUiResult(),
    val detailedStatisticUiResult: DetailedStatisticUiResult = DetailedStatisticUiResult()
)

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

/**
 * Represents the UI state of the Add Measurement screen.
 *
 * @property rawInput The raw text input for the weight measurement.
 * @property isSaveEnabled Indicates whether the save button is enabled.
 */
@Stable
data class MeasurementUiResult(
    val rawInput: String = "",
    val isSaveEnabled: Boolean = false
)

/**
 * Represents the UI state of the Detailed Statistic screen.
 *
 * @property records The list of weight records with trend data.
 * @property isLoading Indicates whether data is currently being loaded.
 * @property isEndReached Indicates whether the end of the data list has been reached (no more records to load).
 */
@Stable
data class DetailedStatisticUiResult(
    val records: List<WeightRecordWithTrend> = emptyList(),
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false
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