package com.chknkv.coredesignsystem.passcode

import androidx.compose.runtime.Composable
import com.chknkv.coredesignsystem.theming.AcTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun PasscodeKeyboardPreview() {
    AcTheme {
        PasscodeKeyboard(
            onNumberClick = {},
            onDeleteClick = {}
        )
    }
}
