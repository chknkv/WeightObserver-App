package com.chknkv.feature.main.domain.resolvers

import com.chknkv.feature.main.model.presentation.MainUiResult
import com.chknkv.feature.main.model.presentation.uiResult.PasscodeSettingsUiEffect
import kotlinx.coroutines.flow.Flow

/**
 * Represents the result of a resolved action.
 */
sealed interface ResolverResult {
    /**
     * A state mutation.
     */
    data class Mutation(val transform: (MainUiResult) -> MainUiResult) : ResolverResult

    /**
     * A side effect.
     */
    data class Effect(val effect: PasscodeSettingsUiEffect) : ResolverResult
}

/**
 * Interface for action resolvers.
 */
interface ActionResolver<Action> {
    /**
     * Resolves the given action.
     *
     * @param action The action to resolve.
     * @param currentState The current state of the UI.
     * @return A flow of results (mutations or effects).
     */
    fun resolve(action: Action, currentState: MainUiResult): Flow<ResolverResult>
}
