package com.chknkv.feature.main.presentation.components.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.chknkv.coredesignsystem.buttons.AcButton
import com.chknkv.coredesignsystem.buttons.AcButtonStyle
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Body1Secondary
import com.chknkv.coredesignsystem.typography.Body3Secondary
import com.chknkv.coredesignsystem.typography.Headline3
import com.chknkv.coresession.LanguageManager
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import weightobserver_project.feature.feature_main.generated.resources.Res
import weightobserver_project.feature.feature_main.generated.resources.ic_flag_gb
import weightobserver_project.feature.feature_main.generated.resources.ic_flag_ru
import weightobserver_project.feature.feature_main.generated.resources.information_accept
import weightobserver_project.feature.feature_main.generated.resources.settings_language_selection_subtitle
import weightobserver_project.feature.feature_main.generated.resources.settings_language_selection_title

/**
 * Displays a bottom sheet for language selection.
 *
 * @param onDismissRequest Callback invoked when the user dismisses the bottom sheet.
 * @param modifier Modifier to be applied to the bottom sheet layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val languageManager = koinInject<LanguageManager>()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var currentLanguage by remember { mutableStateOf(languageManager.getCurrentLanguageCode().lowercase()) }
    val isRussian = currentLanguage == "ru" || currentLanguage.startsWith("ru-")

    LaunchedEffect(Unit) { sheetState.expand() }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier.fillMaxHeight(),
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false,
            shouldDismissOnClickOutside = false
        ),
        scrimColor = AcTokens.Transparent.getThemedColor(),
        containerColor = AcTokens.Background1.getThemedColor(),
        contentColor = AcTokens.Background1.getThemedColor(),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = AcTokens.BottomSheetHook.getThemedColor()
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Headline3(
                    text = stringResource(Res.string.settings_language_selection_title),
                    modifier = Modifier.fillMaxWidth()
                )

                Body3Secondary(
                    text = stringResource(Res.string.settings_language_selection_subtitle),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    LanguageBlock(
                        modifier = Modifier
                            .weight(1f)
                            .padding(PaddingValues(top = 4.dp, end = 4.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .background(AcTokens.Background0.getThemedColor()),
                        iconRes = Res.drawable.ic_flag_ru,
                        text = "Русский",
                        isSelected = isRussian,
                        selectedSuffix = "Выбран",
                        onClick = { if (!isRussian) { languageManager.setLanguage("ru") } }
                    )

                    LanguageBlock(
                        modifier = Modifier
                            .weight(1f)
                            .padding(PaddingValues(top = 4.dp, start = 4.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .background(AcTokens.Background0.getThemedColor()),
                        iconRes = Res.drawable.ic_flag_gb,
                        text = "English",
                        isSelected = !isRussian,
                        selectedSuffix = "Selected",
                        onClick = { if (isRussian) { languageManager.setLanguage("en") } }
                    )
                }
            }

            AcButton(
                text = stringResource(Res.string.information_accept),
                style = AcButtonStyle.Standard,
                onClick = onDismissRequest,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun LanguageBlock(
    modifier: Modifier,
    iconRes: DrawableResource,
    text: String,
    isSelected: Boolean,
    selectedSuffix: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier = modifier.clickable(onClick = onClick, interactionSource = interactionSource, indication = null)) {
        Box(modifier = Modifier.padding(PaddingValues(12.dp))) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(96.dp)
                )

                val displayText = if (isSelected) "$text\u00A0•\u00A0$selectedSuffix" else text
                Body1Secondary(
                    maxLines = 1,
                    modifier = Modifier.padding(top = 12.dp),
                    text = displayText
                )
            }
        }
    }
}
