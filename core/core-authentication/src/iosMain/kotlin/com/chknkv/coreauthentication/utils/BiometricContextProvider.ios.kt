package com.chknkv.coreauthentication.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.chknkv.coreauthentication.models.domain.BiometricContext
import platform.UIKit.UIApplication

/**
 * iOS actual: returns BiometricContext with the current UIViewController.
 * Gets the root view controller from the key window.
 */
@Composable
internal actual fun getBiometricContext(): BiometricContext? {
    return remember {
        try {
            val application = UIApplication.sharedApplication
            val keyWindow = application.keyWindow
            val rootViewController = keyWindow?.rootViewController
            if (rootViewController != null) {
                BiometricContext(rootViewController)
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}
