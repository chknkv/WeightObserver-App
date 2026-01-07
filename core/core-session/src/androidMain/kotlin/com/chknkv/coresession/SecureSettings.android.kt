package com.chknkv.coresession

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Android-specific implementation of [coreStorageModule].
 *
 * Provides a secure [Settings] instance that uses [EncryptedSharedPreferences]
 * with AES-256 encryption (via [MasterKey]) for safely persisting sensitive data.
 *
 * This ensures that all tokens, credentials, and other private values are
 * encrypted both at rest and in transit within the app’s storage layer.
 */
actual val coreStorageModule: Module = module {
    single<Settings>(SECURE_SETTINGS_QUALIFIER) {
        val context = androidContext()

        fun createSecurePrefs(): SharedPreferences {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            return EncryptedSharedPreferences.create(
                context,
                "WeightObserver_secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        try {
            // Основная попытка создать защищённое хранилище
            SharedPreferencesSettings(createSecurePrefs())
        } catch (e: Exception) {
            // Если ключ или зашифрованные данные повреждены — пересоздаём
            if (e is javax.crypto.AEADBadTagException || e.cause is javax.crypto.AEADBadTagException) {
                context.deleteSharedPreferences("WeightObserver_secure_prefs")
                SharedPreferencesSettings(createSecurePrefs())
            } else {
                throw e
            }
        }
    }
}