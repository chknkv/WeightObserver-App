package com.chknkv.feature.welcome.presentation.selectLanguage

import com.arkivanov.decompose.ComponentContext

/**
 * Component for the language selection screen.
 *
 * Allows the user to choose the application interface language.
 */
interface SelectLanguageComponent {

    /**
     * Called when the "Next" button is clicked.
     *
     * Signals that the language has been selected and the user can proceed.
     */
    fun onNextClicked()
}

/**
 * Implementation of [SelectLanguageComponent].
 *
 * @property componentContext Decompose component context.
 * @property onNext Callback invoked to proceed to the next stage.
 */
class SelectLanguageComponentImpl(
    componentContext: ComponentContext,
    private val onNext: () -> Unit
) : SelectLanguageComponent, ComponentContext by componentContext {

    override fun onNextClicked() {
        onNext()
    }
}
