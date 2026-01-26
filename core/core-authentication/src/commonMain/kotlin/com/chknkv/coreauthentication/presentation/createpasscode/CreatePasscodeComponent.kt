package com.chknkv.coreauthentication.presentation.createpasscode

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.chknkv.coreauthentication.domain.BiometricAuthenticator
import com.chknkv.coreauthentication.domain.PasscodeRepository
import com.chknkv.coreauthentication.models.domain.handles.createpasscode.CreatePasscodeHandles
import com.chknkv.coreauthentication.models.presentation.uiAction.createpasscode.CreatePasscodeUiAction
import com.chknkv.coreauthentication.models.presentation.uiAction.createpasscode.CreatePasscodeUiEffect
import com.chknkv.coreauthentication.models.presentation.uiResult.createpasscode.CreatePasscodeUiResult
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
 * Component for the Create Passcode screen. Manages passcode creation, confirmation, and skip flow.
 */
interface CreatePasscodeComponent {

    /** Current UI state (entered digits, confirming, alert visibility). */
    val uiResult: StateFlow<CreatePasscodeUiResult>

    /** One-time UI effects (e.g. passwords do not match snackbar). */
    val uiEffect: SharedFlow<CreatePasscodeUiEffect>

    /** Call once to start the flow and subscribe to actions. */
    fun initLoadCreatePasscodeScreen()

    /** Dispatch a user [action] (e.g. digit click, skip, back). */
    fun emitAction(action: CreatePasscodeUiAction)
}

/**
 * Implementation of [CreatePasscodeComponent].
 */
internal class CreatePasscodeComponentImpl(
    componentContext: ComponentContext,
    private val passcodeRepository: PasscodeRepository,
    private val biometricAuthenticator: BiometricAuthenticator,
    private val handles: CreatePasscodeHandles
) : CreatePasscodeComponent, ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        lifecycle.subscribe(object : Lifecycle.Callbacks {
            override fun onDestroy() {
                coroutineScope.cancel()
            }
        })
    }

    private val _uiEffect = MutableSharedFlow<CreatePasscodeUiEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val uiEffect: SharedFlow<CreatePasscodeUiEffect> = _uiEffect.asSharedFlow()

    private val _uiResult = MutableStateFlow(CreatePasscodeUiResult())
    override val uiResult: StateFlow<CreatePasscodeUiResult> = _uiResult.asStateFlow()

    private val _flow = MutableSharedFlow<CreatePasscodeUiAction>()
    private var initDone = false

    override fun initLoadCreatePasscodeScreen() {
        if (initDone) return
        initDone = true
        _flow
            .onStart { emitAction(CreatePasscodeUiAction.Init) }
            .onEach { action ->
                when (action) {
                    is CreatePasscodeUiAction.Init -> Unit

                    is CreatePasscodeUiAction.Back -> handles.onBack()

                    is CreatePasscodeUiAction.ShowAlert -> _uiResult.update { it.copy(isAlertEnabled = true) }

                    is CreatePasscodeUiAction.DismissAlert -> _uiResult.update { it.copy(isAlertEnabled = false) }

                    is CreatePasscodeUiAction.NumberClick -> handleNumberClick(action.digit, _uiResult.value)

                    is CreatePasscodeUiAction.DeleteClick -> _uiResult.update {
                        it.copy(enteredDigits = it.enteredDigits.dropLast(1))
                    }

                    is CreatePasscodeUiAction.Skip -> {
                        passcodeRepository.savePasscodeHash(null)
                        _uiResult.update { it.copy(isAlertEnabled = false) }
                        handles.onPasscodeSkipped()
                    }
                }
            }
            .launchIn(coroutineScope)
    }

    override fun emitAction(action: CreatePasscodeUiAction) {
        coroutineScope.launch { _flow.emit(action) }
    }

    private fun handleNumberClick(digit: Int, old: CreatePasscodeUiResult) {
        if (old.enteredDigits.size >= MAX_DIGITS) return
        val newDigits = old.enteredDigits + digit
        _uiResult.update { it.copy(enteredDigits = newDigits) }
        if (newDigits.size < MAX_DIGITS) return

        coroutineScope.launch {
            delay(DELAY_MS)
            if (!old.isConfirming) {
                _uiResult.update {
                    it.copy(
                        savedPasscode = newDigits.joinToString(""),
                        enteredDigits = emptyList(),
                        isConfirming = true
                    )
                }
            } else {
                if (newDigits.joinToString("") != old.savedPasscode) {
                    _uiEffect.emit(CreatePasscodeUiEffect.PasswordsDoNotMatch)
                    _uiResult.value = CreatePasscodeUiResult()
                    return@launch
                }
                val passcode = newDigits.joinToString("")
                passcodeRepository.savePasscodeHash(passcode)
                val showBiometry = biometricAuthenticator.isBiometricAvailable()
                handles.onPasscodeCreated(showBiometry)
            }
        }
    }

    companion object {
        private const val MAX_DIGITS = 5
        private const val DELAY_MS = 200L
    }
}
