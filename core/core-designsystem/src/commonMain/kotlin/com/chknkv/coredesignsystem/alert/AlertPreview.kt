package com.chknkv.coredesignsystem.alert

import androidx.compose.runtime.Composable
import com.chknkv.coredesignsystem.theming.AcTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun AlertPreview() {
    AcTheme {
        Alert(
            data = AlertData(
                title = "Alert Title",
                subtitle = "Alert Subtitle",
                firstButtonText = "OK"
            ),
            onDismissClick = {},
            onFirstButtonClick = {},
            onSecondButtonClick = null
        )
    }
}

@Preview
@Composable
internal fun AlertWithTwoButtonsPreview() {
    AcTheme {
        Alert(
            data = AlertData(
                title = "Alert Title",
                subtitle = "Alert Subtitle",
                firstButtonText = "OK",
                secondButtonText = "Cancel"
            ),
            onDismissClick = {},
            onFirstButtonClick = {},
            onSecondButtonClick = {}
        )
    }
}
