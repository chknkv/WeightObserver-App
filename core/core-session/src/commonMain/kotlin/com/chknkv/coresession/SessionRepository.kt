package com.chknkv.coresession

import com.russhwolf.settings.Settings

/**
 * Repository responsible for managing and persisting user session data in a secure storage.
 *
 * This interface provides methods to save, retrieve, and clear sensitive session-related
 * information such as authentication tokens.
 * Implementations of this interface should ensure that all data is stored securely
 * (e.g., using encrypted preferences or keychain storage).
 *
 * Typical use cases:
 * - Persisting access and refresh tokens after successful authentication.
 * - Retrieving stored tokens for authorized API requests.
 * - Clearing all session data when the user logs out.
 */
interface SessionRepository {

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

    override suspend fun clearAll() {
        secureSettings.remove(IS_FIRST_AUTHORIZED)
        weightRepository.clearWeights()
    }

    override var isFirstAuthorized: Boolean
        get() = secureSettings.getBoolean(IS_FIRST_AUTHORIZED, false)
        set(value) { secureSettings.putBoolean(IS_FIRST_AUTHORIZED, value) }

    companion object {
        private const val IS_FIRST_AUTHORIZED = "IS_FIRST_AUTHORIZED"
    }
}