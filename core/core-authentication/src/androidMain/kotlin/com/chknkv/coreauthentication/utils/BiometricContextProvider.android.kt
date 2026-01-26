package com.chknkv.coreauthentication.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import com.chknkv.coreauthentication.models.domain.BiometricContext

private fun Context.findFragmentActivity(): FragmentActivity? {
    var c: Context? = this
    while (c != null) {
        if (c is FragmentActivity) return c
        c = (c as? ContextWrapper)?.baseContext
    }
    return null
}

@Composable
internal actual fun getBiometricContext(): BiometricContext? {
    val context = LocalContext.current
    return remember(context) {
        context.findFragmentActivity()?.let { BiometricContext(it) }
    }
}
