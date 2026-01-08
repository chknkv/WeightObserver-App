package com.chknkv.feature.welcome.di

import com.arkivanov.decompose.ComponentContext
import com.chknkv.feature.welcome.presentation.RootWelcomeComponent
import com.chknkv.feature.welcome.presentation.RootWelcomeComponentImpl
import com.chknkv.feature.welcome.presentation.enterPasscode.EnterPasscodeComponent
import com.chknkv.feature.welcome.presentation.enterPasscode.EnterPasscodeComponentImpl
import org.koin.dsl.module

/**
 * Koin module for feature-welcome.
 *
 * Provides dependencies for the welcome and onboarding functionality (language selection, information, passcode creation).
 * Declares a factory for [RootWelcomeComponent] and [EnterPasscodeComponent].
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

    /**
     * Creates an instance of [EnterPasscodeComponent].
     *
     * @param componentContext Decompose component context.
     * @param onAuthSuccess Callback invoked upon successful authentication.
     * @param onForgotPasscode Callback invoked when user forgot passcode.
     */
    factory<EnterPasscodeComponent> { (componentContext: ComponentContext, onAuthSuccess: () -> Unit, onForgotPasscode: () -> Unit) ->
        EnterPasscodeComponentImpl(
            componentContext = componentContext,
            sessionRepository = get(),
            onAuthSuccess = onAuthSuccess,
            onForgotPasscode = onForgotPasscode
        )
    }
}
