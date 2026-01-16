package com.chknkv.feature.main.di

import com.arkivanov.decompose.ComponentContext
import com.chknkv.feature.main.presentation.RootMainComponent
import com.chknkv.feature.main.presentation.RootMainComponentImpl
import org.koin.dsl.module

/**
 * Koin module for feature-main.
 *
 * Provides dependencies for the main application functionality after authorization.
 * Declares a factory for [RootMainComponent].
 */
val featureMainModule = module {

    /**
     * Creates an instance of [RootMainComponent].
     *
     * @param componentContext Decompose component context.
     * @param onSignOut Callback invoked when sign-out is requested.
     */
    factory<RootMainComponent> { (componentContext: ComponentContext, onSignOut: () -> Unit) ->
        RootMainComponentImpl(
            componentContext = componentContext,
            sessionRepository = get(),
            weightRepository = get(),
            onSignOutRequested = onSignOut
        )
    }
}
