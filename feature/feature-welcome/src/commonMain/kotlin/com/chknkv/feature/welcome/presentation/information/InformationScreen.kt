package com.chknkv.feature.welcome.presentation.information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chknkv.coredesignsystem.buttons.AcButton
import com.chknkv.coredesignsystem.buttons.AcButtonStyle
import com.chknkv.coredesignsystem.cellBase.CellBase
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Footnote1Secondary
import com.chknkv.coredesignsystem.typography.Headline3
import org.jetbrains.compose.resources.stringResource
import weightobserver_project.feature.feature_welcome.generated.resources.Res
import weightobserver_project.feature.feature_welcome.generated.resources.ic_info
import weightobserver_project.feature.feature_welcome.generated.resources.ic_hospital
import weightobserver_project.feature.feature_welcome.generated.resources.ic_lock
import weightobserver_project.feature.feature_welcome.generated.resources.ic_privacy
import weightobserver_project.feature.feature_welcome.generated.resources.information_title
import weightobserver_project.feature.feature_welcome.generated.resources.information_accept
import weightobserver_project.feature.feature_welcome.generated.resources.information_accept_description
import weightobserver_project.feature.feature_welcome.generated.resources.information_block_1_title
import weightobserver_project.feature.feature_welcome.generated.resources.information_block_1_subtile
import weightobserver_project.feature.feature_welcome.generated.resources.information_block_2_title
import weightobserver_project.feature.feature_welcome.generated.resources.information_block_2_subtile
import weightobserver_project.feature.feature_welcome.generated.resources.information_block_3_title
import weightobserver_project.feature.feature_welcome.generated.resources.information_block_3_subtile
import weightobserver_project.feature.feature_welcome.generated.resources.information_block_4_title
import weightobserver_project.feature.feature_welcome.generated.resources.information_block_4_subtile

/**
 * UI screen for [InformationComponent].
 *
 * Shows the user useful information before starting work.
 *
 * @param component Component managing the logic of this screen.
 */
@Composable
fun InformationScreen(component: InformationComponent) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(AcTokens.Background0.getThemedColor())
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            .imePadding(),
        contentColor = AcTokens.Background0.getThemedColor(),
        containerColor = AcTokens.Background0.getThemedColor()
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Headline3(
                    text = stringResource(Res.string.information_title),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, bottom = 8.dp)
                )

                CellBase(
                    modifier = Modifier.padding(vertical = 8.dp),
                    iconRes = Res.drawable.ic_hospital,
                    title = stringResource(Res.string.information_block_1_title),
                    subtitle = stringResource(Res.string.information_block_1_subtile),
                    maxSubtitle = 8,
                    iconTint = AcTokens.IconPrimary.getThemedColor()
                )

                CellBase(
                    modifier = Modifier.padding(vertical = 8.dp),
                    iconRes = Res.drawable.ic_privacy,
                    title = stringResource(Res.string.information_block_2_title),
                    subtitle = stringResource(Res.string.information_block_2_subtile),
                    maxSubtitle = 8,
                    iconTint = AcTokens.IconPrimary.getThemedColor()
                )

                CellBase(
                    modifier = Modifier.padding(vertical = 8.dp),
                    iconRes = Res.drawable.ic_lock,
                    title = stringResource(Res.string.information_block_3_title),
                    subtitle = stringResource(Res.string.information_block_3_subtile),
                    maxSubtitle = 8,
                    iconTint = AcTokens.IconPrimary.getThemedColor()
                )

                CellBase(
                    modifier = Modifier.padding(vertical = 8.dp),
                    iconRes = Res.drawable.ic_info,
                    title = stringResource(Res.string.information_block_4_title),
                    subtitle = stringResource(Res.string.information_block_4_subtile),
                    maxSubtitle = 8,
                    iconTint = AcTokens.IconPrimary.getThemedColor(),
                    isDivider = false
                )
            }

            Footnote1Secondary(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.information_accept_description),
                textAlign = TextAlign.Start
            )

            AcButton(
                text = stringResource(Res.string.information_accept),
                style = AcButtonStyle.Standard,
                onClick = { component.onNextClicked() },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
