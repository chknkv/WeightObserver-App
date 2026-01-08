package com.chknkv.feature.welcome.model.presentation.enterPasscode

/**
 * Ui effect for EnterPasscode screen
 */
sealed interface EnterPasscodeUiEffect {

    /** Invalid passcode */
    data object InvalidPasscode : EnterPasscodeUiEffect
}
