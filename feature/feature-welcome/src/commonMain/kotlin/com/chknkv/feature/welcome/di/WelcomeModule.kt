package com.chknkv.feature.welcome.di

import com.arkivanov.decompose.ComponentContext
import com.chknkv.feature.welcome.presentation.RootWelcomeComponent
import com.chknkv.feature.welcome.presentation.RootWelcomeComponentImpl
import org.koin.dsl.module

/**
 * Koin module for feature-welcome.
 *
 * Provides [RootWelcomeComponent]. CreatePasscode, EnterPasscode, Biometry come from [com.chknkv.coreauthentication].
 */
val featureWelcomeModule = module {

    factory<RootWelcomeComponent> { (componentContext: ComponentContext, onAuthSuccess: () -> Unit) ->
        RootWelcomeComponentImpl(
            componentContext = componentContext,
            sessionRepository = get(),
            onAuthSuccess = onAuthSuccess
        )
    }
}
