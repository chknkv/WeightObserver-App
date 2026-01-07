package com.chknkv.coredesignsystem.alert

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.chknkv.coredesignsystem.buttons.AcButton
import com.chknkv.coredesignsystem.buttons.DualAcButtonVertical
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Body1Secondary
import com.chknkv.coredesignsystem.typography.Title2
import weightobserver_project.core.core_designsystem.generated.resources.Res
import weightobserver_project.core.core_designsystem.generated.resources.ic_close
import weightobserver_project.core.core_designsystem.generated.resources.icon_close
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun Alert(
    data: AlertData,
    onDismissClick: (() -> Unit),
    onFirstButtonClick: (() -> Unit),
    onSecondButtonClick: (() -> Unit)? = null
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
                .padding(24.dp),
            color = AcTokens.Background0.getThemedColor(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(4.dp)) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    data.image?.let { image ->
                        Image(
                            modifier = Modifier
                                .padding(vertical = 16.dp, horizontal = 25.dp)
                                .align(Alignment.Center),
                            painter = painterResource(image),
                            contentDescription = null
                        )
                    }

                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(48.dp),
                        onClick = onDismissClick
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_close),
                            contentDescription = stringResource(Res.string.icon_close),
                            tint = AcTokens.IconSecondary.getThemedColor()
                        )
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Title2(
                        text = data.title,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Body1Secondary(
                        text = data.subtitle,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        maxLines = 4
                    )

                    if (onSecondButtonClick == null) {
                        AcButton(
                            text = data.firstButtonText,
                            style = data.firstButtonStyle,
                            onClick = onFirstButtonClick
                        )
                    } else {
                        DualAcButtonVertical(
                            firstButtonText = data.firstButtonText,
                            secondButtonText = data.secondButtonText ?: "",
                            firstButtonClick = onFirstButtonClick,
                            secondButtonClick = onSecondButtonClick,
                            firstButtonStyle = data.firstButtonStyle,
                            secondButtonStyle = data.secondButtonStyle
                        )
                    }
                }
            }
        }
    }
}