package com.chknkv.coreauthentication.presentation.enterpasscode

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.chknkv.coreauthentication.domain.BiometricAuthenticator
import com.chknkv.coreauthentication.domain.PasscodeRepository
import com.chknkv.coreauthentication.models.domain.BiometricContext
import com.chknkv.coreauthentication.models.domain.BiometricResult
import com.chknkv.coresession.SessionRepository
import com.chknkv.coreauthentication.models.domain.handles.enterpasscode.EnterPasscodeHandles
import com.chknkv.coreauthentication.models.presentation.uiAction.enterpasscode.EnterPasscodeUiAction
import com.chknkv.coreauthentication.models.presentation.uiAction.enterpasscode.EnterPasscodeUiEffect
import com.chknkv.coreauthentication.models.presentation.uiResult.enterpasscode.EnterPasscodeUiResult
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
 * Component for the Enter Passcode screen. Manages passcode verification, biometric auth, and forgot-passcode flow.
 */
interface EnterPasscodeComponent {

    /** Current UI state (entered digits, error, forgot dialog, biometric availability). */
    val uiResult: StateFlow<EnterPasscodeUiResult>

    /** One-time UI effects (e.g. invalid passcode snackbar). */
    val uiEffect: SharedFlow<EnterPasscodeUiEffect>

    /** Call once to start the flow and subscribe to actions. */
    fun initLoadEnterPasscodeScreen()

    /** Dispatch a user [action] (e.g. digit click, forgot, biometric). */
    fun emitAction(action: EnterPasscodeUiAction)

    /**
     * Run platform biometric authentication. On success, invokes [EnterPasscodeHandles.onAuthSuccess].
     *
     * @param context Platform [BiometricContext] (e.g. from getBiometricContext).
     */
    suspend fun tryBiometricAuthentication(context: BiometricContext)
}

/**
 * Implementation of [EnterPasscodeComponent].
 */
internal class EnterPasscodeComponentImpl(
    componentContext: ComponentContext,
    private val passcodeRepository: PasscodeRepository,
    private val biometricAuthenticator: BiometricAuthenticator,
    private val sessionRepository: SessionRepository, // Only for clearAll() on forgot passcode
    private val handles: EnterPasscodeHandles
) : EnterPasscodeComponent, ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        lifecycle.subscribe(object : Lifecycle.Callbacks {
            override fun onDestroy() {
                coroutineScope.cancel()
            }
        })
    }

    private val _uiEffect = MutableSharedFlow<EnterPasscodeUiEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val uiEffect: SharedFlow<EnterPasscodeUiEffect> = _uiEffect.asSharedFlow()

    private val _uiResult = MutableStateFlow(EnterPasscodeUiResult())
    override val uiResult: StateFlow<EnterPasscodeUiResult> = _uiResult.asStateFlow()

    private val _flow = MutableSharedFlow<EnterPasscodeUiAction>()
    private var initDone = false

    override fun initLoadEnterPasscodeScreen() {
        if (initDone) return
        initDone = true
        coroutineScope.launch {
            val enabled = passcodeRepository.isBiometricEnabled
            val available = enabled && biometricAuthenticator.isBiometricAvailable()
            _uiResult.update { it.copy(isBiometricAvailable = available) }
        }
        _flow
            .onStart { emitAction(EnterPasscodeUiAction.Init) }
            .onEach { action ->
                when (action) {
                    is EnterPasscodeUiAction.Init -> Unit

                    is EnterPasscodeUiAction.NumberClick -> handleNumberClick(action.digit, _uiResult.value)

                    is EnterPasscodeUiAction.DeleteClick -> _uiResult.update {
                        it.copy(enteredDigits = it.enteredDigits.dropLast(1), isError = false)
                    }

                    is EnterPasscodeUiAction.ForgotPasscode -> {
                        sessionRepository.clearAll()
                        _uiResult.update { it.copy(isShowForgotDialog = false) }
                        handles.onForgotPasscode()
                    }

                    is EnterPasscodeUiAction.ShowForgotDialog -> _uiResult.update { it.copy(isShowForgotDialog = true) }

                    is EnterPasscodeUiAction.HideForgotDialog -> _uiResult.update { it.copy(isShowForgotDialog = false) }

                    is EnterPasscodeUiAction.TryBiometric -> { /* handled by UI */ }
                }
            }
            .launchIn(coroutineScope)
    }

    override fun emitAction(action: EnterPasscodeUiAction) {
        coroutineScope.launch { _flow.emit(action) }
    }

    override suspend fun tryBiometricAuthentication(context: BiometricContext) {
        if (!passcodeRepository.isBiometricEnabled || !biometricAuthenticator.isBiometricAvailable()) return
        val result = biometricAuthenticator.authenticate(
            context = context,
            reason = "Use biometrics to log in to the app"
        )
        when (result) {
            is BiometricResult.Success -> {
                handles.onAuthSuccess()
            }
            is BiometricResult.NotEnrolled -> passcodeRepository.isBiometricEnabled = false
            else -> { }
        }
    }

    private fun handleNumberClick(digit: Int, old: EnterPasscodeUiResult) {
        if (old.enteredDigits.size >= MAX_DIGITS) return
        val newDigits = old.enteredDigits + digit
        _uiResult.update { it.copy(enteredDigits = newDigits, isError = false) }
        if (newDigits.size < MAX_DIGITS) return
        coroutineScope.launch {
            delay(DELAY_MS)
            val entered = newDigits.joinToString("")
            val saved = passcodeRepository.getPasscodeHash()
            if (entered == saved) {
                handles.onAuthSuccess()
            } else {
                _uiEffect.emit(EnterPasscodeUiEffect.InvalidPasscode)
                _uiResult.update { it.copy(isError = true, enteredDigits = emptyList()) }
            }
        }
    }

    companion object {
        private const val MAX_DIGITS = 5
        private const val DELAY_MS = 200L
    }
}
