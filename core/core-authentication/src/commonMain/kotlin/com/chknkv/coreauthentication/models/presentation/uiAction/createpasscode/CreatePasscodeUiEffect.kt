package com.chknkv.coreauthentication.models.presentation.uiAction.createpasscode

/**
 * One-time UI effects for the Create Passcode screen.
 */
sealed interface CreatePasscodeUiEffect {

    /** Emitted when confirmation passcode does not match the first entry. */
    data object PasswordsDoNotMatch : CreatePasscodeUiEffect
}
