package com.chknkv.coresession

import com.russhwolf.settings.Settings

/**
 * Implementation of [SessionRepository]
 *
 * @property secureSettings Secure storage for session data
 */
internal class SessionRepositoryImpl(
    private val secureSettings: Settings
) : SessionRepository {

    override fun savePasscodeHash(hash: String) { secureSettings.putString(PASSCODE_HASH, hash) }

    override fun getPasscodeHash(): String? = secureSettings.getStringOrNull(PASSCODE_HASH)

    override var isFirstAuthorized: Boolean
        get() = secureSettings.getBoolean(IS_FIRST_AUTHORIZED, false)
        set(value) { secureSettings.putBoolean(IS_FIRST_AUTHORIZED, value) }

    companion object {
        private const val PASSCODE_HASH = "PASSCODE_HASH"
        private const val IS_FIRST_AUTHORIZED = "IS_FIRST_AUTHORIZED"
    }
}