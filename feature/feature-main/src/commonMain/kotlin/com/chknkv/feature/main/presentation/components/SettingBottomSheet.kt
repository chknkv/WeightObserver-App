package com.chknkv.feature.main.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chknkv.coredesignsystem.alertAction.AlertAction
import com.chknkv.coredesignsystem.alertAction.AlertActionData
import com.chknkv.coredesignsystem.buttons.AcButton
import com.chknkv.coredesignsystem.buttons.AcButtonStyle
import com.chknkv.coredesignsystem.cellBase.CellBase
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Headline3
import com.chknkv.coreutils.openUrl
import com.chknkv.feature.main.model.presentation.MainAction
import com.chknkv.feature.main.model.presentation.uiResult.SettingsUiResult
import com.chknkv.feature.main.model.presentation.uiAction.SettingsAction
import com.arkivanov.decompose.ComponentContext
import com.chknkv.feature.main.presentation.components.settings.InformationBottomSheet
import com.chknkv.feature.main.presentation.components.settings.LanguageBottomSheet
import com.chknkv.feature.main.presentation.components.settings.PasscodeSettingsBottomSheet
import org.jetbrains.compose.resources.stringResource
import weightobserver_project.feature.feature_main.generated.resources.Res
import weightobserver_project.feature.feature_main.generated.resources.ic_code
import weightobserver_project.feature.feature_main.generated.resources.settings_clear_data
import weightobserver_project.feature.feature_main.generated.resources.settings_clear_data_cancel
import weightobserver_project.feature.feature_main.generated.resources.settings_clear_data_confirm
import weightobserver_project.feature.feature_main.generated.resources.settings_clear_data_subtitle
import weightobserver_project.feature.feature_main.generated.resources.settings_clear_data_title
import weightobserver_project.feature.feature_main.generated.resources.settings_title
import weightobserver_project.feature.feature_main.generated.resources.ic_info
import weightobserver_project.feature.feature_main.generated.resources.ic_language
import weightobserver_project.feature.feature_main.generated.resources.ic_passcode
import weightobserver_project.feature.feature_main.generated.resources.settings_info_title
import weightobserver_project.feature.feature_main.generated.resources.settings_info_subtitle
import weightobserver_project.feature.feature_main.generated.resources.settings_passcode_title
import weightobserver_project.feature.feature_main.generated.resources.settings_passcode_subtitle
import weightobserver_project.feature.feature_main.generated.resources.settings_language_title
import weightobserver_project.feature.feature_main.generated.resources.settings_language_subtitle
import weightobserver_project.feature.feature_main.generated.resources.settings_opensource_title
import weightobserver_project.feature.feature_main.generated.resources.settings_opensource_subtitle

/**
 * Displays a bottom sheet with application settings.
 *
 * @param onAction Callback invoked user actions on the Main Screen.
 * @param onDismissRequest Callback invoked when the user dismisses the bottom sheet (e.g., by clicking outside or dragging down).
 * @param modifier Modifier to be applied to the bottom sheet layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingBottomSheet(
    onAction: (MainAction) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    componentContext: ComponentContext,
    settingsUiResult: SettingsUiResult
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false,
            shouldDismissOnClickOutside = false
        ),
        scrimColor = AcTokens.BottomSheetScrim.getThemedColor(),
        containerColor = AcTokens.Background1.getThemedColor(),
        contentColor = AcTokens.Background1.getThemedColor(),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = AcTokens.BottomSheetHook.getThemedColor()
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Headline3(
                text = stringResource(Res.string.settings_title),
                modifier = Modifier.fillMaxWidth()
            )

            CellBase(
                modifier = Modifier.padding(top = 12.dp),
                iconRes = Res.drawable.ic_info,
                title = stringResource(Res.string.settings_info_title),
                subtitle = stringResource(Res.string.settings_info_subtitle),
                maxSubtitle = 3,
                iconTint = AcTokens.IconPrimary.getThemedColor(),
                onClick = { onAction(SettingsAction.ShowInformation) }
            )

            CellBase(
                modifier = Modifier.padding(vertical = 8.dp),
                iconRes = Res.drawable.ic_code,
                title = stringResource(Res.string.settings_opensource_title),
                subtitle = stringResource(Res.string.settings_opensource_subtitle),
                maxSubtitle = 3,
                iconTint = AcTokens.IconPrimary.getThemedColor(),
                onClick = { openUrl("https://github.com/chknkv/WeightObserver-App") }
            )

            CellBase(
                modifier = Modifier.padding(top = 8.dp),
                iconRes = Res.drawable.ic_passcode,
                title = stringResource(Res.string.settings_passcode_title),
                subtitle = stringResource(Res.string.settings_passcode_subtitle),
                maxSubtitle = 3,
                iconTint = AcTokens.IconPrimary.getThemedColor(),
                onClick = { onAction(SettingsAction.ShowPasscodeSettings) }
            )

            CellBase(
                modifier = Modifier.padding(top = 8.dp),
                iconRes = Res.drawable.ic_language,
                title = stringResource(Res.string.settings_language_title),
                subtitle = stringResource(Res.string.settings_language_subtitle),
                maxSubtitle = 3,
                iconTint = AcTokens.IconPrimary.getThemedColor(),
                onClick = { onAction(SettingsAction.ShowLanguageSelection) },
                isDivider = false
            )

            AcButton(
                text = stringResource(Res.string.settings_clear_data),
                onClick = { onAction.invoke(SettingsAction.ShowClearDataConfirmation) },
                style = AcButtonStyle.TransparentNegative
            )
        }
    }

    if (settingsUiResult.isClearDataConfirmationVisible) {
        AlertAction(
            data = AlertActionData(
                titleAction = stringResource(Res.string.settings_clear_data_title),
                subtitleAction = stringResource(Res.string.settings_clear_data_subtitle),
                positiveActionText = stringResource(Res.string.settings_clear_data_cancel),
                negativeActionText = stringResource(Res.string.settings_clear_data_confirm)
            ),
            onDismissClick = { onAction.invoke(SettingsAction.HideClearDataConfirmation) },
            onPositiveActionClick = { onAction.invoke(SettingsAction.HideClearDataConfirmation) },
            onNegativeActionClick = { onAction.invoke(SettingsAction.SignOut) }
        )
    }

    if (settingsUiResult.isInformationVisible) {
        InformationBottomSheet(
            onDismissRequest = { onAction.invoke(SettingsAction.HideInformation) }
        )
    }

    if (settingsUiResult.isLanguageSelectionVisible) {
        LanguageBottomSheet(
            onDismissRequest = { onAction.invoke(SettingsAction.HideLanguageSelection) }
        )
    }

    if (settingsUiResult.isPasscodeSettingsVisible) {
        PasscodeSettingsBottomSheet(
            componentContext = componentContext,
            onDismissRequest = { onAction(SettingsAction.HidePasscodeSettings) }
        )
    }
}
