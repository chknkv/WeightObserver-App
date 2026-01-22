package com.chknkv.coresession

import platform.Foundation.NSUserDefaults

/**
 * Updates the system locale on iOS.
 *
 * This implementation updates the "AppleLanguages" key in [NSUserDefaults], which tells iOS
 * which language to use for the app on the next launch.
 *
 * @param languageCode The ISO 639-1 language code.
 */
actual fun updateSystemLocale(languageCode: String) {
    val defaults = NSUserDefaults.standardUserDefaults
    defaults.setObject(arrayListOf(languageCode), forKey = "AppleLanguages")
}

/**
 * Retrieves the platform-specific app language on iOS.
 *
 * Checks the "AppleLanguages" key in [NSUserDefaults] to see if a preferred language
 * has been set for the app (e.g. via iOS Settings).
 *
 * @return The ISO 639-1 language code, or null if not found.
 */
actual fun getPlatformAppLanguageCode(): String? {
    val defaults = NSUserDefaults.standardUserDefaults
    val languages = defaults.objectForKey("AppleLanguages") as? List<*>
    return languages?.firstOrNull() as? String
}
