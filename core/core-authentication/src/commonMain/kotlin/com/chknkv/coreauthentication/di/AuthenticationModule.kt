package com.chknkv.coreauthentication.di

import com.chknkv.coreauthentication.domain.BiometricAuthenticator
import com.chknkv.coreauthentication.domain.BiometricAuthenticatorImpl
import com.chknkv.coreauthentication.domain.PasscodeRepository
import com.chknkv.coreauthentication.domain.PasscodeRepositoryImpl
import com.chknkv.coresession.SECURE_SETTINGS_QUALIFIER
import org.koin.dsl.module

/**
 * Koin module that provides authentication-related dependencies.
 */
val coreAuthenticationModule = module {
    single<PasscodeRepository> {
        PasscodeRepositoryImpl(
            secureSettings = get(SECURE_SETTINGS_QUALIFIER)
        )
    }

    single<BiometricAuthenticator> {
        BiometricAuthenticatorImpl()
    }
}
