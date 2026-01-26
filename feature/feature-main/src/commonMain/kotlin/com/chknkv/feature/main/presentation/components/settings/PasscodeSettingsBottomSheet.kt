package com.chknkv.feature.main.presentation.components.settings

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.chknkv.coreauthentication.models.domain.handles.settings.PasscodeSettingsHandles
import com.chknkv.coreauthentication.presentation.settings.PasscodeSettingsFlow
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coreauthentication.domain.BiometricAuthenticator
import com.chknkv.coreauthentication.domain.PasscodeRepository
import com.chknkv.coresession.SessionRepository
import org.koin.compose.koinInject

/**
 * A bottom sheet composable for handling passcode settings.
 *
 * This component manages the flow for entering an existing passcode, creating a new passcode,
 * and confirming the new passcode. It handles user interactions, displays appropriate alerts
 * (skip creation, forgot passcode), and responds to UI effects (e.g., invalid passcode).
 *
 * @param componentContext ComponentContext
 * @param onDismissRequest Callback invoked when the bottom sheet is dismissed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasscodeSettingsBottomSheet(
    componentContext: ComponentContext,
    onDismissRequest: () -> Unit
) {
    val passcodeRepository: PasscodeRepository = koinInject()
    val biometricAuthenticator: BiometricAuthenticator = koinInject()
    val sessionRepository: SessionRepository = koinInject()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var initialEnter by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        initialEnter = passcodeRepository.getPasscodeHash() != null
        sheetState.expand()
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle(color = AcTokens.BottomSheetHook.getThemedColor()) },
        containerColor = AcTokens.Background0.getThemedColor(),
        contentColor = AcTokens.Background0.getThemedColor(),
        modifier = Modifier.fillMaxHeight()
    ) {

        val enter = initialEnter
        if (enter != null) {
            PasscodeSettingsFlow(
                componentContext = componentContext,
                passcodeRepository = passcodeRepository,
                biometricAuthenticator = biometricAuthenticator,
                sessionRepository = sessionRepository,
                handles = PasscodeSettingsHandles(
                    onClose = onDismissRequest,
                    onPasscodeCreated = onDismissRequest
                ),
                initialEnter = enter
            )
        }
    }
}
