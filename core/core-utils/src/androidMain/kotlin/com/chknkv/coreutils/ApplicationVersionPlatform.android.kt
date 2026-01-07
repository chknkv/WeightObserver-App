package com.chknkv.coreutils

import android.content.Context
import android.content.pm.PackageManager

lateinit var appContext: Context

actual fun getAppVersion(): String {
    return try {
        val pInfo = appContext.packageManager.getPackageInfo(appContext.packageName, 0)
        pInfo.versionName ?: "unknown"
    } catch (e: PackageManager.NameNotFoundException) {
        "unknown"
    }
}