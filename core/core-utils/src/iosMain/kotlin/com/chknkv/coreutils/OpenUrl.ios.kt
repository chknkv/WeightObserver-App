package com.chknkv.coreutils

import platform.UIKit.UIApplication
import platform.Foundation.NSURL

/**
 * iOS implementation of [openUrl].
 * Opens the URL in the default browser using UIApplication.
 * Uses the modern API with completion handler for iOS 10+.
 *
 * @param url The URL to open
 */
actual fun openUrl(url: String) {
    val nsUrl = NSURL.URLWithString(url)
    nsUrl?.let { urlToOpen ->
        val application = UIApplication.sharedApplication
        application.openURL(
            url = urlToOpen,
            options = mapOf<Any?, Any>(),
            completionHandler = { _ -> }
        )
    }
}
