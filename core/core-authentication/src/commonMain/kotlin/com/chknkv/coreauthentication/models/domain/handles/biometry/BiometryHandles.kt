package com.chknkv.coreauthentication.models.domain.handles.biometry

/**
 * Callbacks for the Biometry flow.
 *
 * @param onBiometricEnabled Invoked when user enables biometrics (platform prompt success); consumer navigates to Main.
 * @param onBiometricSkipped Invoked when user skips biometrics; consumer navigates to Main.
 */
data class BiometryHandles(
    val onBiometricEnabled: () -> Unit,
    val onBiometricSkipped: () -> Unit
)
