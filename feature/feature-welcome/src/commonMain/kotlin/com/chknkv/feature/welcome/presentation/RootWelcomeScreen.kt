package com.chknkv.feature.welcome.presentation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.chknkv.feature.welcome.presentation.createPasscode.CreatePasscodeScreen
import com.chknkv.feature.welcome.presentation.information.InformationScreen

/**
 * UI container for welcome screens.
 *
 * Uses [Children] from Decompose to display the currently active screen from the [RootWelcomeComponent.childStack].
 * Applies slide animation when transitioning between screens.
 *
 * @param component Root welcome component managing the navigation.
 */
@Composable
fun RootWelcomeScreen(component: RootWelcomeComponent) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(slide())
    ) {
        when (val child = it.instance) {
            is RootWelcomeComponent.Child.Information -> InformationScreen(child.component)
            is RootWelcomeComponent.Child.CreatePasscode -> CreatePasscodeScreen(child.component)
        }
    }
}
