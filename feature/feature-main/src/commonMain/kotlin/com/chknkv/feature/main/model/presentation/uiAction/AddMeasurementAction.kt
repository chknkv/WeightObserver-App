package com.chknkv.feature.main.model.presentation.uiAction

import com.chknkv.feature.main.model.presentation.MainAction

/**
 * Actions specific to the Add Measurement area.
 */
sealed interface AddMeasurementAction : MainAction {

    /** Action to save the currently generated weight record to the database. */
    data object SaveWeight : AddMeasurementAction

    /** Action to hide the add measurement bottom sheet. */
    data object HideAddMeasurement : AddMeasurementAction

    /** Action to update the weight input. */
    data class UpdateWeightInput(val input: String) : AddMeasurementAction
}
