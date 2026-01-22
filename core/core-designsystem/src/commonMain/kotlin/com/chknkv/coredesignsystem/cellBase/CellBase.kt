package com.chknkv.coredesignsystem.cellBase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Footnote1
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun CellBase(
    iconRes: DrawableResource,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    maxTitle: Int = 2,
    maxSubtitle: Int = 3,
    iconTint: Color = Color.Unspecified,
    isDivider: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            ),
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier
                .padding(end = 12.dp)
                .size(32.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            Text(
                text = title,
                modifier = Modifier,
                maxLines = maxTitle,
                textAlign = TextAlign.Start,
                style = TextStyle(
                    color = AcTokens.TextPrimary.getThemedColor(),
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    letterSpacing = -(0.048).em,
                    fontWeight = FontWeight.Bold
                ),
                overflow = TextOverflow.Ellipsis
            )

            Footnote1(
                text = subtitle,
                modifier = Modifier.padding(top = 2.dp),
                maxLines = maxSubtitle,
                textAlign = TextAlign.Start,
            )

            if (isDivider) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    color = AcTokens.Divider.getThemedColor()
                )
            }
        }
    }
}
