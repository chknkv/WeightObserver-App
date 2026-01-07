package com.chknkv.coreutils

/**
 * Opens a URL in the default browser.
 * Platform-specific implementation handles opening URLs on Android and iOS.
 *
 * @param url The URL to open (must be a valid URL string, e.g., "https://www.example.com")
 */
expect fun openUrl(url: String)
