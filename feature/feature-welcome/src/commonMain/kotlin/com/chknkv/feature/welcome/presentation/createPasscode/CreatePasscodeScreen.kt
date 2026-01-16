package com.chknkv.feature.welcome.presentation.createPasscode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.chknkv.feature.welcome.model.presentation.createPasscode.CreatePasscodeUiAction
import com.chknkv.feature.welcome.model.presentation.createPasscode.CreatePasscodeUiEffect
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import weightobserver_project.feature.feature_welcome.generated.resources.Res
import weightobserver_project.feature.feature_welcome.generated.resources.create_passcode_alert_negative_action
import weightobserver_project.feature.feature_welcome.generated.resources.create_passcode_alert_positive_action
import weightobserver_project.feature.feature_welcome.generated.resources.create_passcode_alert_subtitle
import weightobserver_project.feature.feature_welcome.generated.resources.create_passcode_alert_title
import weightobserver_project.feature.feature_welcome.generated.resources.create_passcode_skip
import weightobserver_project.feature.feature_welcome.generated.resources.create_passcode_snackbar_password_not_matched
import weightobserver_project.feature.feature_welcome.generated.resources.create_passcode_subtitle
import weightobserver_project.feature.feature_welcome.generated.resources.create_passcode_title_1
import weightobserver_project.feature.feature_welcome.generated.resources.create_passcode_title_2

/**
 * UI screen for [CreatePasscodeComponent].
 *
 * @param component Component managing the logic of this screen.
 */
@Composable
fun CreatePasscodeScreen(component: CreatePasscodeComponent) {
    val uiResult by component.uiResult.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage = stringResource(Res.string.create_passcode_snackbar_password_not_matched)

    LaunchedEffect(Unit) { component.initLoadCreatePasscodeScreen() }

    LaunchedEffect(Unit) {
        component.uiEffect.collect { effect ->
            when (effect) {
                is CreatePasscodeUiEffect.PasswordsDoNotMatch -> {
                    launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackBarCard(snackbarMessage)
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(AcTokens.Background0.getThemedColor())
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            .imePadding(),
        contentColor = AcTokens.Background0.getThemedColor(),
        containerColor = AcTokens.Background0.getThemedColor(),
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                SnackBarCard(
                    snackbarData = data,
                    data = SnackBarCardData(
                        closeIcon = true
                    )
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            )

            Headline3(
                text = if (uiResult.isConfirming) stringResource(Res.string.create_passcode_title_2)
                else stringResource(Res.string.create_passcode_title_1),
                modifier = Modifier.fillMaxWidth()
            )

            Body3Secondary(
                text = stringResource(Res.string.create_passcode_subtitle),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            PasscodeStatus(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                enteredDigitsSize = uiResult.enteredDigits.size,
            )

            PasscodeKeyboard(
                onNumberClick = { component.emitAction(CreatePasscodeUiAction.NumberClick(it)) },
                onDeleteClick = { component.emitAction(CreatePasscodeUiAction.DeleteClick) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            AcButton(
                text = stringResource(Res.string.create_passcode_skip),
                style = AcButtonStyle.Transparent,
                onClick = { component.emitAction(CreatePasscodeUiAction.ShowAlert) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (uiResult.isAlertEnabled) {
        AlertAction(
            data = AlertActionData(
                titleAction = stringResource(Res.string.create_passcode_alert_title),
                subtitleAction = stringResource(Res.string.create_passcode_alert_subtitle),
                positiveActionText = stringResource(Res.string.create_passcode_alert_positive_action),
                negativeActionText = stringResource(Res.string.create_passcode_alert_negative_action)
            ),
            onDismissClick = { component.emitAction(CreatePasscodeUiAction.DismissAlert) },
            onPositiveActionClick = { component.emitAction(CreatePasscodeUiAction.DismissAlert) },
            onNegativeActionClick = { component.emitAction(CreatePasscodeUiAction.Skip) }
        )
    }
}