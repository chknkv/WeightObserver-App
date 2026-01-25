package com.chknkv.weightobserver

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.chknkv.coredesignsystem.theming.AcTheme
import com.chknkv.coresession.LanguageManager
import com.chknkv.feature.welcome.presentation.RootWelcomeScreen
import com.chknkv.feature.main.presentation.RootMainScreen
import com.chknkv.feature.welcome.presentation.enterPasscode.EnterPasscodeScreen
import org.koin.compose.koinInject

/**
 * The main UI-function, which contains the entire core UI of the application,
 * holds all components for both authorized and unauthorized zones.
 *
 * @param rootComponent root navigation component
 * @param darkTheme explicit theme override (Android: from system uiMode; iOS: null = use isSystemInDarkTheme)
 */
@Composable
fun WeightObserverAppUiRoot(
    rootComponent: WeightObserverRootComponent,
    darkTheme: Boolean? = null
) {
    val languageManager = koinInject<LanguageManager>()
    val currentLanguage by languageManager.currentLanguage.collectAsState()

    key(currentLanguage) {
        AcTheme(darkTheme = darkTheme) {
            Children(
                stack = rootComponent.rootStack,
                animation = stackAnimation(slide())
            ) {
                when (val child = it.instance) {
                    is WeightObserverRootComponent.RootChild.Auth -> RootWelcomeScreen(component = child.component)
                    is WeightObserverRootComponent.RootChild.Main -> RootMainScreen(component = child.component)
                    is WeightObserverRootComponent.RootChild.EnterPasscode -> EnterPasscodeScreen(component = child.component)
                }
            }
        }
    }
}
