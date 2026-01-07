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
internal fun AcButtonPreview() {
    AcTheme {
        Column(
            modifier = Modifier
                .background(AcTokens.Background0.getThemedColor())
                .padding(16.dp)

        ) {
            AcButton(
                text = "Standard Button",
                style = AcButtonStyle.Standard,
                onClick = {}
            )
            AcButton(
                text = "Negative Button",
                style = AcButtonStyle.Negative,
                onClick = {}
            )
            AcButton(
                text = "Transparent Button",
                style = AcButtonStyle.Transparent,
                onClick = {}
            )
            AcButton(
                text = "Transparent Negative",
                style = AcButtonStyle.TransparentNegative,
                onClick = {}
            )
            AcButton(
                text = "Disabled Button",
                style = AcButtonStyle.Standard,
                onClick = {},
                isEnabled = false
            )
        }
    }
}
