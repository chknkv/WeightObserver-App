package com.chknkv.coreauthentication.domain

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.chknkv.coreauthentication.models.domain.BiometricContext
import com.chknkv.coreauthentication.models.domain.BiometricResult
import com.chknkv.coreauthentication.models.domain.BiometricType
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.resume

/**
 * Android implementation of [BiometricAuthenticator] using BiometricPrompt API.
 */
actual class BiometricAuthenticatorImpl : BiometricAuthenticator, KoinComponent {

    private val context: Context by inject()

    override suspend fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    override suspend fun getBiometricType(): BiometricType {
        if (!isBiometricAvailable()) {
            return BiometricType.NONE
        }

        val biometricManager = BiometricManager.from(context)
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_WEAK

        return try {
            val canAuthenticate = biometricManager.canAuthenticate(authenticators)
            if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
                BiometricType.TOUCH_ID
            } else {
                BiometricType.NONE
            }
        } catch (_: Exception) {
            BiometricType.NONE
        }
    }

    override suspend fun authenticate(context: BiometricContext, reason: String): BiometricResult {
        val activity = context.activity

        if (!isBiometricAvailable()) {
            return BiometricResult.NotAvailable
        }

        return suspendCancellableCoroutine { continuation ->
            val executor = ContextCompat.getMainExecutor(activity)
            val callback = object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    continuation.resume(BiometricResult.Success)
                }

                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    when (errorCode) {
                        BiometricPrompt.ERROR_USER_CANCELED,
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON -> {
                            continuation.resume(BiometricResult.Cancelled)
                        }
                        BiometricPrompt.ERROR_NO_BIOMETRICS -> {
                            continuation.resume(BiometricResult.NotEnrolled)
                        }
                        else -> {
                            continuation.resume(BiometricResult.Error(errString.toString()))
                        }
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Do NOT resume here. User can retry; dialog stays open.
                }
            }

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle(reason)
                .setNegativeButtonText("Cancel")
                .build()

            var promptRef: BiometricPrompt? = null
            continuation.invokeOnCancellation {
                try {
                    promptRef?.cancelAuthentication()
                } catch (_: Exception) {
                }
            }

            // BiometricPrompt.authenticate() must run on the main thread.
            // Callers (e.g. BiometryComponent) often use Dispatchers.Default; without this,
            // the prompt may not show (or only on retry) and fails inside ModalBottomSheet.
            activity.runOnUiThread {
                try {
                    val prompt = BiometricPrompt(activity, executor, callback)
                    promptRef = prompt
                    prompt.authenticate(promptInfo)
                } catch (e: Exception) {
                    continuation.resume(BiometricResult.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }
}
