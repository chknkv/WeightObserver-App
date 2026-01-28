package com.chknkv.coreauthentication.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.chknkv.coreauthentication.AuthenticationFactory
import com.chknkv.coreauthentication.models.domain.handles.biometry.BiometryHandles
import com.chknkv.coreauthentication.models.domain.handles.createpasscode.CreatePasscodeHandles
import com.chknkv.coreauthentication.models.domain.handles.enterpasscode.EnterPasscodeHandles
import com.chknkv.coreauthentication.models.domain.handles.settings.PasscodeSettingsHandles
import com.chknkv.coreauthentication.presentation.biometry.BiometryScreen
import com.chknkv.coreauthentication.presentation.createpasscode.CreatePasscodeScreen
import com.chknkv.coreauthentication.presentation.enterpasscode.EnterPasscodeScreen
import com.chknkv.coreauthentication.domain.BiometricAuthenticator
import com.chknkv.coreauthentication.domain.PasscodeRepository
import com.chknkv.coresession.SessionRepository
import kotlinx.coroutines.launch

/**
 * Flow: Enter (verify current) → Create (new + confirm) → optional Biometry → close.
 * Embed in a bottom sheet. Pass [ComponentContext] from parent.
 */
@Composable
fun PasscodeSettingsFlow(
    componentContext: ComponentContext,
    passcodeRepository: PasscodeRepository,
    biometricAuthenticator: BiometricAuthenticator,
    sessionRepository: SessionRepository,
    handles: PasscodeSettingsHandles,
    initialEnter: Boolean
) {
    val step = remember { mutableStateOf(if (initialEnter) Step.Enter else Step.Create) }
    val scope = rememberCoroutineScope()

    when (step.value) {
        Step.Enter -> key(Step.Enter) {
            val component = remember(componentContext, passcodeRepository, biometricAuthenticator, sessionRepository, step.value) {
                AuthenticationFactory.createEnterPasscodeComponent(
                    componentContext = componentContext,
                    passcodeRepository = passcodeRepository,
                    biometricAuthenticator = biometricAuthenticator,
                    sessionRepository = sessionRepository,
                    handles = EnterPasscodeHandles(
                        onAuthSuccess = { step.value = Step.Create },
                        onForgotPasscode = {
                            scope.launch {
                                sessionRepository.clearAll()
                                handles.onClose()
                            }
                        }
                    )
                )
            }
            EnterPasscodeScreen(component = component)
        }

        Step.Create -> key(Step.Create) {
            val component = remember(componentContext, passcodeRepository, biometricAuthenticator, step.value) {
                AuthenticationFactory.createPasscodeComponent(
                    componentContext = componentContext,
                    passcodeRepository = passcodeRepository,
                    biometricAuthenticator = biometricAuthenticator,
                    handles = CreatePasscodeHandles(
                        onPasscodeCreated = { showBiometry ->
                            if (showBiometry) step.value = Step.Biometry
                            else {
                                handles.onPasscodeCreated()
                                handles.onClose()
                            }
                        },
                        onPasscodeSkipped = { handles.onClose() },
                        onBack = { handles.onClose() }
                    )
                )
            }
            CreatePasscodeScreen(component = component)
        }

        Step.Biometry -> key(Step.Biometry) {
            val component = remember(componentContext, passcodeRepository, biometricAuthenticator, step.value) {
                AuthenticationFactory.createBiometryComponent(
                    componentContext = componentContext,
                    passcodeRepository = passcodeRepository,
                    biometricAuthenticator = biometricAuthenticator,
                    handles = BiometryHandles(
                        onBiometricEnabled = {
                            handles.onPasscodeCreated()
                            handles.onClose()
                        },
                        onBiometricSkipped = {
                            handles.onPasscodeCreated()
                            handles.onClose()
                        }
                    )
                )
            }

            BiometryScreen(component = component)
        }
    }
}

private enum class Step { Enter, Create, Biometry }
