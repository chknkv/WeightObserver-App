package com.chknkv.coresession

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
     * Saves the hash of the user's local passcode for quick authentication
     *
     * @param hash A hashed representation of the user's passcode
     */
    fun savePasscodeHash(hash: String)

    /**
     * Retrieves the stored passcode hash, if available
     *
     * @return The hashed passcode string, or `null` if not stored
     */
    fun getPasscodeHash(): String?

    /**
     * Checks if the user has completed the initial authorization/welcome flow.
     */
    var isFirstAuthorized: Boolean
}