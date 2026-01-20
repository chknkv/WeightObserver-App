package com.chknkv.feature.main.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.chknkv.feature.main.domain.MainScreenInteractor
import com.chknkv.feature.main.model.presentation.MainAction
import com.chknkv.feature.main.model.presentation.MainScreenUiResult
import com.chknkv.feature.main.model.presentation.MeasurementUiResult
import com.chknkv.feature.main.model.presentation.DetailedStatisticUiResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
            .onStart { emitAction(MainAction.MainScreenAction.Init) }
            .onEach { action ->
                when (action) {
                    is MainAction.MainScreenAction -> resolveMainScreenAction(action)
                    is MainAction.SettingsAction -> resolveSettingsAction(action)
                    is MainAction.AddMeasurementAction -> resolveAddMeasurementAction(action)
                    is MainAction.DetailedStatisticAction -> resolveDetailedStatisticAction(action)
                }
            }
            .launchIn(coroutineScope)
    }

    private fun resolveMainScreenAction(action: MainAction.MainScreenAction) = when(action) {
        is MainAction.MainScreenAction.Init -> loadLastWeight()
        is MainAction.MainScreenAction.ShowSettings -> _uiResult.update { it.copy(isSettingVisible = true) }
        is MainAction.MainScreenAction.ShowDetailedStatistic -> initDetailedStatistic()
        is MainAction.MainScreenAction.ShowAddMeasurement -> _uiResult.update {
            it.copy(
                isAddMeasurementVisible = true,
                measurementUiResult = MeasurementUiResult()
            )
        }
    }

    private fun resolveSettingsAction(action: MainAction.SettingsAction) = when(action) {
        is MainAction.SettingsAction.HideSettings -> _uiResult.update {
            it.copy(isSettingVisible = false)
        }

        is MainAction.SettingsAction.ShowClearDataConfirmation -> _uiResult.update {
            it.copy(settingsUiResult = it.settingsUiResult.copy(isClearDataConfirmationVisible = true))
        }

        is MainAction.SettingsAction.HideClearDataConfirmation -> _uiResult.update {
            it.copy(settingsUiResult = it.settingsUiResult.copy(isClearDataConfirmationVisible = false))
        }

        is MainAction.SettingsAction.SignOut -> onSignOut()
    }

    private fun resolveAddMeasurementAction(action: MainAction.AddMeasurementAction) = when(action) {
        is MainAction.AddMeasurementAction.HideAddMeasurement -> _uiResult.update { it.copy(isAddMeasurementVisible = false) }
        is MainAction.AddMeasurementAction.SaveWeight -> saveData()
        is MainAction.AddMeasurementAction.UpdateWeightInput -> updateWeightInput(action.input)
    }

    private fun resolveDetailedStatisticAction(action: MainAction.DetailedStatisticAction) = when(action) {
        is MainAction.DetailedStatisticAction.HideDetailedStatistic -> _uiResult.update { it.copy(isDetailedStatisticVisible = false) }
        is MainAction.DetailedStatisticAction.LoadMoreWeights -> loadMoreWeights()
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
