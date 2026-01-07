package com.chknkv.coredesignsystem.alertAction

import androidx.compose.runtime.Composable
import com.chknkv.coredesignsystem.theming.AcTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun AlertActionPreview() {
    AcTheme {
        AlertAction(
            data = AlertActionData(
                titleAction = "Alert Action Title",
                subtitleAction = "Subtitle description",
                positiveActionText = "Confirm",
                negativeActionText = "Cancel"
            ),
            onDismissClick = {},
            onPositiveActionClick = {},
            onNegativeActionClick = {}
        )
    }
}
