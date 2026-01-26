package com.chknkv.coreauthentication.models.domain

import androidx.fragment.app.FragmentActivity

/**
 * Android-specific context wrapper for biometric authentication.
 * Contains the FragmentActivity needed for BiometricPrompt.
 */
actual class BiometricContext(val activity: FragmentActivity)
