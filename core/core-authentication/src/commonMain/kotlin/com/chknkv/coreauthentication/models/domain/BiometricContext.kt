package com.chknkv.coreauthentication.models.domain

/**
 * Platform-specific context for biometric authentication.
 * This is used to pass platform-specific objects (like Activity on Android or UIViewController on iOS)
 * to the authenticator.
 */
expect class BiometricContext
