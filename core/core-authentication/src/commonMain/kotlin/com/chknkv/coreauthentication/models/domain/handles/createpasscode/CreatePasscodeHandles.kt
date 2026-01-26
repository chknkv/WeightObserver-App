package com.chknkv.coreauthentication.models.domain.handles.createpasscode

/**
 * Callbacks for the Create Passcode flow.
 *
 * @param onPasscodeCreated Invoked when passcode is successfully created. [showBiometry] is true
 *        if biometric is available and consumer should show Biometry screen; false to go to Main.
 * @param onPasscodeSkipped Invoked when user skips passcode creation; consumer navigates to Main.
 * @param onBack Invoked when user navigates back.
 */
data class CreatePasscodeHandles(
    val onPasscodeCreated: (showBiometry: Boolean) -> Unit,
    val onPasscodeSkipped: () -> Unit,
    val onBack: () -> Unit
)
