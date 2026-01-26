package com.chknkv.coreauthentication.domain

import com.chknkv.coreauthentication.models.domain.BiometricContext
import com.chknkv.coreauthentication.models.domain.BiometricResult
import com.chknkv.coreauthentication.models.domain.BiometricType
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics
import kotlin.coroutines.resume

/**
 * iOS implementation of [BiometricAuthenticator] using LocalAuthentication framework.
 */
@OptIn(ExperimentalForeignApi::class)
actual class BiometricAuthenticatorImpl : BiometricAuthenticator {

    override suspend fun isBiometricAvailable(): Boolean {
        return memScoped {
            val context = LAContext()
            val errorPtr = alloc<ObjCObjectVar<NSError?>>()
            try {
                context.canEvaluatePolicy(
                    policy = LAPolicyDeviceOwnerAuthenticationWithBiometrics,
                    error = errorPtr.ptr
                )
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun getBiometricType(): BiometricType {
        if (!isBiometricAvailable()) {
            return BiometricType.NONE
        }

        return memScoped {
            val context = LAContext()
            val errorPtr = alloc<ObjCObjectVar<NSError?>>()
            val canEvaluate = try {
                context.canEvaluatePolicy(
                    policy = LAPolicyDeviceOwnerAuthenticationWithBiometrics,
                    error = errorPtr.ptr
                )
            } catch (e: Exception) {
                false
            }

            if (!canEvaluate) {
                return BiometricType.NONE
            }

            // Check biometric type
            // On iOS, we can check the biometryType property (iOS 11+)
            try {
                val biometryType = context.biometryType
                when (biometryType.toInt()) {
                    1 -> BiometricType.FACE_ID  // LABiometryTypeFaceID
                    2 -> BiometricType.TOUCH_ID // LABiometryTypeTouchID
                    else -> BiometricType.NONE
                }
            } catch (e: Exception) {
                BiometricType.TOUCH_ID
            }
        }
    }

    override suspend fun authenticate(context: BiometricContext, reason: String): BiometricResult {
        val laContext = LAContext()
        
        val canEvaluate = memScoped {
            val errorPtr = alloc<ObjCObjectVar<NSError?>>()
            try {
                laContext.canEvaluatePolicy(
                    policy = LAPolicyDeviceOwnerAuthenticationWithBiometrics,
                    error = errorPtr.ptr
                )
            } catch (e: Exception) {
                false
            }
        }

        if (!canEvaluate) {
            return BiometricResult.NotAvailable
        }

        return suspendCancellableCoroutine { continuation ->
            laContext.evaluatePolicy(
                policy = LAPolicyDeviceOwnerAuthenticationWithBiometrics,
                localizedReason = reason
            ) { success, error ->
                if (success) {
                    continuation.resume(BiometricResult.Success)
                } else {
                    val nsError = error
                    if (nsError != null) {
                        val errorCode = nsError.code.toLong()

                        // Error codes from LAError enum
                        when (errorCode) {
                            -1L -> continuation.resume(BiometricResult.Cancelled) // LAErrorUserCancel
                            -2L -> continuation.resume(BiometricResult.Cancelled) // LAErrorUserFallback
                            -3L -> continuation.resume(BiometricResult.Error("System cancelled")) // LAErrorSystemCancel
                            -4L -> continuation.resume(BiometricResult.NotEnrolled) // LAErrorPasscodeNotSet
                            -5L -> continuation.resume(BiometricResult.NotEnrolled) // LAErrorTouchIDNotAvailable
                            -6L -> continuation.resume(BiometricResult.NotEnrolled) // LAErrorTouchIDNotEnrolled
                            -7L -> continuation.resume(BiometricResult.Error("Touch ID is locked")) // LAErrorTouchIDLockout
                            -8L -> continuation.resume(BiometricResult.Error("App cancelled")) // LAErrorAppCancel
                            -9L -> continuation.resume(BiometricResult.Error("Invalid context")) // LAErrorInvalidContext
                            else -> continuation.resume(BiometricResult.Error(nsError.localizedDescription ?: "Unknown error"))
                        }
                    } else {
                        continuation.resume(BiometricResult.Error("Authentication failed"))
                    }
                }
            }

            continuation.invokeOnCancellation {
                try {
                    laContext.invalidate()
                } catch (_: Exception) {
                }
            }
        }
    }
}
