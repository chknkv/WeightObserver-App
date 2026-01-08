package com.chknkv.feature.welcome.presentation.enterPasscode

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.chknkv.coresession.SessionRepository
import com.chknkv.feature.welcome.model.presentation.enterPasscode.EnterPasscodeUiEffect
import com.chknkv.feature.welcome.model.presentation.enterPasscode.EnterPasscodeUiAction
import com.chknkv.feature.welcome.model.presentation.enterPasscode.EnterPasscodeUiResult
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
 * Component for EnterPasscode screen (Login with PIN)
 */
interface EnterPasscodeComponent {

    /** Ui result for EnterPasscode screen */
    val uiResult: StateFlow<EnterPasscodeUiResult>

    /** Ui effect for EnterPasscode screen */
    val uiEffect: SharedFlow<EnterPasscodeUiEffect>

    /** Init & load screen flow */
    fun initLoadEnterPasscodeScreen()

    /**
     * Emit action from UI
     *
     * @param action Ui action for EnterPasscode screen
     */
    fun emitAction(action: EnterPasscodeUiAction)
}

/**
 * Implementation of [EnterPasscodeComponent]
 *
 * @param componentContext ComponentContext
 * @param sessionRepository SessionRepository
 * @param onAuthSuccess Callback for successful authentication (navigate to Main)
 * @param onForgotPasscode Callback when user forgets passcode (navigate to Welcome/Login flow)
 */
internal class EnterPasscodeComponentImpl(
    componentContext: ComponentContext,
    private val sessionRepository: SessionRepository,
    private val onAuthSuccess: () -> Unit,
    private val onForgotPasscode: () -> Unit
) : EnterPasscodeComponent, ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        lifecycle.subscribe(object : Lifecycle.Callbacks {
            override fun onDestroy() {
                coroutineScope.cancel()
            }
        })
    }

    override val uiEffect: SharedFlow<EnterPasscodeUiEffect> get() = _uiEffect.asSharedFlow()
    private val _uiEffect = MutableSharedFlow<EnterPasscodeUiEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val uiResult: StateFlow<EnterPasscodeUiResult> get() = _uiResult.asStateFlow()
    private val _uiResult = MutableStateFlow(EnterPasscodeUiResult())

    private val _enterPasscodeFlow = MutableSharedFlow<EnterPasscodeUiAction>()
    private var isFirstLoadEnterPasscodeScreenFlag = true

    override fun initLoadEnterPasscodeScreen() {
        if (!isFirstLoadEnterPasscodeScreenFlag) return
        isFirstLoadEnterPasscodeScreenFlag = false

        _enterPasscodeFlow
            .onStart { emitAction(EnterPasscodeUiAction.Init) }
            .onEach { action ->
                when (action) {
                    is EnterPasscodeUiAction.Init -> Unit
                    is EnterPasscodeUiAction.NumberClick -> handleNumberClick(action.digit, _uiResult.value)
                    is EnterPasscodeUiAction.DeleteClick -> _uiResult.update { oldUiResult ->
                        oldUiResult.copy(
                            enteredDigits = oldUiResult.enteredDigits.dropLast(DROP_LAST_COUNTER),
                            isError = false
                        )
                    }
                    is EnterPasscodeUiAction.ForgotPasscode -> {
                        sessionRepository.clearAll()
                        _uiResult.update { it.copy(isShowForgotDialog = false) }
                        onForgotPasscode()
                    }
                    is EnterPasscodeUiAction.ShowForgotDialog -> {
                        _uiResult.update { it.copy(isShowForgotDialog = true) }
                    }
                    is EnterPasscodeUiAction.HideForgotDialog -> {
                        _uiResult.update { it.copy(isShowForgotDialog = false) }
                    }
                }
            }
            .launchIn(coroutineScope)
    }

    override fun emitAction(action: EnterPasscodeUiAction) {
        coroutineScope.launch { _enterPasscodeFlow.emit(action) }
    }

    private fun handleNumberClick(digit: Int, oldUiResult: EnterPasscodeUiResult) {
        val current = oldUiResult.enteredDigits
        if (current.size >= MAX_DIGITS_COUNTER) return

        val newDigits = current + digit
        _uiResult.update { it.copy(enteredDigits = newDigits, isError = false) }

        if (newDigits.size == MAX_DIGITS_COUNTER) {
            coroutineScope.launch {
                delay(DELAY)
                val enteredPasscode = newDigits.joinToString("")
                val savedPasscode = sessionRepository.getPasscodeHash()

                if (enteredPasscode == savedPasscode) {
                    Napier.i("Passcode correct", tag = TAG)
                    onAuthSuccess()
                } else {
                    Napier.w("Passcode incorrect", tag = TAG)
                    _uiEffect.emit(EnterPasscodeUiEffect.InvalidPasscode)
                    _uiResult.update { it.copy(isError = true, enteredDigits = emptyList()) }
                }
            }
        }
    }

    companion object {
        private const val TAG = "EnterPasscodeComponent"
        private const val MAX_DIGITS_COUNTER = 5
        private const val DROP_LAST_COUNTER = 1
        private const val DELAY = 200L
    }
}
