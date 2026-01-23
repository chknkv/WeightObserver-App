package com.chknkv.weightobserver

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.chknkv.coresession.SessionRepository
import com.chknkv.feature.welcome.presentation.RootWelcomeComponent
import com.chknkv.feature.main.presentation.RootMainComponent
import com.chknkv.feature.welcome.presentation.enterPasscode.EnterPasscodeComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

/**
 * Component for application (using Decompose lib). Contains components of authorized & unauthorized areas
 *
 * @property rootStack Stack in unauthorized area
 */
interface WeightObserverRootComponent {

    val rootStack: Value<ChildStack<*, RootChild>>

    /** Child of main application */
    sealed class RootChild {

        /** Unauthorized area (Welcome) */
        data class Auth(val component: RootWelcomeComponent) : RootChild()

        /** Authorized area (Main) */
        data class Main(val component: RootMainComponent) : RootChild()

        /** Enter Passcode Screen (for authorized users with saved passcode) */
        data class EnterPasscode(val component: EnterPasscodeComponent) : RootChild()
    }
}

/**
 * Implementations of [WeightObserverRootComponent]
 *
 * @param componentContext ComponentContext
 */
class WeightObserverRootComponentImpl(
    componentContext: ComponentContext,
    private val sessionRepository: SessionRepository,
) : WeightObserverRootComponent, ComponentContext by componentContext, KoinComponent {

    private val navigation = StackNavigation<RootConfig>()

    override val rootStack: Value<ChildStack<*, WeightObserverRootComponent.RootChild>> = childStack(
        source = navigation,
        serializer = RootConfig.serializer(),
        initialConfiguration = getInitialConfig(),
        handleBackButton = false,
        childFactory = ::startChild
    )

    private fun getInitialConfig(): RootConfig {
        return if (sessionRepository.isFirstAuthorized) {
            if (sessionRepository.getPasscodeHash() != null) {
                RootConfig.EnterPasscode
            } else {
                RootConfig.Main
            }
        } else {
            RootConfig.Auth
        }
    }

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
                    { navigation.replaceAll(RootConfig.Main) } // Fixed: Use replaceAll for clean transition
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

        RootConfig.EnterPasscode -> WeightObserverRootComponent.RootChild.EnterPasscode(
            component = get<EnterPasscodeComponent> {
                parametersOf(
                    context,
                    { navigation.replaceAll(RootConfig.Main) },
                    { navigation.replaceAll(RootConfig.Auth) }
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

        @Serializable
        data object EnterPasscode : RootConfig
    }
}
