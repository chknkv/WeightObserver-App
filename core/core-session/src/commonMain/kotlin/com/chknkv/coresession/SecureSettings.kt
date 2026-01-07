package com.chknkv.coresession

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Expects a platform-specific dependency injection [Module] that provides secure key–value storage.
 */
expect val coreStorageModule: Module

/**
 * Koin module that provides session-related dependencies for the application.
 *
 */
val coreSessionModule = module {
    single<SessionRepository> {
        SessionRepositoryImpl(
            secureSettings = get(SECURE_SETTINGS_QUALIFIER)
        )
    }
}

/**
 * A Koin qualifier used to distinguish secure instances from other providers that may exist in the project.
 */
val SECURE_SETTINGS_QUALIFIER = named("secureSettings")