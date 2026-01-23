package com.chknkv.feature.main.presentation.components.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chknkv.coredesignsystem.alertAction.AlertAction
import com.chknkv.coredesignsystem.alertAction.AlertActionData
import com.chknkv.coredesignsystem.buttons.AcButton
import com.chknkv.coredesignsystem.buttons.AcButtonStyle
import com.chknkv.coredesignsystem.passcode.PasscodeKeyboard
import com.chknkv.coredesignsystem.passcode.PasscodeStatus
import com.chknkv.coredesignsystem.snackbar.SnackBarCard
import com.chknkv.coredesignsystem.snackbar.SnackBarCardData
import com.chknkv.coredesignsystem.snackbar.showSnackBarCard
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Body3Secondary
import com.chknkv.coredesignsystem.typography.Headline3
import com.chknkv.feature.main.model.presentation.uiAction.PasscodeSettingsAction
import com.chknkv.feature.main.model.presentation.uiResult.PasscodeSettingsUiEffect
import com.chknkv.feature.main.model.presentation.uiResult.PasscodeSettingsUiResult
import com.chknkv.feature.main.model.presentation.uiResult.PasscodeStep
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import weightobserver_project.feature.feature_main.generated.resources.Res
import weightobserver_project.feature.feature_main.generated.resources.create_passcode_alert_negative_action
import weightobserver_project.feature.feature_main.generated.resources.create_passcode_alert_positive_action
import weightobserver_project.feature.feature_main.generated.resources.create_passcode_alert_subtitle
import weightobserver_project.feature.feature_main.generated.resources.create_passcode_alert_title
import weightobserver_project.feature.feature_main.generated.resources.create_passcode_skip
import weightobserver_project.feature.feature_main.generated.resources.create_passcode_subtitle
import weightobserver_project.feature.feature_main.generated.resources.create_passcode_snackbar_password_not_matched
import weightobserver_project.feature.feature_main.generated.resources.create_passcode_title_1
import weightobserver_project.feature.feature_main.generated.resources.create_passcode_title_2
import weightobserver_project.feature.feature_main.generated.resources.enter_passcode_alert_negative_action
import weightobserver_project.feature.feature_main.generated.resources.enter_passcode_alert_positive_action
import weightobserver_project.feature.feature_main.generated.resources.enter_passcode_alert_subtitle
import weightobserver_project.feature.feature_main.generated.resources.enter_passcode_alert_title
import weightobserver_project.feature.feature_main.generated.resources.enter_passcode_forget_passcode
import weightobserver_project.feature.feature_main.generated.resources.enter_passcode_incorrect_passcode
import weightobserver_project.feature.feature_main.generated.resources.enter_passcode_title
import weightobserver_project.feature.feature_main.generated.resources.passcode_created_success

/**
 * A bottom sheet composable for handling passcode settings.
 *
 * This component manages the flow for entering an existing passcode, creating a new passcode,
 * and confirming the new passcode. It handles user interactions, displays appropriate alerts
 * (skip creation, forgot passcode), and responds to UI effects (e.g., invalid passcode).
 *
 * @param uiResult The current UI state of the passcode settings.
 * @param effects A generic stream of side effects (e.g., [PasscodeSettingsUiEffect]).
 * @param onAction Callback to handle user actions (e.g., [PasscodeSettingsAction]).
 * @param onDismissRequest Callback invoked when the bottom sheet is dismissed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasscodeSettingsBottomSheet(
    uiResult: PasscodeSettingsUiResult,
    effects: SharedFlow<PasscodeSettingsUiEffect>,
    onAction: (PasscodeSettingsAction) -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }

    val incorrectPasscodeMessage = stringResource(Res.string.enter_passcode_incorrect_passcode)
    val passwordsNotMatchedMessage = stringResource(Res.string.create_passcode_snackbar_password_not_matched)
    val passcodeCreatedMessage = stringResource(Res.string.passcode_created_success)

    LaunchedEffect(Unit) {
        effects.collect { effect ->
            launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                when (effect) {
                    PasscodeSettingsUiEffect.InvalidPasscode -> {
                        snackbarHostState.showSnackBarCard(incorrectPasscodeMessage)
                    }
                    PasscodeSettingsUiEffect.PasswordsDoNotMatch -> {
                        snackbarHostState.showSnackBarCard(passwordsNotMatchedMessage)
                    }
                    PasscodeSettingsUiEffect.PasscodeCreated -> {
                        snackbarHostState.showSnackBarCard(passcodeCreatedMessage)
                    }
                }
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle(color = AcTokens.BottomSheetHook.getThemedColor()) },
        containerColor = AcTokens.Background0.getThemedColor(),
        contentColor = AcTokens.Background0.getThemedColor(),
        modifier = Modifier.fillMaxHeight()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                when (uiResult.step) {
                    PasscodeStep.Enter -> EnterPasscodeContent(uiResult, onAction)
                    PasscodeStep.Create -> CreatePasscodeContent(uiResult, onAction, false)
                    PasscodeStep.Confirm -> CreatePasscodeContent(uiResult, onAction, true)
                    PasscodeStep.Init -> Unit
                }
            }

            SnackbarHost(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                hostState = snackbarHostState
            ) { data ->
                SnackBarCard(
                    snackbarData = data,
                    data = SnackBarCardData(closeIcon = true)
                )
            }
        }
    }

    if (uiResult.isSkipAlertVisible) {
        AlertAction(
            data = AlertActionData(
                titleAction = stringResource(Res.string.create_passcode_alert_title),
                subtitleAction = stringResource(Res.string.create_passcode_alert_subtitle),
                positiveActionText = stringResource(Res.string.create_passcode_alert_positive_action),
                negativeActionText = stringResource(Res.string.create_passcode_alert_negative_action)
            ),
            onDismissClick = { onAction(PasscodeSettingsAction.HideSkipAlert) },
            onPositiveActionClick = { onAction(PasscodeSettingsAction.HideSkipAlert) },
            onNegativeActionClick = { onAction(PasscodeSettingsAction.SkipCreate) }
        )
    }

    if (uiResult.isForgotAlertVisible) {
        AlertAction(
            data = AlertActionData(
                titleAction = stringResource(Res.string.enter_passcode_alert_title),
                subtitleAction = stringResource(Res.string.enter_passcode_alert_subtitle),
                positiveActionText = stringResource(Res.string.enter_passcode_alert_negative_action),
                negativeActionText = stringResource(Res.string.enter_passcode_alert_positive_action)
            ),
            onDismissClick = { onAction(PasscodeSettingsAction.HideForgotAlert) },
            onPositiveActionClick = { onAction(PasscodeSettingsAction.HideForgotAlert) },
            onNegativeActionClick = { onAction(PasscodeSettingsAction.ResetPasscode) }
        )
    }
}

@Composable
private fun ColumnScope.EnterPasscodeContent(
    uiResult: PasscodeSettingsUiResult,
    onAction: (PasscodeSettingsAction) -> Unit
) {
    Headline3(
        text = stringResource(Res.string.enter_passcode_title),
        modifier = Modifier.fillMaxWidth()
    )

    PasscodeStatus(
        modifier = Modifier.fillMaxWidth().weight(1f),
        enteredDigitsSize = uiResult.enteredDigits.size
    )

    PasscodeKeyboard(
        onNumberClick = { onAction(PasscodeSettingsAction.DigitClick(it)) },
        onDeleteClick = { onAction(PasscodeSettingsAction.DeleteClick) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
    )

    AcButton(
        text = stringResource(Res.string.enter_passcode_forget_passcode),
        style = AcButtonStyle.Transparent,
        onClick = { onAction(PasscodeSettingsAction.ShowForgotAlert) },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun ColumnScope.CreatePasscodeContent(
    uiResult: PasscodeSettingsUiResult,
    onAction: (PasscodeSettingsAction) -> Unit,
    isConfirm: Boolean
) {
    Headline3(
        text = if (isConfirm) stringResource(Res.string.create_passcode_title_2)
        else stringResource(Res.string.create_passcode_title_1),
        modifier = Modifier.fillMaxWidth()
    )

    Body3Secondary(
        text = stringResource(Res.string.create_passcode_subtitle),
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    )

    PasscodeStatus(
        modifier = Modifier.fillMaxWidth().weight(1f),
        enteredDigitsSize = uiResult.enteredDigits.size
    )

    PasscodeKeyboard(
        onNumberClick = { onAction(PasscodeSettingsAction.DigitClick(it)) },
        onDeleteClick = { onAction(PasscodeSettingsAction.DeleteClick) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
    )

    AcButton(
        text = stringResource(Res.string.create_passcode_skip),
        style = AcButtonStyle.Transparent,
        onClick = { onAction(PasscodeSettingsAction.ShowSkipAlert) },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))
}
