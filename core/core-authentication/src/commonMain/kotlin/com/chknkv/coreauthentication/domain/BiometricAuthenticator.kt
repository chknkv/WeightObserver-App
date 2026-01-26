package com.chknkv.coreauthentication.domain

import com.chknkv.coreauthentication.models.domain.BiometricContext
import com.chknkv.coreauthentication.models.domain.BiometricResult
import com.chknkv.coreauthentication.models.domain.BiometricType

/**
 * Interface for biometric authentication across platforms.
 * Provides methods to check availability and perform authentication.
 */
interface BiometricAuthenticator {
    /**
     * Checks if biometric authentication is available on the device.
     * @return true if biometric authentication is available and enrolled, false otherwise
     */
    suspend fun isBiometricAvailable(): Boolean

    /**
     * Gets the type of biometric authentication available on the device.
     * @return [BiometricType] indicating the available biometric method, or [BiometricType.NONE] if not available
     */
    suspend fun getBiometricType(): BiometricType

    /**
     * Performs biometric authentication.
     * @param context Platform-specific context (Activity on Android, UIViewController on iOS)
     * @param reason The reason for authentication (shown to the user)
     * @return [BiometricResult] indicating the result of the authentication attempt
     */
    suspend fun authenticate(context: BiometricContext, reason: String): BiometricResult
}

/**
 * Expect declaration for platform-specific biometric authenticator implementation.
 */
expect class BiometricAuthenticatorImpl() : BiometricAuthenticator
