package com.chknkv.weightobserver

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.chknkv.coresession.SessionRepository
import com.chknkv.feature.welcome.presentation.RootWelcomeComponent
import com.chknkv.feature.main.presentation.RootMainComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

/**
 * Component for application (using Decompose lib). Contains components of authorized & unauthorized areas
 *
 * @property rootStack Stack in unauthorized area
 */
internal interface WeightObserverRootComponent {

    val rootStack: Value<ChildStack<*, RootChild>>

    /** Child of main application */
    sealed class RootChild {

        /** Unauthorized area (Welcome) */
        data class Auth(val component: RootWelcomeComponent) : RootChild()

        /** Authorized area (Main) */
        data class Main(val component: RootMainComponent) : RootChild()
    }
}

/**
 * Implementations of [WeightObserverRootComponent]
 *
 * @param componentContext ComponentContext
 */
internal class WeightObserverRootComponentImpl(
    componentContext: ComponentContext,
    sessionRepository: SessionRepository,
) : WeightObserverRootComponent, ComponentContext by componentContext, KoinComponent {

    private val navigation = StackNavigation<RootConfig>()

    override val rootStack: Value<ChildStack<*, WeightObserverRootComponent.RootChild>> = childStack(
        source = navigation,
        serializer = RootConfig.serializer(),
        initialConfiguration = if (sessionRepository.isFirstAuthorized) RootConfig.Main else RootConfig.Auth,
        handleBackButton = false,
        childFactory = ::startChild
    )

    private var previousConfig: RootConfig? = null

    init {
        rootStack.subscribe { stack ->
            val currentConfig = stack.active.configuration as? RootConfig
            if (previousConfig == RootConfig.Main && currentConfig is RootConfig.Auth) {
                val authChild = stack.active.instance as? WeightObserverRootComponent.RootChild.Auth
                authChild?.component?.resetToWelcome()
            }
            previousConfig = currentConfig
        }
    }

    private fun startChild(
        config: RootConfig,
        context: ComponentContext
    ): WeightObserverRootComponent.RootChild = when (config) {
        RootConfig.Auth -> WeightObserverRootComponent.RootChild.Auth(
            component = get<RootWelcomeComponent> {
                parametersOf(
                    context,
                    { navigation.pushNew(RootConfig.Main) }
                )
            }
        )

        RootConfig.Main -> WeightObserverRootComponent.RootChild.Main(
            component = get<RootMainComponent> {
                parametersOf(
                    context,
                    {
                        navigation.replaceAll(RootConfig.Auth)
                    }
                )
            }
        )
    }

    @Serializable
    sealed interface RootConfig {

        @Serializable
        data object Auth : RootConfig

        @Serializable
        data object Main : RootConfig
    }
}

