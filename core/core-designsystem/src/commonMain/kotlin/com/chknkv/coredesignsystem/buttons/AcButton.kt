package com.chknkv.coredesignsystem.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor

@Composable
fun AcButton(
    text: String,
    style: AcButtonStyle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) = when (style) {
    AcButtonStyle.Standard -> StandardAcButton(text, onClick, modifier, isEnabled)
    AcButtonStyle.Negative -> NegativeAcButton(text, onClick, modifier, isEnabled)
    AcButtonStyle.Transparent -> TransparentAcButton(text, onClick, modifier, isEnabled)
    AcButtonStyle.TransparentNegative -> TransparentNegativeAcButton(text, onClick, modifier, isEnabled)
}

@Composable
fun AcButton(
    text: String,
    style: AcButtonStyle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    horizontalPadding: Dp = 0.dp,
    verticalPadding: Dp = 16.dp
) = when (style) {
    AcButtonStyle.Standard -> StandardAcButton(text, onClick, modifier, isEnabled, horizontalPadding, verticalPadding)
    AcButtonStyle.Transparent -> TransparentAcButton(text, onClick, modifier, isEnabled, horizontalPadding, verticalPadding)
    AcButtonStyle.Negative -> NegativeAcButton(text, onClick, modifier, isEnabled, horizontalPadding, verticalPadding)
    AcButtonStyle.TransparentNegative -> TransparentNegativeAcButton(text, onClick, modifier, isEnabled, horizontalPadding, verticalPadding)
}

@Composable
private fun StandardAcButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    horizontalPadding: Dp = 0.dp,
    verticalPadding: Dp = 16.dp
) {
    InnerAcButton(
        text = text,
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .then(AcButtonModifier),
        isEnabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = AcTokens.ButtonStandard.getThemedColor(),
            disabledContainerColor = AcTokens.ButtonDisabled.getThemedColor()
        ),
        textColor = AcTokens.TextButtonStandard.getThemedColor(),
        disabledTextColor = AcTokens.TextButtonDisabled.getThemedColor()
    )
}

@Composable
private fun NegativeAcButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    horizontalPadding: Dp = 0.dp,
    verticalPadding: Dp = 16.dp
) {
    InnerAcButton(
        text = text,
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .then(AcButtonModifier),
        isEnabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = AcTokens.ButtonWarning.getThemedColor(),
            disabledContainerColor = AcTokens.ButtonDisabled.getThemedColor()
        ),
        textColor = AcTokens.TextButtonWarning.getThemedColor(),
        disabledTextColor = AcTokens.TextButtonDisabled.getThemedColor()
    )
}

@Composable
private fun TransparentAcButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    horizontalPadding: Dp = 0.dp,
    verticalPadding: Dp = 16.dp
) {
    InnerAcButton(
        text = text,
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .then(AcButtonModifier),
        isEnabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = AcTokens.ButtonTransparent.getThemedColor(),
            disabledContainerColor = AcTokens.ButtonTransparent.getThemedColor()
        ),
        textColor = AcTokens.TextButtonTransparent.getThemedColor(),
        disabledTextColor = AcTokens.TextButtonDisabled.getThemedColor()
    )
}

@Composable
private fun TransparentNegativeAcButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    horizontalPadding: Dp = 0.dp,
    verticalPadding: Dp = 16.dp
) {
    InnerAcButton(
        text = text,
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .then(AcButtonModifier),
        isEnabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = AcTokens.ButtonTransparent.getThemedColor(),
            disabledContainerColor = AcTokens.ButtonTransparent.getThemedColor()
        ),
        textColor = AcTokens.TextButtonTransparentNegative.getThemedColor(),
        disabledTextColor = AcTokens.TextButtonDisabled.getThemedColor()
    )
}

@Composable
private fun InnerAcButton(
    text: String,
    onClick: () -> Unit,
    colors: ButtonColors,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    textColor: Color,
    disabledTextColor: Color,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = colors,
        enabled = isEnabled,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            color = if (isEnabled) textColor else disabledTextColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

enum class AcButtonStyle {
    Standard, 
    Negative,
    Transparent,
    TransparentNegative
}

private val AcButtonModifier = Modifier
    .requiredHeight(48.dp)
    .fillMaxWidth()