package com.chknkv.coresession

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.chknkv.coresession.db.WeightDatabase
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * iOS-specific implementation of [coreStorageModule].
 *
 * Provides a secure [Settings] instance based on [KeychainSettings],
 * which uses the iOS Keychain for securely persisting sensitive data
 * such as authentication tokens and passcodes.
 *
 * All stored values are encrypted and protected by the system.
 */
@OptIn(ExperimentalSettingsImplementation::class)
actual val coreStorageModule: Module = module {

    single<Settings>(SECURE_SETTINGS_QUALIFIER) {
        KeychainSettings(service = "WeightObserverApp")
    }

    single<SqlDriver> {
        NativeSqliteDriver(WeightDatabase.Schema, "weight.db")
    }
}