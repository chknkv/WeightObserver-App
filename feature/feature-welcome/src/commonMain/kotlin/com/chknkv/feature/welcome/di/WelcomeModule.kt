package com.chknkv.feature.welcome.di

import com.arkivanov.decompose.ComponentContext
import com.chknkv.feature.welcome.presentation.RootWelcomeComponent
import com.chknkv.feature.welcome.presentation.RootWelcomeComponentImpl
import org.koin.dsl.module

/**
 * Koin module for feature-welcome.
 *
 * Provides dependencies for the welcome and onboarding functionality (language selection, information, passcode creation).
 * Declares a factory for [RootWelcomeComponent].
 */
val featureWelcomeModule = module {

    /**
     * Creates an instance of [RootWelcomeComponent].
     *
     * @param componentContext Decompose component context.
     * @param onAuthSuccess Callback invoked upon successful completion of the welcome/authorization process.
     */
    factory<RootWelcomeComponent> { (componentContext: ComponentContext, onAuthSuccess: () -> Unit) ->
        RootWelcomeComponentImpl(
            componentContext = componentContext,
            sessionRepository = get(),
            onAuthSuccess = onAuthSuccess
        )
    }
}
