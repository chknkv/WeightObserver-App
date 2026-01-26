package com.chknkv.coreauthentication.models.domain.handles.settings

/**
 * Callbacks for the passcode-settings flow (e.g. change passcode in settings).
 *
 * @param onClose Invoked when the flow is closed (dismiss sheet).
 * @param onPasscodeCreated Invoked when passcode is successfully created/changed (e.g. show snackbar).
 */
data class PasscodeSettingsHandles(
    val onClose: () -> Unit,
    val onPasscodeCreated: () -> Unit
)
