package com.chknkv.feature.main.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.chknkv.feature.main.domain.MainScreenInteractor
import com.chknkv.feature.main.model.presentation.MainAction
import com.chknkv.feature.main.model.presentation.MainScreenUiResult
import com.chknkv.feature.main.model.presentation.MeasurementUiResult
import com.chknkv.feature.main.model.presentation.DetailedStatisticUiResult
import com.chknkv.feature.main.model.presentation.MainAction.SettingsAction
import com.chknkv.feature.main.model.presentation.MainAction.MainScreenAction
import com.chknkv.feature.main.model.presentation.MainAction.AddMeasurementAction
import com.chknkv.feature.main.model.presentation.MainAction.DetailedStatisticAction
import com.chknkv.feature.main.model.presentation.MainAction.PasscodeSettingsAction
import com.chknkv.feature.main.model.presentation.PasscodeSettingsUiEffect
import com.chknkv.feature.main.model.presentation.PasscodeSettingsUiResult
import com.chknkv.feature.main.model.presentation.PasscodeStep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Root component for the main application feature.
 *
 * Responsible for the logic of the main screen available to an authorized user.
 * Handles user actions, such as signing out.
 */
interface RootMainComponent {

    /**
     * Exposes the current state of the UI for the main screen.
     * Updates whenever the underlying data or UI state changes.
     */
    val uiResult: StateFlow<MainScreenUiResult>

    /**
     * Effect flow for Passcode Settings.
     */
    val passcodeEffect: SharedFlow<PasscodeSettingsUiEffect>

    /**
     * Initiates the initial data loading for the main screen.
     * Should be called when the screen is first displayed.
     */
    fun initLoadMainScreen()

    /**
     * Processes a user action from the UI.
     *
     * @param action The specific action triggered by the user (e.g., save weight, sign out).
     */
    fun emitAction(action: MainAction)
}

/**
 * Implementation of [RootMainComponent].
 *
 * @property componentContext Decompose component context for lifecycle management.
 * @property mainScreenInteractor Interactor for business logic.
 * @property onSignOutRequested Callback notifying the parent component about the sign-out request.
 */
class RootMainComponentImpl(
    componentContext: ComponentContext,
    private val mainScreenInteractor: MainScreenInteractor,
    private val onSignOutRequested: () -> Unit
) : RootMainComponent, ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override val uiResult: StateFlow<MainScreenUiResult> get() = _uiResult.asStateFlow()
    private val _uiResult = MutableStateFlow(MainScreenUiResult())

    override val passcodeEffect: SharedFlow<PasscodeSettingsUiEffect> get() = _passcodeEffect.asSharedFlow()
    private val _passcodeEffect = MutableSharedFlow<PasscodeSettingsUiEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val _actionFlow = MutableSharedFlow<MainAction>()
    private var isFirstLoadMainScreenFlag = true

    private var currentWeightPageOffset = 0
    private val weightPageSize = 20

    init {
        lifecycle.subscribe(object : Lifecycle.Callbacks {
            override fun onDestroy() {
                coroutineScope.cancel()
            }
        })
    }

    override fun initLoadMainScreen() {
        if (!isFirstLoadMainScreenFlag) return
        isFirstLoadMainScreenFlag = false

        _actionFlow
            .onStart { emitAction(MainScreenAction.Init) }
            .onEach { action ->
                when (action) {
                    is MainScreenAction -> resolveMainScreenAction(action)
                    is SettingsAction -> resolveSettingsAction(action)
                    is AddMeasurementAction -> resolveAddMeasurementAction(action)
                    is DetailedStatisticAction -> resolveDetailedStatisticAction(action)
                    is PasscodeSettingsAction -> resolvePasscodeSettingsAction(action)
                }
            }
            .launchIn(coroutineScope)
    }

    private fun resolveMainScreenAction(action: MainScreenAction) = when(action) {
        is MainScreenAction.Init -> loadLastWeight()
        is MainScreenAction.ShowSettings -> _uiResult.update { it.copy(isSettingVisible = true) }
        is MainScreenAction.ShowDetailedStatistic -> initDetailedStatistic()
        is MainScreenAction.ShowAddMeasurement -> _uiResult.update {
            it.copy(isAddMeasurementVisible = true, measurementUiResult = MeasurementUiResult())
        }
    }

    private fun resolveSettingsAction(action: SettingsAction) = when(action) {
        is SettingsAction.HideSettings -> _uiResult.update {
            it.copy(isSettingVisible = false)
        }

        is SettingsAction.ShowClearDataConfirmation -> _uiResult.update {
            it.copy(settingsUiResult = it.settingsUiResult.copy(isClearDataConfirmationVisible = true))
        }

        is SettingsAction.HideClearDataConfirmation -> _uiResult.update {
            it.copy(settingsUiResult = it.settingsUiResult.copy(isClearDataConfirmationVisible = false))
        }

        is SettingsAction.SignOut -> onSignOut()

        is SettingsAction.ShowInformation -> _uiResult.update {
            it.copy(settingsUiResult = it.settingsUiResult.copy(isInformationVisible = true))
        }

        is SettingsAction.HideInformation -> _uiResult.update {
            it.copy(settingsUiResult = it.settingsUiResult.copy(isInformationVisible = false))
        }

        is SettingsAction.ShowLanguageSelection -> _uiResult.update {
            it.copy(settingsUiResult = it.settingsUiResult.copy(isLanguageSelectionVisible = true))
        }

        is SettingsAction.HideLanguageSelection -> _uiResult.update {
            it.copy(settingsUiResult = it.settingsUiResult.copy(isLanguageSelectionVisible = false))
        }

        is SettingsAction.ShowPasscodeSettings -> {
            coroutineScope.launch {
                val hasPasscode = mainScreenInteractor.hasPasscode()
                val initialStep = if (hasPasscode) PasscodeStep.Enter else PasscodeStep.Create
                _uiResult.update {
                    it.copy(
                        settingsUiResult = it.settingsUiResult.copy(
                            isPasscodeSettingsVisible = true,
                            passcodeSettingsUiResult = PasscodeSettingsUiResult(step = initialStep)
                        )
                    )
                }
            }
        }

        is SettingsAction.HidePasscodeSettings -> _uiResult.update {
            it.copy(settingsUiResult = it.settingsUiResult.copy(isPasscodeSettingsVisible = false))
        }
    }

    private fun resolveAddMeasurementAction(action: AddMeasurementAction) = when(action) {
        is AddMeasurementAction.HideAddMeasurement -> _uiResult.update { it.copy(isAddMeasurementVisible = false) }
        is AddMeasurementAction.SaveWeight -> saveData()
        is AddMeasurementAction.UpdateWeightInput -> updateWeightInput(action.input)
    }

    private fun resolveDetailedStatisticAction(action: DetailedStatisticAction) = when(action) {
        is DetailedStatisticAction.HideDetailedStatistic -> _uiResult.update { it.copy(isDetailedStatisticVisible = false) }
        is DetailedStatisticAction.LoadMoreWeights -> loadMoreWeights()
    }

    private fun resolvePasscodeSettingsAction(action: PasscodeSettingsAction) {
        when (action) {
            is PasscodeSettingsAction.DigitClick -> handlePasscodeDigitClick(action.digit)
            is PasscodeSettingsAction.DeleteClick -> handlePasscodeDeleteClick()
            is PasscodeSettingsAction.SkipCreate -> handleSkipCreate()
            is PasscodeSettingsAction.ShowSkipAlert -> _uiResult.update {
                it.copy(
                    settingsUiResult = it.settingsUiResult.copy(
                        passcodeSettingsUiResult = it.settingsUiResult.passcodeSettingsUiResult.copy(isSkipAlertVisible = true)
                    )
                )
            }
            is PasscodeSettingsAction.HideSkipAlert -> _uiResult.update {
                it.copy(
                    settingsUiResult = it.settingsUiResult.copy(
                        passcodeSettingsUiResult = it.settingsUiResult.passcodeSettingsUiResult.copy(isSkipAlertVisible = false)
                    )
                )
            }
            is PasscodeSettingsAction.ShowForgotAlert -> _uiResult.update {
                it.copy(
                    settingsUiResult = it.settingsUiResult.copy(
                        passcodeSettingsUiResult = it.settingsUiResult.passcodeSettingsUiResult.copy(isForgotAlertVisible = true)
                    )
                )
            }
            is PasscodeSettingsAction.HideForgotAlert -> _uiResult.update {
                it.copy(
                    settingsUiResult = it.settingsUiResult.copy(
                        passcodeSettingsUiResult = it.settingsUiResult.passcodeSettingsUiResult.copy(isForgotAlertVisible = false)
                    )
                )
            }
            is PasscodeSettingsAction.ResetPasscode -> handleResetPasscode()
        }
    }

    private fun handlePasscodeDigitClick(digit: Int) {
        val uiState = _uiResult.value.settingsUiResult.passcodeSettingsUiResult
        if (uiState.enteredDigits.size >= 5) return

        val newDigits = uiState.enteredDigits + digit
        _uiResult.update {
            it.copy(
                settingsUiResult = it.settingsUiResult.copy(
                    passcodeSettingsUiResult = uiState.copy(enteredDigits = newDigits, isError = false)
                )
            )
        }

        if (newDigits.size == 5) {
            coroutineScope.launch {
                delay(200)
                processPasscodeFilled(newDigits)
            }
        }
    }

    private fun handlePasscodeDeleteClick() {
        val uiState = _uiResult.value.settingsUiResult.passcodeSettingsUiResult
        if (uiState.enteredDigits.isNotEmpty()) {
            _uiResult.update {
                it.copy(
                    settingsUiResult = it.settingsUiResult.copy(
                        passcodeSettingsUiResult = uiState.copy(
                            enteredDigits = uiState.enteredDigits.dropLast(1),
                            isError = false
                        )
                    )
                )
            }
        }
    }

    private suspend fun processPasscodeFilled(digits: List<Int>) {
        val uiState = _uiResult.value.settingsUiResult.passcodeSettingsUiResult
        val passcode = digits.joinToString("")

        when (uiState.step) {
            PasscodeStep.Enter -> {
                if (mainScreenInteractor.checkPasscode(passcode)) {
                    _uiResult.update {
                        it.copy(
                            settingsUiResult = it.settingsUiResult.copy(
                                passcodeSettingsUiResult = PasscodeSettingsUiResult(step = PasscodeStep.Create)
                            )
                        )
                    }
                } else {
                    _passcodeEffect.emit(PasscodeSettingsUiEffect.InvalidPasscode)
                    _uiResult.update {
                        it.copy(
                            settingsUiResult = it.settingsUiResult.copy(
                                passcodeSettingsUiResult = uiState.copy(enteredDigits = emptyList(), isError = true)
                            )
                        )
                    }
                }
            }
            PasscodeStep.Create -> {
                _uiResult.update {
                    it.copy(
                        settingsUiResult = it.settingsUiResult.copy(
                            passcodeSettingsUiResult = uiState.copy(
                                step = PasscodeStep.Confirm,
                                savedPasscode = passcode,
                                enteredDigits = emptyList()
                            )
                        )
                    )
                }
            }
            PasscodeStep.Confirm -> {
                if (passcode == uiState.savedPasscode) {
                    mainScreenInteractor.savePasscode(passcode)
                    _passcodeEffect.emit(PasscodeSettingsUiEffect.PasscodeCreated)
                    emitAction(SettingsAction.HidePasscodeSettings)
                } else {
                    _passcodeEffect.emit(PasscodeSettingsUiEffect.PasswordsDoNotMatch)
                    _uiResult.update {
                        it.copy(
                            settingsUiResult = it.settingsUiResult.copy(
                                passcodeSettingsUiResult = PasscodeSettingsUiResult(step = PasscodeStep.Create, isError = true)
                            )
                        )
                    }
                }
            }
            PasscodeStep.Init -> Unit
        }
    }

    private fun handleSkipCreate() {
        coroutineScope.launch {
            mainScreenInteractor.savePasscode(null)
            emitAction(SettingsAction.HidePasscodeSettings)
        }
    }

    private fun handleResetPasscode() {
        coroutineScope.launch {
            mainScreenInteractor.signOut()
            onSignOutRequested()
        }
    }

    override fun emitAction(action: MainAction) {
        coroutineScope.launch { _actionFlow.emit(action) }
    }

    private fun loadLastWeight() {
        coroutineScope.launch {
            val lastWeight = mainScreenInteractor.getLastWeight()
            _uiResult.update { it.copy(lastSavedWeight = lastWeight) }
        }
    }

    private fun saveData() {
        val rawInput = _uiResult.value.measurementUiResult.rawInput
        if (rawInput.length < 2) return

        val weight = rawInput.toDoubleOrNull()?.div(10.0) ?: return

        coroutineScope.launch {
            mainScreenInteractor.saveWeight(weight)
            _uiResult.update { it.copy(isAddMeasurementVisible = false) }
            loadLastWeight()
        }
    }

    private fun updateWeightInput(input: String) {
        var filtered = input.filter { it.isDigit() }
        while (filtered.startsWith("0")) {
            filtered = filtered.drop(1)
        }
        filtered = filtered.take(4)

        val isSaveEnabled = filtered.length >= 2

        _uiResult.update {
            it.copy(
                measurementUiResult = it.measurementUiResult.copy(
                    rawInput = filtered,
                    isSaveEnabled = isSaveEnabled
                )
            )
        }
    }

    private fun onSignOut() {
        coroutineScope.launch {
            _uiResult.update {
                it.copy(
                    settingsUiResult = it.settingsUiResult.copy(isClearDataConfirmationVisible = false),
                    isSettingVisible = false
                )
            }
            mainScreenInteractor.signOut()
            onSignOutRequested()
        }
    }

    private fun initDetailedStatistic() {
        currentWeightPageOffset = 0
        _uiResult.update {
            it.copy(
                isDetailedStatisticVisible = true,
                detailedStatisticUiResult = DetailedStatisticUiResult()
            )
        }
        loadMoreWeights()
    }

    private fun loadMoreWeights() {
        val currentStats = _uiResult.value.detailedStatisticUiResult
        if (currentStats.isLoading || currentStats.isEndReached) return

        _uiResult.update {
            it.copy(detailedStatisticUiResult = currentStats.copy(isLoading = true))
        }

        coroutineScope.launch {
            try {
                val newRecords = mainScreenInteractor.getPaginatedWeights(weightPageSize, currentWeightPageOffset)
                val isEndReached = newRecords.size < weightPageSize

                currentWeightPageOffset += newRecords.size

                _uiResult.update {
                    val stats = it.detailedStatisticUiResult
                    it.copy(
                        detailedStatisticUiResult = stats.copy(
                            records = stats.records + newRecords,
                            isLoading = false,
                            isEndReached = isEndReached
                        )
                    )
                }
            } catch (_: Exception) {
                _uiResult.update {
                    val stats = it.detailedStatisticUiResult
                    it.copy(
                        detailedStatisticUiResult = stats.copy(isLoading = false)
                    )
                }
            }
        }
    }
}