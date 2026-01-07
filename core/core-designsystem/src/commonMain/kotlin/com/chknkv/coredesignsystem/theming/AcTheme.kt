package com.chknkv.coredesignsystem.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

/** ThemeProvider */
object AcTheme {
    internal val tokens: Map<AcTokens, Color>
        @Composable
        @ReadOnlyComposable
        get() = LocalTokens.current
}

/**
 * Main UI-theme of application
 *
 * @param darkTheme true - dark theme, false - light theme
 * @param content main content
 */
@Composable
fun AcTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalTokens provides getColorsThemeTokens(darkTheme)) {
        content()
    }
}

/** Selecting current theme tokens */
@Composable
private fun getColorsThemeTokens(isDarkTheme: Boolean): Map<AcTokens, Color> =
    if (isDarkTheme) darkThemeTokensMap() else lightThemeTokensMap()