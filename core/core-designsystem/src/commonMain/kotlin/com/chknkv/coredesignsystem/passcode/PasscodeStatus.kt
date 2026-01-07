package com.chknkv.coredesignsystem.passcode

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor

@Composable
fun PasscodeStatus(
    enteredDigitsSize: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(MAX_DIGITS_COUNTER) { index ->
            val isFilled = index < enteredDigitsSize
            val color = if (isFilled) {
                AcTokens.TextPrimary.getThemedColor()
            } else {
                AcTokens.IconSecondary.getThemedColor()
            }

            Canvas(
                modifier = Modifier.size(16.dp)
            ) {
                drawCircle(
                    color = color,
                    radius = size.minDimension / 2
                )
            }
        }
    }
}

private const val MAX_DIGITS_COUNTER = 5