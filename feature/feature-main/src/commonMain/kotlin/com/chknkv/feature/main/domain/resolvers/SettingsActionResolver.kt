package com.chknkv.feature.main.domain.resolvers

import com.chknkv.feature.main.domain.MainScreenInteractor
import com.chknkv.feature.main.model.presentation.MainUiResult
import com.chknkv.feature.main.model.presentation.uiResult.PasscodeSettingsUiResult
import com.chknkv.feature.main.model.presentation.uiAction.SettingsAction
import com.chknkv.feature.main.model.presentation.uiResult.PasscodeStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Resolver responsible for handling settings-related actions.
 *
 * This resolver manages state transitions for various settings features including:
 * - Toggling settings visibility
 * - Managing "Clear Data" confirmation dialogs
 * - Handling sign-out requests
 * - Controlling visibility of information and language selection screens
 * - Managing the passcode settings flow (setup, visibility)
 *
 * @property interactor Interactor for performing business logic operations (e.g. signing out, checking passcode status).
 * @property onSignOutRequested Callback invoked when the user successfully confirms sign out.
 */
class SettingsActionResolver(
    private val interactor: MainScreenInteractor,
    private val onSignOutRequested: () -> Unit
) : ActionResolver<SettingsAction> {

    override fun resolve(
        action: SettingsAction,
        currentState: MainUiResult
    ): Flow<ResolverResult> = flow {
        when (action) {
            is SettingsAction.HideSettings -> {
                emit(ResolverResult.Mutation { it.copy(isSettingVisible = false) })
            }

            is SettingsAction.ShowClearDataConfirmation -> {
                emit(ResolverResult.Mutation {
                    it.copy(settingsUiResult = it.settingsUiResult.copy(isClearDataConfirmationVisible = true))
                })
            }

            is SettingsAction.HideClearDataConfirmation -> {
                emit(ResolverResult.Mutation {
                    it.copy(settingsUiResult = it.settingsUiResult.copy(isClearDataConfirmationVisible = false))
                })
            }

            is SettingsAction.SignOut -> {
                emit(ResolverResult.Mutation {
                    it.copy(
                        settingsUiResult = it.settingsUiResult.copy(isClearDataConfirmationVisible = false),
                        isSettingVisible = false
                    )
                })
                interactor.signOut()
                onSignOutRequested()
            }

            is SettingsAction.ShowInformation -> {
                emit(ResolverResult.Mutation {
                    it.copy(settingsUiResult = it.settingsUiResult.copy(isInformationVisible = true))
                })
            }

            is SettingsAction.HideInformation -> {
                emit(ResolverResult.Mutation {
                    it.copy(settingsUiResult = it.settingsUiResult.copy(isInformationVisible = false))
                })
            }

            is SettingsAction.ShowLanguageSelection -> {
                emit(ResolverResult.Mutation {
                    it.copy(settingsUiResult = it.settingsUiResult.copy(isLanguageSelectionVisible = true))
                })
            }

            is SettingsAction.HideLanguageSelection -> {
                emit(ResolverResult.Mutation {
                    it.copy(settingsUiResult = it.settingsUiResult.copy(isLanguageSelectionVisible = false))
                })
            }

            is SettingsAction.ShowPasscodeSettings -> {
                val hasPasscode = interactor.hasPasscode()
                val initialStep = if (hasPasscode) PasscodeStep.Enter else PasscodeStep.Create
                emit(ResolverResult.Mutation {
                    it.copy(
                        settingsUiResult = it.settingsUiResult.copy(
                            isPasscodeSettingsVisible = true,
                            passcodeSettingsUiResult = PasscodeSettingsUiResult(step = initialStep)
                        )
                    )
                })
            }

            is SettingsAction.HidePasscodeSettings -> {
                emit(ResolverResult.Mutation {
                    it.copy(settingsUiResult = it.settingsUiResult.copy(isPasscodeSettingsVisible = false))
                })
            }
        }
    }
}
