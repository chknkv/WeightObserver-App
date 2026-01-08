package com.chknkv.feature.welcome.presentation.createPasscode

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.chknkv.coresession.SessionRepository
import com.chknkv.feature.welcome.model.presentation.createPasscode.CreatePasscodeUiEffect
import com.chknkv.feature.welcome.model.presentation.createPasscode.CreatePasscodeUiAction
import com.chknkv.feature.welcome.model.presentation.createPasscode.CreatePasscodeUiResult
import io.github.aakira.napier.Napier
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
 * Component for CreatePasscode screen
 */
interface CreatePasscodeComponent {

    /** Ui result for CreatePasscode screen */
    val uiResult: StateFlow<CreatePasscodeUiResult>

    /** Ui effect for CreatePasscode screen */
    val uiEffect: SharedFlow<CreatePasscodeUiEffect>

    /** Init & load screen flow */
    fun initLoadCreatePasscodeScreen()

    /**
     * Emit action from UI
     *
     * @param action Ui action for CreatePasscode screen
     */
    fun emitAction(action: CreatePasscodeUiAction)
}

/**
 * Implementation of [CreatePasscodeComponent]
 *
 * @param componentContext ComponentContext
 * @param sessionRepository SessionRepository
 * @param onNext Callback for navigation to the next screen (Main Root)
 * @param onBack Callback for navigation back
 */
internal class CreatePasscodeComponentImpl(
    componentContext: ComponentContext,
    private val sessionRepository: SessionRepository,
    private val onNext: () -> Unit,
    private val onBack: () -> Unit
) : CreatePasscodeComponent, ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        lifecycle.subscribe(object : Lifecycle.Callbacks {
            override fun onDestroy() {
                coroutineScope.cancel()
            }
        })
    }


    override val uiEffect: SharedFlow<CreatePasscodeUiEffect> get() = _uiEffect.asSharedFlow()
    private val _uiEffect = MutableSharedFlow<CreatePasscodeUiEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val uiResult: StateFlow<CreatePasscodeUiResult> get() = _uiResult.asStateFlow()
    private val _uiResult = MutableStateFlow(CreatePasscodeUiResult())

    /** Shared flow used for processing and reacting to user actions (e.g., typing, button clicks). */
    private val _createPasscodeFlow = MutableSharedFlow<CreatePasscodeUiAction>()

    /** Flag that ensures the initial loading logic for the CreatePasscode screen is executed only once. */
    private var isFirstLoadCreatePasscodeScreenFlag = true

    override fun initLoadCreatePasscodeScreen() {
        if (!isFirstLoadCreatePasscodeScreenFlag) return
        isFirstLoadCreatePasscodeScreenFlag = false

        _createPasscodeFlow
            .onStart { emitAction(CreatePasscodeUiAction.Init) }
            .onEach { action ->
                when (action) {
                    is CreatePasscodeUiAction.Init -> Unit
                    is CreatePasscodeUiAction.Back -> onBack()
                    is CreatePasscodeUiAction.ShowAlert -> _uiResult.update { oldUiResult -> oldUiResult.copy(isAlertEnabled = true) }
                    is CreatePasscodeUiAction.DismissAlert -> _uiResult.update { oldUiResult -> oldUiResult.copy(isAlertEnabled = false) }
                    is CreatePasscodeUiAction.NumberClick -> handleNumberClick(action.digit, _uiResult.value)
                    is CreatePasscodeUiAction.DeleteClick -> _uiResult.update { oldUiResult ->
                        oldUiResult.copy(enteredDigits = oldUiResult.enteredDigits.dropLast(DROP_LAST_COUNTER))
                    }
                    is CreatePasscodeUiAction.Skip -> {
                        Napier.i("Passcode creation skipped", tag = TAG)
                        sessionRepository.savePasscodeHash(null)
                        _uiResult.update { oldUiResult -> oldUiResult.copy(isAlertEnabled = false) }
                        onNext()
                    }
                }
            }
            .launchIn(coroutineScope)
    }

    override fun emitAction(action: CreatePasscodeUiAction) {
        coroutineScope.launch { _createPasscodeFlow.emit(action) }
    }

    private fun handleNumberClick(
        digit: Int,
        oldUiResult: CreatePasscodeUiResult
    ) {
        val current = oldUiResult.enteredDigits
        if (current.size >= MAX_DIGITS_COUNTER) return

        val newDigits = current + digit
        _uiResult.update { oldUiResult -> oldUiResult.copy(enteredDigits = newDigits) }

        if (newDigits.size == MAX_DIGITS_COUNTER) {
            coroutineScope.launch {
                delay(DELAY_BETWEEN_END)

                if (!oldUiResult.isConfirming) {
                    _uiResult.update { oldUiResult ->
                        oldUiResult.copy(
                            savedPasscode = newDigits.joinToString(""),
                            enteredDigits = emptyList(),
                            isConfirming = true
                        )
                    }
                } else {
                    if (newDigits.joinToString("") == oldUiResult.savedPasscode) {
                        Napier.i("Successful passcode: ${oldUiResult.savedPasscode}", tag = TAG)

                        oldUiResult.savedPasscode.let {
                            sessionRepository.savePasscodeHash(it)
                        }
                        onNext()
                    } else {
                        _uiEffect.emit(CreatePasscodeUiEffect.PasswordsDoNotMatch)
                        _uiResult.update { CreatePasscodeUiResult() }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "CreatePasscodeComponent"

        private const val MAX_DIGITS_COUNTER = 5
        private const val DROP_LAST_COUNTER = 1
        private const val DELAY_BETWEEN_END = 200L
    }
}
