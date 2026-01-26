package com.chknkv.coreauthentication.presentation.enterpasscode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chknkv.coreauthentication.models.presentation.uiAction.enterpasscode.EnterPasscodeUiAction
import com.chknkv.coreauthentication.models.presentation.uiAction.enterpasscode.EnterPasscodeUiEffect
import com.chknkv.coreauthentication.presentation.keyboard.PasscodeKeyboard
import com.chknkv.coreauthentication.presentation.keyboard.PasscodeStatus
import com.chknkv.coreauthentication.utils.getBiometricContext
import com.chknkv.coredesignsystem.alertAction.AlertAction
import com.chknkv.coredesignsystem.alertAction.AlertActionData
import com.chknkv.coredesignsystem.buttons.AcButton
import com.chknkv.coredesignsystem.buttons.AcButtonStyle
import com.chknkv.coredesignsystem.snackbar.SnackBarCard
import com.chknkv.coredesignsystem.snackbar.SnackBarCardData
import com.chknkv.coredesignsystem.snackbar.showSnackBarCard
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Headline3
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import weightobserver_project.core.core_authentication.generated.resources.Res
import weightobserver_project.core.core_authentication.generated.resources.enter_passcode_alert_negative_action
import weightobserver_project.core.core_authentication.generated.resources.enter_passcode_alert_positive_action
import weightobserver_project.core.core_authentication.generated.resources.enter_passcode_alert_subtitle
import weightobserver_project.core.core_authentication.generated.resources.enter_passcode_alert_title
import weightobserver_project.core.core_authentication.generated.resources.enter_passcode_forget_passcode
import weightobserver_project.core.core_authentication.generated.resources.enter_passcode_incorrect_passcode
import weightobserver_project.core.core_authentication.generated.resources.enter_passcode_title

/**
 * UI screen for [EnterPasscodeComponent]. Enter passcode, optional biometric, and forgot-passcode flow.
 */
@Composable
fun EnterPasscodeScreen(component: EnterPasscodeComponent) {
    val uiResult by component.uiResult.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage = stringResource(Res.string.enter_passcode_incorrect_passcode)
    val biometricContext = getBiometricContext()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { component.initLoadEnterPasscodeScreen() }

    LaunchedEffect(biometricContext, uiResult.isBiometricAvailable) {
        if (biometricContext != null && uiResult.isBiometricAvailable) {
            delay(200)
            try {
                component.tryBiometricAuthentication(biometricContext)
            } catch (_: Exception) {
            }
        }
    }

    LaunchedEffect(Unit) {
        component.uiEffect.collect { effect ->
            when (effect) {
                EnterPasscodeUiEffect.InvalidPasscode -> {
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
                SnackBarCard(snackbarData = data, data = SnackBarCardData(closeIcon = true))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {

                Headline3(
                    text = stringResource(Res.string.enter_passcode_title),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, bottom = 8.dp)
                )

                PasscodeStatus(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    enteredDigitsSize = uiResult.enteredDigits.size
                )

                PasscodeKeyboard(
                    onNumberClick = { component.emitAction(EnterPasscodeUiAction.NumberClick(it)) },
                    onDeleteClick = { component.emitAction(EnterPasscodeUiAction.DeleteClick) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    onBiometricClick = if (uiResult.isBiometricAvailable && biometricContext != null) {
                        {
                            scope.launch {
                                try {
                                    component.tryBiometricAuthentication(biometricContext)
                                } catch (_: Exception) {
                                }
                            }
                        }
                    } else null,
                    enteredDigitsSize = uiResult.enteredDigits.size
                )
            }

            AcButton(
                text = stringResource(Res.string.enter_passcode_forget_passcode),
                style = AcButtonStyle.Transparent,
                onClick = { component.emitAction(EnterPasscodeUiAction.ShowForgotDialog) }
            )
        }
    }

    if (uiResult.isShowForgotDialog) {
        AlertAction(
            data = AlertActionData(
                titleAction = stringResource(Res.string.enter_passcode_alert_title),
                subtitleAction = stringResource(Res.string.enter_passcode_alert_subtitle),
                positiveActionText = stringResource(Res.string.enter_passcode_alert_negative_action),
                negativeActionText = stringResource(Res.string.enter_passcode_alert_positive_action)
            ),
            onDismissClick = { component.emitAction(EnterPasscodeUiAction.HideForgotDialog) },
            onPositiveActionClick = { component.emitAction(EnterPasscodeUiAction.HideForgotDialog) },
            onNegativeActionClick = { component.emitAction(EnterPasscodeUiAction.ForgotPasscode) }
        )
    }
}
