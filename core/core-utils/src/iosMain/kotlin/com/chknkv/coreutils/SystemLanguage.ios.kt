package com.chknkv.coreutils

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

/**
 * Retrieves the current system language code on iOS.
 *
 * Implementation uses [NSLocale.currentLocale] to access the current locale
 * and returns its language code.
 *
 * @return The two-letter ISO 639-1 language code.
 */
actual fun getSystemLanguageCode(): String {
    return NSLocale.currentLocale.languageCode
}
