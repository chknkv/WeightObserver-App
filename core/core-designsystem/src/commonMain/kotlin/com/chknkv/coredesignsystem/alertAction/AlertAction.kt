package com.chknkv.coredesignsystem.alertAction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor

@Composable
fun AlertAction(
    data: AlertActionData,
    onDismissClick: () -> Unit,
    onPositiveActionClick: () -> Unit,
    onNegativeActionClick: (() -> Unit)? = null
) {
    Dialog(
        onDismissRequest = onDismissClick,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = AcTokens.AlertActionBackground.getThemedColor(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(modifier = Modifier.padding(vertical = 28.dp, horizontal = 24.dp)) {
                Column {
                    Text(
                        text = data.titleAction,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2,
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            color = AcTokens.TextPrimary.getThemedColor(),
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            letterSpacing = -(0.048).em,
                            fontWeight = FontWeight.Bold
                        ),
                        overflow = TextOverflow.Ellipsis
                    )

                    data.subtitleAction?.let { subtitle ->
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .size(12.dp)
                        )

                        Text(
                            text = subtitle,
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 7,
                            textAlign = TextAlign.Start,
                            style = TextStyle(
                                color = AcTokens.TextSecondary.getThemedColor(),
                                fontSize = 16.sp,
                                lineHeight = 18.sp,
                                letterSpacing = -(0.048).em,
                                fontWeight = FontWeight.Normal
                            ),
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(22.dp, Alignment.End),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (data.negativeActionText != null && onNegativeActionClick != null) {
                            ButtonAction(
                                title = data.negativeActionText,
                                color = AcTokens.IconWarning.getThemedColor(),
                                onAction = onNegativeActionClick
                            )
                        }

                        ButtonAction(
                            title = data.positiveActionText,
                            color = AcTokens.TextPrimary.getThemedColor(),
                            onAction = onPositiveActionClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ButtonAction(
    title: String,
    color: Color,
    onAction: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onAction
            )
    ) {
        Text(
            text = title,
            maxLines = 1,
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = color,
                fontSize = 16.sp,
                lineHeight = 18.sp,
                letterSpacing = -(0.048).em,
                fontWeight = FontWeight.Normal
            ),
            overflow = TextOverflow.Ellipsis
        )
    }
}