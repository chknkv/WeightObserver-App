package com.chknkv.weightobserver

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.chknkv.coredesignsystem.theming.AcTheme
import com.chknkv.feature.welcome.presentation.RootWelcomeScreen
import com.chknkv.feature.main.presentation.RootMainScreen

/**
 * The main UI-function, which contains the entire core UI of the application,
 * holds all components for both authorized and unauthorized zones.
 */
@Composable
internal fun WeightObserverAppUiRoot(rootComponent: WeightObserverRootComponent) {
    AcTheme {
        Children(
            stack = rootComponent.rootStack,
            animation = stackAnimation(slide())
        ) {
            when (val child = it.instance) {
                is WeightObserverRootComponent.RootChild.Auth -> RootWelcomeScreen(component = child.component)
                is WeightObserverRootComponent.RootChild.Main -> RootMainScreen(component = child.component)
            }
        }
    }
}
