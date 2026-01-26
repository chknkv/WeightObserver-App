package com.chknkv.coreauthentication.models.domain

/**
 * Represents the type of biometric authentication available on the device.
 */
enum class BiometricType {
    FACE_ID,      // Face ID (iOS) or Face Unlock (Android)
    TOUCH_ID,     // Touch ID (iOS) or Fingerprint (Android)
    NONE          // No biometric authentication available
}
