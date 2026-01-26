package com.chknkv.coreauthentication.models.domain

/**
 * Result of a biometric authentication attempt.
 */
sealed class BiometricResult {
    /** Authentication succeeded; user is verified. */
    object Success : BiometricResult()

    /**
     * Authentication failed due to an error.
     * @param message Human-readable error description.
     */
    data class Error(val message: String) : BiometricResult()

    /** User cancelled the authentication prompt (e.g. tapped "Cancel" or dismissed). */
    object Cancelled : BiometricResult()

    /** Biometric authentication is not available on this device (hardware or system support missing). */
    object NotAvailable : BiometricResult()

    /** Biometric authentication is available but not enrolled (no fingerprints/Face ID set up). */
    object NotEnrolled : BiometricResult()
}
