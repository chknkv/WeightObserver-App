package com.chknkv.feature.welcome.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.chknkv.coreauthentication.AuthenticationFactory
import com.chknkv.coreauthentication.models.domain.handles.biometry.BiometryHandles
import com.chknkv.coreauthentication.models.domain.handles.createpasscode.CreatePasscodeHandles
import com.chknkv.coreauthentication.presentation.biometry.BiometryComponent
import com.chknkv.coreauthentication.presentation.createpasscode.CreatePasscodeComponent
import com.chknkv.coreauthentication.domain.BiometricAuthenticator
import com.chknkv.coreauthentication.domain.PasscodeRepository
import com.chknkv.coresession.SessionRepository
import com.chknkv.feature.welcome.presentation.information.InformationComponent
import com.chknkv.feature.welcome.presentation.information.InformationComponentImpl
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Root component for the Welcome Feature flow.
 *
 * Manages navigation between onboarding screens:
 * 1. [InformationComponent] - Information screen.
 * 2. [CreatePasscodeComponent] - Passcode creation.
 * 3. [BiometryComponent] - Biometrics enable/skip (after passcode created).
 */
interface RootWelcomeComponent {
    /**
     * Stack of child components for navigation.
     */
    val childStack: Value<ChildStack<*, Child>>

    /**
     * Resets navigation to the initial welcome screen (Language Selection).
     */
    fun resetToWelcome()

    /**
     * Completes the welcome/authorization process.
     */
    fun onCompleteAuth()

    /**
     * Defines possible child components (screens) in the welcome flow.
     */
    sealed class Child {

        /**
         * Information screen.
         * @property component Instance of the information component.
         */
        data class Information(val component: InformationComponent) : Child()

        /**
         * Passcode creation screen.
         * @property component Instance of the passcode creation component.
         */
        data class CreatePasscode(val component: CreatePasscodeComponent) : Child()

        /**
         * Biometrics enable/skip screen (after passcode created).
         * @property component Instance of the biometry component.
         */
        data class Biometry(val component: BiometryComponent) : Child()
    }
}

/**
 * Implementation of [RootWelcomeComponent].
 *
 * @property componentContext Decompose component context.
 * @property sessionRepository Repository for saving authorization state.
 * @property onAuthSuccess Callback to notify about flow completion.
 */
class RootWelcomeComponentImpl(
    componentContext: ComponentContext,
    private val sessionRepository: SessionRepository,
    private val onAuthSuccess: () -> Unit
) : RootWelcomeComponent, ComponentContext by componentContext, KoinComponent {

    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, RootWelcomeComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Information,
        handleBackButton = true,
        childFactory = ::child
    )

    /**
     * Factory for creating child components based on configuration.
     */
    private fun child(config: Config, componentContext: ComponentContext): RootWelcomeComponent.Child =
        when (config) {
            Config.Information -> RootWelcomeComponent.Child.Information(
                InformationComponentImpl(
                    componentContext = componentContext,
                    onNext = { navigation.pushNew(Config.CreatePasscode) }
                )
            )

            Config.CreatePasscode -> RootWelcomeComponent.Child.CreatePasscode(
                AuthenticationFactory.createPasscodeComponent(
                    componentContext = componentContext,
                    passcodeRepository = get<PasscodeRepository>(),
                    biometricAuthenticator = get<BiometricAuthenticator>(),
                    handles = CreatePasscodeHandles(
                        onPasscodeCreated = { showBiometry ->
                            if (showBiometry) navigation.pushNew(Config.Biometry)
                            else onCompleteAuth()
                        },
                        onPasscodeSkipped = { onCompleteAuth() },
                        onBack = { navigation.pop() }
                    )
                )
            )

            Config.Biometry -> RootWelcomeComponent.Child.Biometry(
                AuthenticationFactory.createBiometryComponent(
                    componentContext = componentContext,
                    passcodeRepository = get<PasscodeRepository>(),
                    biometricAuthenticator = get<BiometricAuthenticator>(),
                    handles = BiometryHandles(
                        onBiometricEnabled = { onCompleteAuth() },
                        onBiometricSkipped = { onCompleteAuth() }
                    )
                )
            )
        }

    override fun resetToWelcome() {
        navigation.replaceAll(Config.Information)
    }

    override fun onCompleteAuth() {
        sessionRepository.isFirstAuthorized = true
        onAuthSuccess()
    }

    /**
     * Configuration for Decompose navigation.
     */
    @Serializable
    private sealed interface Config {

        @Serializable
        data object Information : Config

        @Serializable
        data object CreatePasscode : Config

        @Serializable
        data object Biometry : Config
    }
}
