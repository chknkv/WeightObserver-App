package com.chknkv.coreutils

import android.content.Intent
import android.net.Uri

/**
 * Android implementation of [openUrl].
 * Opens the URL in the default browser using an Intent.
 *
 * @param url The URL to open
 */
actual fun openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    appContext.startActivity(intent)
}
