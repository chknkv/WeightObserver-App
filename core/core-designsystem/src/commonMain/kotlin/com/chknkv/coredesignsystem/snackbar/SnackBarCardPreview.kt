package com.chknkv.coredesignsystem.snackbar

import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import com.chknkv.coredesignsystem.theming.AcTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun SnackBarCardPreview() {
    val mockSnackbarData = object : SnackbarData {
        override val visuals: SnackbarVisuals = object : SnackbarVisuals {
            override val actionLabel: String? = null
            override val duration: androidx.compose.material3.SnackbarDuration = androidx.compose.material3.SnackbarDuration.Short
            override val message: String = "This is a snackbar message"
            override val withDismissAction: Boolean = false
        }
        override fun dismiss() {}
        override fun performAction() {}
    }

    AcTheme {
        SnackBarCard(
            snackbarData = mockSnackbarData,
            data = SnackBarCardData(
                closeIcon = true
            )
        )
    }
}
