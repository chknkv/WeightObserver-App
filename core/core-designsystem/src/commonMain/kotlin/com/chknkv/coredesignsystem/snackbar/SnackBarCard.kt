package com.chknkv.coredesignsystem.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import weightobserver_project.core.core_designsystem.generated.resources.Res
import weightobserver_project.core.core_designsystem.generated.resources.ic_close
import weightobserver_project.core.core_designsystem.generated.resources.icon_close
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SnackBarCard(
    snackbarData: SnackbarData,
    data: SnackBarCardData,
) {
    val containerColor = AcTokens.SnackBarCardContainerPrimary.getThemedColor()
    val contentColor = AcTokens.SnackBarCardContentPrimary.getThemedColor()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(12.dp))
                .background(containerColor)
                .padding(
                    vertical = 16.dp,
                    horizontal = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (data.icon != null) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(data.icon),
                    contentDescription = null,
                    tint = data.iconTint
                )
            }

            Text(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                text = snackbarData.visuals.message,
                textAlign = TextAlign.Start,
                maxLines = 2,
                style = TextStyle(
                    color = contentColor,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    letterSpacing = 0.em
                ),
                overflow = TextOverflow.Ellipsis
            )

            if (data.closeIcon) {
                IconButton(
                    modifier = Modifier.size(28.dp),
                    onClick = { snackbarData.dismiss() }
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(Res.drawable.ic_close),
                        contentDescription = stringResource(Res.string.icon_close),
                        tint = contentColor
                    )
                }
            }
        }
    }
}

suspend fun SnackbarHostState.showSnackBarCard(message: String) {
    if (this.currentSnackbarData == null) {
        this.showSnackbar(
            message = message,
            duration = SnackbarDuration.Short
        )
    }
}

data class SnackBarCardData(
    val icon: DrawableResource? = null,
    val iconTint: Color = Color.White,
    val closeIcon: Boolean = false,
)