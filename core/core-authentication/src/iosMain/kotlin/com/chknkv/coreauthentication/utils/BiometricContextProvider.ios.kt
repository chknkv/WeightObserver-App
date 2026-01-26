package com.chknkv.coreauthentication.utils

import androidx.compose.runtime.Composable
import com.chknkv.coreauthentication.models.domain.BiometricContext

/**
 * iOS actual: placeholder; returns null. Biometric auth is not yet implemented on iOS.
 */
@Composable
internal actual fun getBiometricContext(): BiometricContext? {
    return null
}
