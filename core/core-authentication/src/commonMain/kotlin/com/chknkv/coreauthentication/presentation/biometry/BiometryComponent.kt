package com.chknkv.coreauthentication.presentation.biometry

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.chknkv.coreauthentication.domain.BiometricAuthenticator
import com.chknkv.coreauthentication.domain.PasscodeRepository
import com.chknkv.coreauthentication.models.domain.BiometricContext
import com.chknkv.coreauthentication.models.domain.BiometricResult
import com.chknkv.coreauthentication.models.domain.handles.biometry.BiometryHandles
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Component for the Biometry screen. Enables or skips biometric auth after passcode creation.
 */
interface BiometryComponent {

    /**
     * Triggers platform biometric prompt. On success, enables biometrics and invokes
     * [BiometryHandles.onBiometricEnabled]; otherwise no-op.
     *
     * @param context Platform [BiometricContext] (e.g. from getBiometricContext).
     */
    fun enableBiometrics(context: BiometricContext)

    /**
     * User skipped biometrics. Disables biometrics and invokes [BiometryHandles.onBiometricSkipped].
     */
    fun skipBiometrics()
}

/**
 * Implementation of [BiometryComponent].
 */
internal class BiometryComponentImpl(
    componentContext: ComponentContext,
    private val passcodeRepository: PasscodeRepository,
    private val biometricAuthenticator: BiometricAuthenticator,
    private val handles: BiometryHandles
) : BiometryComponent, ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        lifecycle.subscribe(object : Lifecycle.Callbacks {
            override fun onDestroy() {
                coroutineScope.cancel()
            }
        })
    }

    override fun enableBiometrics(context: BiometricContext) {
        coroutineScope.launch {
            if (!biometricAuthenticator.isBiometricAvailable()) return@launch
            val result = biometricAuthenticator.authenticate(
                context = context,
                reason = "Use your fingerprint or Face ID to enable quick login"
            )
            when (result) {
                is BiometricResult.Success -> {
                    passcodeRepository.isBiometricEnabled = true
                    Napier.i("Biometrics enabled", tag = TAG)
                    handles.onBiometricEnabled()
                }
                else -> { }
            }
        }
    }

    override fun skipBiometrics() {
        passcodeRepository.isBiometricEnabled = false
        handles.onBiometricSkipped()
    }

    companion object {
        private const val TAG = "BiometryComponent"
    }
}
