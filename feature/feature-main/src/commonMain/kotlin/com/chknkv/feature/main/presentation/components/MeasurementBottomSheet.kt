package com.chknkv.feature.main.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chknkv.coredesignsystem.buttons.AcButtonStyle
import com.chknkv.coredesignsystem.buttons.DualAcButtonHorizontal
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Footnote1Secondary
import com.chknkv.coredesignsystem.typography.Headline3
import com.chknkv.feature.main.model.presentation.MainAction
import com.chknkv.feature.main.model.presentation.MeasurementUiResult
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import weightobserver_project.feature.feature_main.generated.resources.Res
import weightobserver_project.feature.feature_main.generated.resources.measurement_add
import weightobserver_project.feature.feature_main.generated.resources.measurement_cancel
import weightobserver_project.feature.feature_main.generated.resources.measurement_privacy_policy
import weightobserver_project.feature.feature_main.generated.resources.measurement_title

/**
 * Displays a bottom sheet for adding a new weight measurement.
 *
 * This sheet provides an interface for the user to input and save a new weight record.
 * It is configured to prevent accidental dismissal by clicking on the scrim or swiping down.
 *
 * @param onAction Callback to propagate user actions (e.g., saving data) to the parent component.
 * @param onDismissRequest Callback invoked when the user explicitly cancels the operation or requests to close the sheet.
 * @param modifier Modifier to be applied to the bottom sheet layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasurementBottomSheet(
    measurementUiResult: MeasurementUiResult,
    onAction: (MainAction) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val focusRequester = remember { FocusRequester() }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false,
            shouldDismissOnClickOutside = false
        ),
        scrimColor = AcTokens.BottomSheetScrim.getThemedColor(),
        containerColor = AcTokens.Background1.getThemedColor(),
        contentColor = AcTokens.Background1.getThemedColor(),
        dragHandle = { BottomSheetDefaults.DragHandle(color = AcTokens.BottomSheetHook.getThemedColor()) },
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
    ) {
        val density = LocalDensity.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Headline3(
                text = stringResource(Res.string.measurement_title),
                modifier = Modifier.fillMaxWidth()
            )

            Footnote1Secondary(
                text = stringResource(Res.string.measurement_privacy_policy),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 16.dp)
            )

            Box(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = TextFieldValue(
                        text = measurementUiResult.rawInput,
                        selection = TextRange(measurementUiResult.rawInput.length)
                    ),
                    onValueChange = { newValue ->
                        onAction(MainAction.AddMeasurementAction.UpdateWeightInput(newValue.text))
                    },
                    modifier = Modifier.focusRequester(focusRequester),
                    textStyle = TextStyle(
                        fontSize = with(density) { 64.dp.toSp() },
                        color = AcTokens.TextPrimary.getThemedColor(),
                        textAlign = TextAlign.Center,
                        letterSpacing = (-0.85).sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        autoCorrectEnabled = false
                    ),
                    cursorBrush = SolidColor(AcTokens.TextPrimary.getThemedColor()),
                    singleLine = true,
                    visualTransformation = WeightVisualTransformation()
                )
            }

            DualAcButtonHorizontal(
                firstButtonText = stringResource(Res.string.measurement_cancel),
                secondButtonText = stringResource(Res.string.measurement_add),
                firstButtonClick = onDismissRequest,
                secondButtonClick = { onAction(MainAction.AddMeasurementAction.SaveWeight) },
                firstButtonStyle = AcButtonStyle.Transparent,
                secondButtonStyle = AcButtonStyle.Standard,
                secondButtonEnabled = measurementUiResult.isSaveEnabled
            )
        }
    }
    
    LaunchedEffect(Unit) {
        delay(500)
        focusRequester.requestFocus()
    }
}

private class WeightVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        
        val builder = StringBuilder("---.-")
        
        val reversedRaw = raw.reversed()
        
        if (reversedRaw.isNotEmpty()) builder[4] = reversedRaw[0]
        if (reversedRaw.length > 1) builder[2] = reversedRaw[1]
        if (reversedRaw.length > 2) builder[1] = reversedRaw[2]
        if (reversedRaw.length > 3) builder[0] = reversedRaw[3]
        
        val output = builder.toString()
        
        return TransformedText(
            AnnotatedString(output),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return output.length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return raw.length
                }
            }
        )
    }
}
