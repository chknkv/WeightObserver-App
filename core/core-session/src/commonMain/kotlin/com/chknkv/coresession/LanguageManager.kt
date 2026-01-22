package com.chknkv.coresession

import com.chknkv.coreutils.getSystemLanguageCode
import com.russhwolf.settings.Settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages the application's language settings.
 *
 * This interface provides methods to get and set the current language, as well as observing language changes.
 * It also handles synchronization with platform-specific settings.
 */
interface LanguageManager {
    /**
     * A [StateFlow] emitting the current language code (e.g., "en", "ru").
     * Observers can collect this flow to react to language changes in real-time.
     */
    val currentLanguage: StateFlow<String>

    /**
     * Retrieves the current language code.
     *
     * Prioritizes the platform-specific app language setting (if available), otherwise falls back to the
     * internal saved preference or the system default.
     *
     * @return The ISO 639-1 language code.
     */
    fun getCurrentLanguageCode(): String

    /**
     * Sets the application language.
     *
     * This updates the internal storage, the system locale configuration, and notifies observers.
     *
     * @param languageCode The ISO 639-1 language code to set.
     */
    fun setLanguage(languageCode: String)

    /**
     * Initializes the language manager.
     *
     * Should be called on application startup to ensure the correct locale is applied
     * based on saved preferences or system settings.
     */
    fun init()
}

internal class LanguageManagerImpl(
    private val settings: Settings
) : LanguageManager {

    private val _currentLanguage = MutableStateFlow(getCurrentLanguageCode())
    override val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    override fun getCurrentLanguageCode(): String {
        val platformLang = getPlatformAppLanguageCode()
        if (platformLang != null) {
            if (settings.getStringOrNull(KEY_LANGUAGE) != platformLang) {
                settings.putString(KEY_LANGUAGE, platformLang)
            }
            return platformLang
        }
        return settings.getString(KEY_LANGUAGE, getSystemLanguageCode())
    }

    override fun setLanguage(languageCode: String) {
        settings.putString(KEY_LANGUAGE, languageCode)
        updateSystemLocale(languageCode)
        _currentLanguage.value = languageCode
    }

    override fun init() {
        val languageCode = getCurrentLanguageCode()
        updateSystemLocale(languageCode)
        _currentLanguage.value = languageCode
    }

    companion object {
        private const val KEY_LANGUAGE = "selected_language"
    }
}

/**
 * Updates the system's locale configuration for the current application context.
 *
 * @param languageCode The ISO 639-1 language code to apply.
 */
expect fun updateSystemLocale(languageCode: String)

/**
 * Retrieves the language code explicitly set for this application by the platform system settings, if any.
 *
 * @return The ISO 639-1 language code, or null if no app-specific language is set.
 */
expect fun getPlatformAppLanguageCode(): String?
