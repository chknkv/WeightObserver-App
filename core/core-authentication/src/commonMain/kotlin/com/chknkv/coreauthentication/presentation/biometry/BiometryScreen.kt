package com.chknkv.coreauthentication.presentation.biometry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chknkv.coreauthentication.utils.getBiometricContext
import com.chknkv.coredesignsystem.buttons.AcButtonStyle
import com.chknkv.coredesignsystem.buttons.DualAcButtonVertical
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Body3Secondary
import com.chknkv.coredesignsystem.typography.Headline3
import org.jetbrains.compose.resources.stringResource
import weightobserver_project.core.core_authentication.generated.resources.Res
import weightobserver_project.core.core_authentication.generated.resources.biometry_enable
import weightobserver_project.core.core_authentication.generated.resources.biometry_skip
import weightobserver_project.core.core_authentication.generated.resources.biometry_subtitle
import weightobserver_project.core.core_authentication.generated.resources.biometry_title

/**
 * UI screen for [BiometryComponent]. Enable or skip biometrics via [DualAcButtonVertical].
 */
@Composable
fun BiometryScreen(component: BiometryComponent) {
    val biometricContext = getBiometricContext()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(AcTokens.Background0.getThemedColor())
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        contentColor = AcTokens.Background0.getThemedColor(),
        containerColor = AcTokens.Background0.getThemedColor()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Headline3(
                    text = stringResource(Res.string.biometry_title),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, bottom = 8.dp)
                )

                Body3Secondary(
                    text = stringResource(Res.string.biometry_subtitle),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }

            DualAcButtonVertical(
                modifier = Modifier.fillMaxWidth(),
                firstButtonText = stringResource(Res.string.biometry_enable),
                secondButtonText = stringResource(Res.string.biometry_skip),
                firstButtonClick = { biometricContext?.let { component.enableBiometrics(it) } },
                secondButtonClick = { component.skipBiometrics() },
                firstButtonStyle = AcButtonStyle.Standard,
                secondButtonStyle = AcButtonStyle.Transparent
            )
        }
    }
}
