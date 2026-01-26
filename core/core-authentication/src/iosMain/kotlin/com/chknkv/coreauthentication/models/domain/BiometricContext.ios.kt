package com.chknkv.coreauthentication.models.domain

import platform.UIKit.UIViewController

/**
 * iOS-specific context wrapper for biometric authentication.
 * Contains the UIViewController needed for presenting authentication.
 */
actual class BiometricContext(val viewController: UIViewController)
