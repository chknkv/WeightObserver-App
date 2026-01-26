package com.chknkv.coreauthentication

import com.arkivanov.decompose.ComponentContext
import com.chknkv.coreauthentication.models.domain.handles.biometry.BiometryHandles
import com.chknkv.coreauthentication.models.domain.handles.createpasscode.CreatePasscodeHandles
import com.chknkv.coreauthentication.models.domain.handles.enterpasscode.EnterPasscodeHandles
import com.chknkv.coreauthentication.presentation.biometry.BiometryComponent
import com.chknkv.coreauthentication.presentation.biometry.BiometryComponentImpl
import com.chknkv.coreauthentication.presentation.createpasscode.CreatePasscodeComponent
import com.chknkv.coreauthentication.presentation.createpasscode.CreatePasscodeComponentImpl
import com.chknkv.coreauthentication.presentation.enterpasscode.EnterPasscodeComponent
import com.chknkv.coreauthentication.presentation.enterpasscode.EnterPasscodeComponentImpl
import com.chknkv.coreauthentication.domain.BiometricAuthenticator
import com.chknkv.coreauthentication.domain.PasscodeRepository

/**
 * Factory for creating authentication components.
 * Consumers pass [PasscodeRepository], [BiometricAuthenticator] (e.g. from Koin) and handles.
 */
object AuthenticationFactory {

    fun createPasscodeComponent(
        componentContext: ComponentContext,
        passcodeRepository: PasscodeRepository,
        biometricAuthenticator: BiometricAuthenticator,
        handles: CreatePasscodeHandles
    ): CreatePasscodeComponent = CreatePasscodeComponentImpl(
        componentContext = componentContext,
        passcodeRepository = passcodeRepository,
        biometricAuthenticator = biometricAuthenticator,
        handles = handles
    )

    fun createEnterPasscodeComponent(
        componentContext: ComponentContext,
        passcodeRepository: PasscodeRepository,
        biometricAuthenticator: BiometricAuthenticator,
        sessionRepository: com.chknkv.coresession.SessionRepository, // Only for clearAll() on forgot passcode
        handles: EnterPasscodeHandles
    ): EnterPasscodeComponent = EnterPasscodeComponentImpl(
        componentContext = componentContext,
        passcodeRepository = passcodeRepository,
        biometricAuthenticator = biometricAuthenticator,
        sessionRepository = sessionRepository,
        handles = handles
    )

    fun createBiometryComponent(
        componentContext: ComponentContext,
        passcodeRepository: PasscodeRepository,
        biometricAuthenticator: BiometricAuthenticator,
        handles: BiometryHandles
    ): BiometryComponent = BiometryComponentImpl(
        componentContext = componentContext,
        passcodeRepository = passcodeRepository,
        biometricAuthenticator = biometricAuthenticator,
        handles = handles
    )
}
