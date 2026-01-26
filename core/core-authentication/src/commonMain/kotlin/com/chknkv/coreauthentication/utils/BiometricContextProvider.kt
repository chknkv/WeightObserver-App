package com.chknkv.coreauthentication.utils

import androidx.compose.runtime.Composable
import com.chknkv.coreauthentication.models.domain.BiometricContext

/**
 * Platform-specific provider of [BiometricContext] for biometric authentication.
 * Returns non-null when biometric prompts can be shown (e.g. host activity on Android).
 *
 * @return [BiometricContext] if available, or null if not (e.g. iOS placeholder, wrong host type).
 */
@Composable
internal expect fun getBiometricContext(): BiometricContext?
