package com.chknkv.feature.main.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.chknkv.feature.main.domain.MainScreenInteractor
import com.chknkv.feature.main.domain.resolvers.AddMeasurementActionResolver
import com.chknkv.feature.main.domain.resolvers.DetailedStatisticActionResolver
import com.chknkv.feature.main.domain.resolvers.MainScreenActionResolver
import com.chknkv.feature.main.domain.resolvers.PasscodeSettingsActionResolver
import com.chknkv.feature.main.domain.resolvers.ResolverResult
import com.chknkv.feature.main.domain.resolvers.SettingsActionResolver
import com.chknkv.feature.main.model.presentation.MainAction
import com.chknkv.feature.main.model.presentation.uiAction.AddMeasurementAction
import com.chknkv.feature.main.model.presentation.uiAction.DetailedStatisticAction
import com.chknkv.feature.main.model.presentation.uiAction.MainScreenAction
import com.chknkv.feature.main.model.presentation.uiAction.PasscodeSettingsAction
import com.chknkv.feature.main.model.presentation.uiAction.SettingsAction
import com.chknkv.feature.main.model.presentation.MainUiResult
import com.chknkv.feature.main.model.presentation.uiResult.PasscodeSettingsUiEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
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
    val uiResult: StateFlow<MainUiResult>

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

    private val mainScreenActionResolver = MainScreenActionResolver(mainScreenInteractor)
    private val detailedStatisticActionResolver = DetailedStatisticActionResolver(mainScreenInteractor)
    private val settingsActionResolver = SettingsActionResolver(mainScreenInteractor, onSignOutRequested)
    private val passcodeSettingsActionResolver = PasscodeSettingsActionResolver(mainScreenInteractor, onSignOutRequested)
    private val addMeasurementActionResolver = AddMeasurementActionResolver(mainScreenInteractor)

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override val uiResult: StateFlow<MainUiResult> get() = _uiResult.asStateFlow()
    private val _uiResult = MutableStateFlow(MainUiResult())

    override val passcodeEffect: SharedFlow<PasscodeSettingsUiEffect> get() = _passcodeEffect.asSharedFlow()
    private val _passcodeEffect = MutableSharedFlow<PasscodeSettingsUiEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val _actionFlow = MutableSharedFlow<MainAction>()
    private var isFirstLoadMainScreenFlag = true

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
                val flow = when (action) {
                    is MainScreenAction -> mainScreenActionResolver.resolve(action, _uiResult.value)
                    is SettingsAction -> settingsActionResolver.resolve(action, _uiResult.value)
                    is AddMeasurementAction -> addMeasurementActionResolver.resolve(action, _uiResult.value)
                    is DetailedStatisticAction -> detailedStatisticActionResolver.resolve(action, _uiResult.value)
                    is PasscodeSettingsAction -> passcodeSettingsActionResolver.resolve(action, _uiResult.value)
                    else -> kotlinx.coroutines.flow.emptyFlow()
                }

                flow.collect { result ->
                    when (result) {
                        is ResolverResult.Mutation -> _uiResult.update(result.transform)
                        is ResolverResult.Effect -> _passcodeEffect.emit(result.effect)
                    }
                }
            }
            .launchIn(coroutineScope)
    }

    override fun emitAction(action: MainAction) {
        coroutineScope.launch { _actionFlow.emit(action) }
    }
}
