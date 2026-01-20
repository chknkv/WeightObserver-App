package com.chknkv.feature.main.di

import com.arkivanov.decompose.ComponentContext
import com.chknkv.feature.main.domain.MainScreenInteractor
import com.chknkv.feature.main.domain.MainScreenInteractorImpl
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

    single<MainScreenInteractor> {
        MainScreenInteractorImpl(
            weightRepository = get(),
            sessionRepository = get()
        )
    }

    /**
     * Creates an instance of [RootMainComponent].
     */
    factory<RootMainComponent> { (componentContext: ComponentContext, onSignOut: () -> Unit) ->
        RootMainComponentImpl(
            componentContext = componentContext,
            mainScreenInteractor = get(),
            onSignOutRequested = onSignOut
        )
    }
}
