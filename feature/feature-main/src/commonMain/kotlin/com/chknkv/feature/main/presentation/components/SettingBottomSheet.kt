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
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Headline3
import com.chknkv.feature.main.model.presentation.MainAction
import com.chknkv.feature.main.model.presentation.SettingsUiResult
import org.jetbrains.compose.resources.stringResource
import weightobserver_project.feature.feature_main.generated.resources.Res
import weightobserver_project.feature.feature_main.generated.resources.settings_clear_data
import weightobserver_project.feature.feature_main.generated.resources.settings_clear_data_cancel
import weightobserver_project.feature.feature_main.generated.resources.settings_clear_data_confirm
import weightobserver_project.feature.feature_main.generated.resources.settings_clear_data_subtitle
import weightobserver_project.feature.feature_main.generated.resources.settings_clear_data_title
import weightobserver_project.feature.feature_main.generated.resources.settings_title

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
    onAction: (MainAction.SettingsAction) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    settingsUiResult: SettingsUiResult
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false,
            shouldDismissOnClickOutside = false
        ),
        containerColor = AcTokens.Background0.getThemedColor(),
        contentColor = AcTokens.Background0.getThemedColor(),
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

            AcButton(
                text = stringResource(Res.string.settings_clear_data),
                onClick = { onAction.invoke(MainAction.SettingsAction.ShowClearDataConfirmation) },
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
            onDismissClick = { onAction.invoke(MainAction.SettingsAction.HideClearDataConfirmation) },
            onPositiveActionClick = { onAction.invoke(MainAction.SettingsAction.HideClearDataConfirmation) },
            onNegativeActionClick = { onAction.invoke(MainAction.SettingsAction.SignOut) }
        )
    }
}
