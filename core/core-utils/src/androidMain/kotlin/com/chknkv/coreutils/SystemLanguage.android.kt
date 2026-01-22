package com.chknkv.coreutils

import android.content.res.Resources
import android.os.Build

/**
 * Retrieves the current system language code on Android.
 *
 * Implementation uses [Resources.getSystem()] to access the device's
 * global configuration, ensuring we get the true system language
 * even if the app has overridden the default Locale.
 *
 * @return The two-letter ISO 639-1 language code.
 */
actual fun getSystemLanguageCode(): String {
    val configuration = Resources.getSystem().configuration
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        configuration.locales[0].language
    } else {
        @Suppress("DEPRECATION")
        configuration.locale.language
    }
}
