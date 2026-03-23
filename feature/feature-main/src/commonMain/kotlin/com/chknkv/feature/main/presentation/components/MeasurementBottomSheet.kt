package com.chknkv.feature.main.presentation.components

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chknkv.coredesignsystem.buttons.AcButtonStyle
import com.chknkv.coredesignsystem.buttons.DualAcButtonHorizontal
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Footnote1Secondary
import com.chknkv.coredesignsystem.typography.Headline3
import com.chknkv.feature.main.model.presentation.MainAction
import com.chknkv.feature.main.model.presentation.uiResult.MeasurementUiResult
import com.chknkv.feature.main.model.presentation.uiAction.AddMeasurementAction
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import weightobserver_project.feature.feature_main.generated.resources.Res
import weightobserver_project.feature.feature_main.generated.resources.main_weight_unit
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
        dragHandle = null,
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) { detectVerticalDragGestures { _, _ -> } }
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp),
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

            WeightInputField(
                rawInput = measurementUiResult.rawInput,
                onValueChange = { onAction(AddMeasurementAction.UpdateWeightInput(it)) },
                focusRequester = focusRequester,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            DualAcButtonHorizontal(
                firstButtonText = stringResource(Res.string.measurement_cancel),
                secondButtonText = stringResource(Res.string.measurement_add),
                firstButtonClick = onDismissRequest,
                secondButtonClick = { onAction(AddMeasurementAction.SaveWeight) },
                firstButtonStyle = AcButtonStyle.Transparent,
                secondButtonStyle = AcButtonStyle.Standard,
                secondButtonEnabled = measurementUiResult.isSaveEnabled
            )
        }
    }

    LaunchedEffect(sheetState.isVisible) {
        if (sheetState.isVisible) {
            delay(100)
            focusRequester.requestFocus()
        }
    }
}

/**
 * A centered weight input field with a localized unit suffix ("kg" / "кг").
 *
 * Displays a placeholder "0" in [AcTokens.TextSecondary] when no input is present.
 * When the user types a meaningful value (parsed weight > 0), both the entered text
 * and the unit suffix switch to [AcTokens.TextPrimary].
 *
 * @param rawInput The current raw text value (digits and optional decimal separator).
 * @param onValueChange Callback when the text changes.
 * @param focusRequester [FocusRequester] to programmatically request focus.
 * @param modifier Modifier applied to the outer container.
 */
@Composable
private fun WeightInputField(
    rawInput: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val hasMeaningfulInput = rawInput.trimEnd('.').toDoubleOrNull()?.let { it > 0 } ?: false
    val textColor = (if (hasMeaningfulInput) AcTokens.TextPrimary else AcTokens.TextSecondary).getThemedColor()

    val fontSize = with(density) { 64.dp.toSp() }
    val textStyle = TextStyle(
        fontSize = fontSize,
        color = textColor,
        letterSpacing = (-0.85).sp
    )

    // When rawInput is empty, display "0" as actual text so the cursor sits after it
    val displayText = rawInput.ifEmpty { "0" }

    // Measure actual text width so BasicTextField wraps its content
    val textMeasurer = rememberTextMeasurer()
    val measuredWidth = textMeasurer.measure(displayText, textStyle).size.width
    val fieldWidth = with(density) { measuredWidth.toDp() + 4.dp } // +4dp for cursor

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = TextFieldValue(
                text = displayText,
                selection = TextRange(displayText.length)
            ),
            onValueChange = { newValue -> onValueChange(newValue.text) },
            modifier = modifier
                .focusRequester(focusRequester)
                .width(fieldWidth),
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                autoCorrectEnabled = false
            ),
            cursorBrush = SolidColor(AcTokens.TextPrimary.getThemedColor()),
            singleLine = true
        )

        Text(
            text = stringResource(Res.string.main_weight_unit),
            modifier = Modifier.padding(start = 4.dp),
            style = TextStyle(
                color = textColor,
                fontSize = 24.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}
