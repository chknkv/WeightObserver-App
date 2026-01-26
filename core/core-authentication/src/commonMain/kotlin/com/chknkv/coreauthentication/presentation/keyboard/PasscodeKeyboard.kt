package com.chknkv.coreauthentication.presentation.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Footnote1Secondary
import com.chknkv.coredesignsystem.typography.Headline1
import org.jetbrains.compose.resources.painterResource
import weightobserver_project.core.core_authentication.generated.resources.Res
import weightobserver_project.core.core_authentication.generated.resources.ic_backspace
import weightobserver_project.core.core_authentication.generated.resources.ic_biometry

/**
 * Internal numeric passcode keyboard with digit keys and a trailing action key.
 * Triggers [onNumberClick] for 0–9. The trailing key (bottom-right):
 * - If [onBiometricClick] is non-null and [enteredDigitsSize] is 0: shows biometric icon, tap → [onBiometricClick].
 * - Otherwise: shows backspace icon, tap → [onDeleteClick].
 * Use [onBiometricClick] = null (default) on CreatePasscode for delete-only behavior.
 */
@Composable
internal fun PasscodeKeyboard(
    onNumberClick: (Int) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    onBiometricClick: (() -> Unit)? = null,
    enteredDigitsSize: Int = 0
) {
    val keyRows = remember { keyRows }
    val showBiometry = onBiometricClick != null && enteredDigitsSize == 0
    val trailingOnClick = when {
        onBiometricClick != null && showBiometry -> onBiometricClick
        else -> onDeleteClick
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        keyRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                row.forEach { key ->
                    when (key.number) {
                        null -> Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 2.dp, horizontal = 2.dp)
                                .height(PASSCODE_KEY_HEIGHT_SIZE)
                        )

                        TRAILING_KEY_ID -> PasscodeTrailingKey(
                            modifier = Modifier.weight(1f),
                            showBiometryIcon = showBiometry,
                            onClick = trailingOnClick
                        )

                        else -> PasscodeKey(
                            label = key.number.toString(),
                            subLabel = key.label,
                            modifier = Modifier.weight(1f)
                        ) { onNumberClick(key.number) }
                    }
                }
            }
        }
    }
}

@Composable
private fun PasscodeKey(
    label: String,
    subLabel: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .padding(vertical = 2.dp, horizontal = 2.dp)
            .height(PASSCODE_KEY_HEIGHT_SIZE)
            .background(
                color = AcTokens.PasscodeKey.getThemedColor(),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Headline1(text = label)
            if (subLabel.isNotEmpty()) {
                Footnote1Secondary(
                    text = subLabel,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun PasscodeTrailingKey(
    modifier: Modifier = Modifier,
    showBiometryIcon: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .padding(vertical = 2.dp, horizontal = 2.dp)
            .height(PASSCODE_KEY_HEIGHT_SIZE)
            .background(
                color = AcTokens.Transparent.getThemedColor(),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(
                if (showBiometryIcon) Res.drawable.ic_biometry
                else Res.drawable.ic_backspace
            ),
            contentDescription = null,
            tint = AcTokens.TextSecondary.getThemedColor()
        )
    }
}

private data class KeyData(val number: Int?, val label: String)

private const val TRAILING_KEY_ID = -1
private val PASSCODE_KEY_HEIGHT_SIZE = 72.dp

private val keyRows = listOf(
    listOf(
        KeyData(1, "A B C"),
        KeyData(2, "D E F"),
        KeyData(3, "G H I")
    ),
    listOf(
        KeyData(4, "J K L"),
        KeyData(5, "M N O"),
        KeyData(6, "P Q R")
    ),
    listOf(
        KeyData(7, "S T U"),
        KeyData(8, "V W X"),
        KeyData(9, "Y Z"),
    ),
    listOf(
        KeyData(null, ""),
        KeyData(0, ""),
        KeyData(TRAILING_KEY_ID, "")
    )
)
