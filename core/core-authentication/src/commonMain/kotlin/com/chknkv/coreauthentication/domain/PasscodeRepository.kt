package com.chknkv.coreauthentication.domain

import com.russhwolf.settings.Settings

/**
 * Repository for managing passcode and biometric authentication settings.
 * Handles persistence of passcode hash and biometric enabled state.
 */
interface PasscodeRepository {

    /**
     * Saves the hash of the user's local passcode for quick authentication.
     * Pass `null` to remove the passcode.
     *
     * @param hash A hashed representation of the user's passcode, or null to clear.
     */
    fun savePasscodeHash(hash: String?)

    /**
     * Retrieves the stored passcode hash, if available.
     *
     * @return The hashed passcode string, or `null` if not stored.
     */
    fun getPasscodeHash(): String?

    /**
     * Checks if biometric authentication is enabled for the user.
     */
    var isBiometricEnabled: Boolean
}

/**
 * Implementation of [PasscodeRepository] using secure settings.
 */
internal class PasscodeRepositoryImpl(
    private val secureSettings: Settings
) : PasscodeRepository {

    override fun savePasscodeHash(hash: String?) {
        if (hash == null) {
            secureSettings.remove(PASSCODE_HASH)
        } else {
            secureSettings.putString(PASSCODE_HASH, hash)
        }
    }

    override fun getPasscodeHash(): String? = secureSettings.getStringOrNull(PASSCODE_HASH)

    override var isBiometricEnabled: Boolean
        get() = secureSettings.getBoolean(IS_BIOMETRIC_ENABLED, false)
        set(value) { secureSettings.putBoolean(IS_BIOMETRIC_ENABLED, value) }

    companion object {
        private const val PASSCODE_HASH = "PASSCODE_HASH"
        private const val IS_BIOMETRIC_ENABLED = "IS_BIOMETRIC_ENABLED"
    }
}
