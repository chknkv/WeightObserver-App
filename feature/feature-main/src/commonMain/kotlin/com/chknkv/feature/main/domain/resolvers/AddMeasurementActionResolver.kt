package com.chknkv.feature.main.domain.resolvers

import com.chknkv.feature.main.domain.MainScreenInteractor
import com.chknkv.feature.main.model.presentation.MainUiResult
import com.chknkv.feature.main.model.presentation.uiAction.AddMeasurementAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Resolver responsible for handling weight measurement entry actions.
 *
 * This resolver manages the logic for the "Add Measurement" feature:
 * - Handling text input updates for weight (filtering digits, formatting)
 * - Validating input and enabling/disabling the save button
 * - Saving the new weight measurement via the interactor
 * - Updating the main screen state after a successful save
 * - Handling the visibility of the "Add Measurement" dialog/screen
 *
 * @property interactor Interactor for saving weight data and retrieving updated values.
 */
class AddMeasurementActionResolver(
    private val interactor: MainScreenInteractor
) : ActionResolver<AddMeasurementAction> {

    override fun resolve(
        action: AddMeasurementAction,
        currentState: MainUiResult
    ): Flow<ResolverResult> = flow {
        when (action) {
            is AddMeasurementAction.HideAddMeasurement -> {
                emit(ResolverResult.Mutation { it.copy(isAddMeasurementVisible = false) })
            }

            is AddMeasurementAction.SaveWeight -> {
                val rawInput = currentState.measurementUiResult.rawInput
                val cleanInput = rawInput.trimEnd('.')
                val weight = cleanInput.toDoubleOrNull()
                if (weight != null && weight > 0) {
                    interactor.saveWeight(weight)
                    emit(ResolverResult.Mutation { it.copy(isAddMeasurementVisible = false) })

                    val lastWeight = interactor.getLastWeight()
                    val chartData = interactor.getChartDataForWindow(0)
                    val canSwipeRight = interactor.hasOlderWindow(0)
                    emit(ResolverResult.Mutation {
                        it.copy(
                            lastSavedWeight = lastWeight,
                            chartData = chartData,
                            chartWindowIndex = 0,
                            chartCanSwipeRight = canSwipeRight
                        )
                    })
                }
            }

            is AddMeasurementAction.UpdateWeightInput -> {
                val filtered = filterWeightInput(
                    newInput = action.input,
                    currentInput = currentState.measurementUiResult.rawInput
                )
                val parsedWeight = filtered.trimEnd('.').toDoubleOrNull() ?: 0.0
                val isSaveEnabled = parsedWeight > 0

                emit(ResolverResult.Mutation {
                    it.copy(
                        measurementUiResult = it.measurementUiResult.copy(
                            rawInput = filtered,
                            isSaveEnabled = isSaveEnabled
                        )
                    )
                })
            }
        }
    }
}

/**
 * Filters and validates weight input to enforce the format: up to 3 digits
 * before the decimal separator and up to 2 digits after (max 999.99).
 *
 * Rules:
 * - Only digits, '.' and ',' are allowed; ',' is normalized to '.'
 * - No leading zeros (except the pattern "0.")
 * - At most one decimal separator
 * - Max 3 integer digits, max 2 fractional digits
 *
 * @param newInput The raw new value from the text field.
 * @param currentInput The previously accepted value (used as fallback).
 * @return The filtered input string.
 */
private fun filterWeightInput(newInput: String, currentInput: String): String {
    val normalized = buildString {
        for (char in newInput) {
            when {
                char.isDigit() -> append(char)
                char == '.' || char == ',' -> append('.')
            }
        }
    }

    // Ensure at most one dot
    val dotIndex = normalized.indexOf('.')
    val sanitized = if (dotIndex >= 0) {
        val beforeDot = normalized.substring(0, dotIndex)
        val afterDot = normalized.substring(dotIndex + 1).replace(".", "")
        "$beforeDot.$afterDot"
    } else {
        normalized
    }

    // Split into integer and fractional parts
    val parts = sanitized.split('.')
    var integerPart = parts[0]
    val fractionalPart = parts.getOrNull(1)

    // Remove all leading zeros (first digit must be 1-9)
    while (integerPart.startsWith("0")) {
        integerPart = integerPart.drop(1)
    }

    // Enforce max 3 integer digits
    integerPart = integerPart.take(3)

    // Disallow decimal separator as first character (no "0.xx" inputs)
    if (integerPart.isEmpty() && fractionalPart != null) {
        return integerPart
    }

    // Build the result
    return if (fractionalPart != null) {
        // Enforce max 2 fractional digits
        "$integerPart.${fractionalPart.take(2)}"
    } else {
        integerPart
    }
}
