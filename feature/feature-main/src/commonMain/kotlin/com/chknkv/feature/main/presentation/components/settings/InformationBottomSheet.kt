package com.chknkv.feature.main.presentation.components.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chknkv.coredesignsystem.buttons.AcButton
import com.chknkv.coredesignsystem.buttons.AcButtonStyle
import com.chknkv.coredesignsystem.cellBase.CellBase
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Headline3
import org.jetbrains.compose.resources.stringResource
import weightobserver_project.feature.feature_main.generated.resources.Res
import weightobserver_project.feature.feature_main.generated.resources.ic_hospital
import weightobserver_project.feature.feature_main.generated.resources.ic_info
import weightobserver_project.feature.feature_main.generated.resources.ic_lock
import weightobserver_project.feature.feature_main.generated.resources.ic_privacy
import weightobserver_project.feature.feature_main.generated.resources.ic_target
import weightobserver_project.feature.feature_main.generated.resources.information_accept
import weightobserver_project.feature.feature_main.generated.resources.information_about_subtitle
import weightobserver_project.feature.feature_main.generated.resources.information_about_title
import weightobserver_project.feature.feature_main.generated.resources.information_block_1_subtitle
import weightobserver_project.feature.feature_main.generated.resources.information_block_1_title
import weightobserver_project.feature.feature_main.generated.resources.information_block_2_subtitle
import weightobserver_project.feature.feature_main.generated.resources.information_block_2_title
import weightobserver_project.feature.feature_main.generated.resources.information_block_3_subtitle
import weightobserver_project.feature.feature_main.generated.resources.information_block_3_title
import weightobserver_project.feature.feature_main.generated.resources.information_block_4_subtitle
import weightobserver_project.feature.feature_main.generated.resources.information_block_4_title
import weightobserver_project.feature.feature_main.generated.resources.information_title

/**
 * Displays a bottom sheet with application information.
 *
 * @param onDismissRequest Callback invoked when the user dismisses the bottom sheet (e.g., by clicking outside or dragging down).
 * @param modifier Modifier to be applied to the bottom sheet layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) { sheetState.expand() }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier.fillMaxHeight(),
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false,
            shouldDismissOnClickOutside = false
        ),
        scrimColor = AcTokens.Transparent.getThemedColor(),
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                Headline3(
                    text = stringResource(Res.string.information_title),
                    modifier = Modifier.fillMaxWidth()
                )

                CellBase(
                    modifier = Modifier.padding(vertical = 8.dp),
                    iconRes = Res.drawable.ic_target,
                    title = stringResource(Res.string.information_about_title),
                    subtitle = stringResource(Res.string.information_about_subtitle),
                    maxSubtitle = 8,
                    iconTint = AcTokens.IconPrimary.getThemedColor()
                )

                CellBase(
                    modifier = Modifier.padding(vertical = 8.dp),
                    iconRes = Res.drawable.ic_hospital,
                    title = stringResource(Res.string.information_block_1_title),
                    subtitle = stringResource(Res.string.information_block_1_subtitle),
                    maxSubtitle = 8,
                    iconTint = AcTokens.IconPrimary.getThemedColor()
                )

                CellBase(
                    modifier = Modifier.padding(vertical = 8.dp),
                    iconRes = Res.drawable.ic_privacy,
                    title = stringResource(Res.string.information_block_2_title),
                    subtitle = stringResource(Res.string.information_block_2_subtitle),
                    maxSubtitle = 8,
                    iconTint = AcTokens.IconPrimary.getThemedColor()
                )

                CellBase(
                    modifier = Modifier.padding(vertical = 8.dp),
                    iconRes = Res.drawable.ic_lock,
                    title = stringResource(Res.string.information_block_3_title),
                    subtitle = stringResource(Res.string.information_block_3_subtitle),
                    maxSubtitle = 8,
                    iconTint = AcTokens.IconPrimary.getThemedColor()
                )

                CellBase(
                    modifier = Modifier.padding(vertical = 8.dp),
                    iconRes = Res.drawable.ic_info,
                    title = stringResource(Res.string.information_block_4_title),
                    subtitle = stringResource(Res.string.information_block_4_subtitle),
                    maxSubtitle = 8,
                    iconTint = AcTokens.IconPrimary.getThemedColor(),
                    isDivider = false
                )
            }

            AcButton(
                text = stringResource(Res.string.information_accept),
                style = AcButtonStyle.Standard,
                onClick = onDismissRequest,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
