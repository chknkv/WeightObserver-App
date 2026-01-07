package com.chknkv.coredesignsystem.progress

import androidx.compose.runtime.Composable
import com.chknkv.coredesignsystem.theming.AcTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun BouncingProgressBarPreview() {
    AcTheme {
        BouncingProgressBar(
            text = "Loading..."
        )
    }
}
