package com.chknkv.feature.welcome.presentation.information

import com.arkivanov.decompose.ComponentContext

/**
 * Component for the information screen.
 *
 * Displays information about the application or an introductory briefing.
 */
interface InformationComponent {

    /**
     * Called when the "Next" button is clicked.
     *
     * Signals the completion of viewing the information.
     */
    fun onNextClicked()
}

/**
 * Implementation of [InformationComponent].
 *
 * @property componentContext Decompose component context.
 * @property onNext Callback invoked to proceed to the next stage.
 */
class InformationComponentImpl(
    componentContext: ComponentContext,
    private val onNext: () -> Unit
) : InformationComponent, ComponentContext by componentContext {

    override fun onNextClicked() {
        onNext()
    }
}
