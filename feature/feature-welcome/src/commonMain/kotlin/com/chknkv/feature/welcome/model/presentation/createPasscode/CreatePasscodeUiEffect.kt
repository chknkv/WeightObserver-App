package com.chknkv.feature.welcome.model.presentation.createPasscode

/**
 * Ui effect for CreatePasscode screen
 */
sealed interface CreatePasscodeUiEffect {

    /** Passwords do not match */
    data object PasswordsDoNotMatch : CreatePasscodeUiEffect
}
