package com.chknkv.coreutils

import java.util.Locale

/**
 * Retrieves the current system language code on Android.
 *
 * Implementation uses [Locale.getDefault()] to access the default locale
 * and returns its language component.
 *
 * @return The two-letter ISO 639-1 language code.
 */
actual fun getSystemLanguageCode(): String {
    return Locale.getDefault().language
}
