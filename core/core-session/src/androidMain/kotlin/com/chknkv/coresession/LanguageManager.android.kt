package com.chknkv.coresession

import android.app.LocaleManager
import android.os.Build
import android.os.LocaleList
import com.chknkv.coreutils.appContext
import java.util.Locale

/**
 * Updates the system locale on Android.
 *
 * This function updates the default [Locale], the configuration of the application context resources,
 * and for Android 13+ (Tiramisu), it syncs with the system's per-app language preferences via [LocaleManager].
 *
 * @param languageCode The ISO 639-1 language code.
 */
actual fun updateSystemLocale(languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    
    val context = appContext
    val config = context.resources.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val localeManager = context.getSystemService(LocaleManager::class.java)
        localeManager.applicationLocales = LocaleList(locale)
    }
}

/**
 * Retrieves the platform-specific app language on Android (API 33+).
 *
 * Uses [LocaleManager] to check if the user has set a specific language for this app in system settings.
 *
 * @return The ISO 639-1 language code, or null if not supported or not set.
 */
actual fun getPlatformAppLanguageCode(): String? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val localeManager = appContext.getSystemService(LocaleManager::class.java)
        val locales = localeManager.applicationLocales
        if (!locales.isEmpty) {
            return locales.get(0).language
        }
    }
    return null
}
