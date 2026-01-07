package com.chknkv.feature.welcome.presentation.selectLanguage

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
 * UI screen for [SelectLanguageComponent].
 *
 * Provides an interface for language selection.
 *
 * @param component Component managing the logic of this screen.
 */
@Composable
fun SelectLanguageScreen(component: SelectLanguageComponent) {
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
            Title1(text = "SelectLanguageScreen")
        }

        AcButton(
            text = "Далее / Next",
            style = AcButtonStyle.Standard,
            onClick = { component.onNextClicked() }
        )
    }
}
