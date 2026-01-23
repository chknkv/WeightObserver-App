package com.chknkv.feature.main.domain.resolvers

import com.chknkv.feature.main.domain.MainScreenInteractor
import com.chknkv.feature.main.model.presentation.MainUiResult
import com.chknkv.feature.main.model.presentation.uiResult.PasscodeSettingsUiResult
import com.chknkv.feature.main.model.presentation.uiAction.PasscodeSettingsAction
import com.chknkv.feature.main.model.presentation.uiResult.PasscodeSettingsUiEffect
import com.chknkv.feature.main.model.presentation.uiResult.PasscodeStep
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Resolver responsible for handling passcode configuration actions.
 *
 * This resolver manages the complex flow of creating and verifying passcodes, including:
 * - Handling digit input and deletion
 * - Managing the passcode creation steps (Enter, Create, Confirm)
 * - Handling "Skip" and "Forgot Passcode" alerts
 * - Validating entered passcodes against saved ones
 * - Handling passcode reset (which triggers sign out)
 *
 * @property interactor Interactor for saving and checking passcodes, and performing sign out.
 * @property onSignOutRequested Callback invoked when a passcode reset (sign out) is requested.
 */
class PasscodeSettingsActionResolver(
    private val interactor: MainScreenInteractor,
    private val onSignOutRequested: () -> Unit
) : ActionResolver<PasscodeSettingsAction> {

    override fun resolve(
        action: PasscodeSettingsAction,
        currentState: MainUiResult
    ): Flow<ResolverResult> = flow {
        when (action) {
            is PasscodeSettingsAction.DigitClick -> {
                val uiState = currentState.settingsUiResult.passcodeSettingsUiResult
                if (uiState.enteredDigits.size < 5) {
                    val newDigits = uiState.enteredDigits + action.digit
                    emit(ResolverResult.Mutation {
                        it.copy(
                            settingsUiResult = it.settingsUiResult.copy(
                                passcodeSettingsUiResult = uiState.copy(enteredDigits = newDigits, isError = false)
                            )
                        )
                    })

                    if (newDigits.size == 5) {
                        delay(200)
                        val passcode = newDigits.joinToString("")
                        processPasscodeFilled(passcode, uiState.step, uiState.savedPasscode).collect { emit(it) }
                    }
                }
            }

            is PasscodeSettingsAction.DeleteClick -> {
                val uiState = currentState.settingsUiResult.passcodeSettingsUiResult
                if (uiState.enteredDigits.isNotEmpty()) {
                    emit(ResolverResult.Mutation {
                        it.copy(
                            settingsUiResult = it.settingsUiResult.copy(
                                passcodeSettingsUiResult = uiState.copy(
                                    enteredDigits = uiState.enteredDigits.dropLast(1),
                                    isError = false
                                )
                            )
                        )
                    })
                }
            }

            is PasscodeSettingsAction.SkipCreate -> {
                interactor.savePasscode(null)
                emit(ResolverResult.Mutation {
                    it.copy(settingsUiResult = it.settingsUiResult.copy(isPasscodeSettingsVisible = false))
                })
            }

            is PasscodeSettingsAction.ShowSkipAlert -> {
                emit(ResolverResult.Mutation {
                    it.copy(
                        settingsUiResult = it.settingsUiResult.copy(
                            passcodeSettingsUiResult = it.settingsUiResult.passcodeSettingsUiResult.copy(isSkipAlertVisible = true)
                        )
                    )
                })
            }

            is PasscodeSettingsAction.HideSkipAlert -> {
                emit(ResolverResult.Mutation {
                    it.copy(
                        settingsUiResult = it.settingsUiResult.copy(
                            passcodeSettingsUiResult = it.settingsUiResult.passcodeSettingsUiResult.copy(isSkipAlertVisible = false)
                        )
                    )
                })
            }

            is PasscodeSettingsAction.ShowForgotAlert -> {
                emit(ResolverResult.Mutation {
                    it.copy(
                        settingsUiResult = it.settingsUiResult.copy(
                            passcodeSettingsUiResult = it.settingsUiResult.passcodeSettingsUiResult.copy(isForgotAlertVisible = true)
                        )
                    )
                })
            }

            is PasscodeSettingsAction.HideForgotAlert -> {
                emit(ResolverResult.Mutation {
                    it.copy(
                        settingsUiResult = it.settingsUiResult.copy(
                            passcodeSettingsUiResult = it.settingsUiResult.passcodeSettingsUiResult.copy(isForgotAlertVisible = false)
                        )
                    )
                })
            }

            is PasscodeSettingsAction.ResetPasscode -> {
                interactor.signOut()
                onSignOutRequested()
            }
        }
    }

    private fun processPasscodeFilled(
        passcode: String,
        step: PasscodeStep,
        savedPasscode: String?
    ): Flow<ResolverResult> = flow {
        when (step) {
            PasscodeStep.Enter -> {
                if (interactor.checkPasscode(passcode)) {
                    emit(ResolverResult.Mutation {
                        it.copy(
                            settingsUiResult = it.settingsUiResult.copy(
                                passcodeSettingsUiResult = PasscodeSettingsUiResult(step = PasscodeStep.Create)
                            )
                        )
                    })
                } else {
                    emit(ResolverResult.Effect(PasscodeSettingsUiEffect.InvalidPasscode))
                    emit(ResolverResult.Mutation {
                        it.copy(
                            settingsUiResult = it.settingsUiResult.copy(
                                passcodeSettingsUiResult = it.settingsUiResult.passcodeSettingsUiResult.copy(
                                    enteredDigits = emptyList(),
                                    isError = true
                                )
                            )
                        )
                    })
                }
            }

            PasscodeStep.Create -> {
                emit(ResolverResult.Mutation {
                    val uiState = it.settingsUiResult.passcodeSettingsUiResult
                    it.copy(
                        settingsUiResult = it.settingsUiResult.copy(
                            passcodeSettingsUiResult = uiState.copy(
                                step = PasscodeStep.Confirm,
                                savedPasscode = passcode,
                                enteredDigits = emptyList()
                            )
                        )
                    )
                })
            }

            PasscodeStep.Confirm -> {
                if (passcode == savedPasscode) {
                    interactor.savePasscode(passcode)
                    emit(ResolverResult.Effect(PasscodeSettingsUiEffect.PasscodeCreated))
                    emit(ResolverResult.Mutation {
                        it.copy(settingsUiResult = it.settingsUiResult.copy(isPasscodeSettingsVisible = false))
                    })
                } else {
                    emit(ResolverResult.Effect(PasscodeSettingsUiEffect.PasswordsDoNotMatch))
                    emit(ResolverResult.Mutation {
                        it.copy(
                            settingsUiResult = it.settingsUiResult.copy(
                                passcodeSettingsUiResult = PasscodeSettingsUiResult(step = PasscodeStep.Create, isError = true)
                            )
                        )
                    })
                }
            }

            PasscodeStep.Init -> Unit
        }
    }
}
