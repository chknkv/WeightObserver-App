package com.chknkv.coredesignsystem.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chknkv.coredesignsystem.theming.AcTheme
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun DualAcButtonVerticalPreview() {
    AcTheme {
        Column(modifier = Modifier.background(AcTokens.Background0.getThemedColor()).padding(16.dp)) {
            DualAcButtonVertical(
                firstButtonText = "First Action",
                secondButtonText = "Second Action",
                firstButtonClick = {},
                secondButtonClick = {}
            )
        }
    }
}

@Preview
@Composable
internal fun DualBlueberryHorizontalPreview() {
    AcTheme {
        Column(modifier = Modifier.background(AcTokens.Background0.getThemedColor()).padding(16.dp)) {
            DualBlueberryHorizontal(
                firstButtonText = "Left",
                secondButtonText = "Right",
                firstButtonClick = {},
                secondButtonClick = {}
            )
        }
    }
}
