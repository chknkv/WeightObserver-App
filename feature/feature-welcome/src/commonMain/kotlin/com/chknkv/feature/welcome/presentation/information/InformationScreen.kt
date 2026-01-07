package com.chknkv.feature.welcome.presentation.information

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
 * UI screen for [InformationComponent].
 *
 * Shows the user useful information before starting work.
 *
 * @param component Component managing the logic of this screen.
 */
@Composable
fun InformationScreen(component: InformationComponent) {
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
            Title1(text = "InformationScreen")
        }

        AcButton(
            text = "Далее / Next",
            style = AcButtonStyle.Standard,
            onClick = { component.onNextClicked() }
        )
    }
}
