package com.chknkv.feature.main.presentation

import com.arkivanov.decompose.ComponentContext
import com.chknkv.coresession.SessionRepository

/**
 * Root component for the main application feature.
 *
 * Responsible for the logic of the main screen available to an authorized user.
 * Handles user actions, such as signing out.
 */
interface RootMainComponent {

    /**
     * Called when the sign-out button is clicked.
     *
     * Initiates the session termination process and navigation to the welcome screen.
     */
    fun onSignOut()
}

/**
 * Implementation of [RootMainComponent].
 *
 * @property componentContext Decompose component context for lifecycle management.
 * @property sessionRepository Repository for managing user session (authorization flag, etc.).
 * @property onSignOutRequested Callback notifying the parent component about the sign-out request.
 */
class RootMainComponentImpl(
    componentContext: ComponentContext,
    private val sessionRepository: SessionRepository,
    private val onSignOutRequested: () -> Unit
) : RootMainComponent, ComponentContext by componentContext {

    /**
     * Handles the sign-out request.
     *
     * Resets the authorization flag in [sessionRepository] and invokes [onSignOutRequested].
     */
    override fun onSignOut() {
        sessionRepository.isFirstAuthorized = false
        onSignOutRequested()
    }
}
