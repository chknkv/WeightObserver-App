package com.chknkv.coreauthentication.models.presentation.uiAction.createpasscode

/**
 * User actions for the Create Passcode screen.
 */
sealed interface CreatePasscodeUiAction {

    /** Initial load; emitted once when the screen is first shown. */
    data object Init : CreatePasscodeUiAction

    /** User navigated back. */
    data object Back : CreatePasscodeUiAction

    /** User tapped "skip"; show confirmation alert. */
    data object ShowAlert : CreatePasscodeUiAction

    /** User dismissed or declined skip alert. */
    data object DismissAlert : CreatePasscodeUiAction

    /** User tapped a digit key (0–9). [digit] is the pressed digit. */
    data class NumberClick(val digit: Int) : CreatePasscodeUiAction

    /** User tapped the backspace/delete key. */
    data object DeleteClick : CreatePasscodeUiAction

    /** User confirmed skip; consumer should navigate to Main (no passcode). */
    data object Skip : CreatePasscodeUiAction
}
