package com.chknkv.coresession

import com.russhwolf.settings.Settings

/**
 * Repository responsible for managing and persisting user session data in a secure storage.
 *
 * This interface provides methods to save, retrieve, and clear sensitive session-related
 * information such as authentication tokens and the user's passcode hash.
 * Implementations of this interface should ensure that all data is stored securely
 * (e.g., using encrypted preferences or keychain storage).
 *
 * Typical use cases:
 * - Persisting access and refresh tokens after successful authentication.
 * - Retrieving stored tokens for authorized API requests.
 * - Storing and validating a local passcode for quick app re-login.
 * - Clearing all session data when the user logs out.
 */
interface SessionRepository {

    /**
     * Saves the hash of the user's local passcode for quick authentication.
     * Pass `null` to remove the passcode.
     *
     * @param hash A hashed representation of the user's passcode, or null to clear.
     */
    fun savePasscodeHash(hash: String?)

    /**
     * Retrieves the stored passcode hash, if available
     *
     * @return The hashed passcode string, or `null` if not stored
     */
    fun getPasscodeHash(): String?

    /**
     * Clears all session data and user data (e.g. database).
     */
    suspend fun clearAll()

    /**
     * Checks if the user has completed the initial authorization/welcome flow.
     */
    var isFirstAuthorized: Boolean
}

/**
 * Implementation of [SessionRepository]
 *
 * @property secureSettings Secure storage for session data
 */
internal class SessionRepositoryImpl(
    private val secureSettings: Settings,
    private val weightRepository: WeightRepository
) : SessionRepository {

    override fun savePasscodeHash(hash: String?) {
        if (hash == null) {
            secureSettings.remove(PASSCODE_HASH)
        } else {
            secureSettings.putString(PASSCODE_HASH, hash)
        }
    }

    override fun getPasscodeHash(): String? = secureSettings.getStringOrNull(PASSCODE_HASH)

    override suspend fun clearAll() {
        secureSettings.remove(PASSCODE_HASH)
        secureSettings.remove(IS_FIRST_AUTHORIZED)
        weightRepository.clearWeights()
    }

    override var isFirstAuthorized: Boolean
        get() = secureSettings.getBoolean(IS_FIRST_AUTHORIZED, false)
        set(value) { secureSettings.putBoolean(IS_FIRST_AUTHORIZED, value) }

    companion object {
        private const val PASSCODE_HASH = "PASSCODE_HASH"
        private const val IS_FIRST_AUTHORIZED = "IS_FIRST_AUTHORIZED"
    }
}