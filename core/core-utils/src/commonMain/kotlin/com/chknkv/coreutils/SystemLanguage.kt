package com.chknkv.coreutils

/**
 * Retrieves the current system language code.
 *
 * This function returns the language code set in the system settings.
 *
 * @return The two-letter ISO 639-1 language code (e.g., "en", "ru").
 */
expect fun getSystemLanguageCode(): String
