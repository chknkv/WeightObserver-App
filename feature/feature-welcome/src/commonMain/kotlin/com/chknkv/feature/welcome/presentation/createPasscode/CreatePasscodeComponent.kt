package com.chknkv.feature.welcome.presentation.createPasscode

import com.arkivanov.decompose.ComponentContext

/**
 * Component for the passcode creation screen.
 *
 * Responsible for the logic of creating a new PIN code for app entry.
 */
interface CreatePasscodeComponent {

    /**
     * Called when the "Next" or "Skip" button is clicked.
     *
     * Signals the completion of the passcode creation step.
     */
    fun onNextClicked()
}

/**
 * Implementation of [CreatePasscodeComponent].
 *
 * @property componentContext Decompose component context.
 * @property onNext Callback invoked to proceed to the next stage (completion of authorization).
 */
class CreatePasscodeComponentImpl(
    componentContext: ComponentContext,
    private val onNext: () -> Unit
) : CreatePasscodeComponent, ComponentContext by componentContext {

    override fun onNextClicked() {
        onNext()
    }
}
