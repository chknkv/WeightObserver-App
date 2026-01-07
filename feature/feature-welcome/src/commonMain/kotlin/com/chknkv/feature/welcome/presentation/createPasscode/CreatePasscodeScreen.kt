package com.chknkv.feature.welcome.presentation.createPasscode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chknkv.coredesignsystem.buttons.AcButton
import com.chknkv.coredesignsystem.buttons.AcButtonStyle
import com.chknkv.coredesignsystem.typography.Title1

/**
 * UI screen for [CreatePasscodeComponent].
 *
 * @param component Component managing the logic of this screen.
 */
@Composable
fun CreatePasscodeScreen(component: CreatePasscodeComponent) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Title1(text = "CreatePasscodeScreen")
        }

        AcButton(
            text = "Пропустить",
            style = AcButtonStyle.Transparent,
            onClick = { component.onNextClicked() },
        )
    }
}
