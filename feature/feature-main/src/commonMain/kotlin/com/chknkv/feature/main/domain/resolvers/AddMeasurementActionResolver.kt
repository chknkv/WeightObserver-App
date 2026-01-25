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
                if (rawInput.length >= 2) {
                    val weight = rawInput.toDoubleOrNull()?.div(10.0)
                    if (weight != null) {
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
            }

            is AddMeasurementAction.UpdateWeightInput -> {
                var filtered = action.input.filter { it.isDigit() }
                while (filtered.startsWith("0")) {
                    filtered = filtered.drop(1)
                }
                filtered = filtered.take(4)
                val isSaveEnabled = filtered.length >= 2

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
