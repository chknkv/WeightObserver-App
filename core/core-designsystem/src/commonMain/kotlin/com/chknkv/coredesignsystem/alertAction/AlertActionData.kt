package com.chknkv.coredesignsystem.alertAction

import androidx.compose.runtime.Stable

/**
 * @property titleAction The main title text of the alertAction dialog
 * @property subtitleAction Optional secondary message providing additional context or clarification
 * @property positiveActionText The text for the primary (confirmation) button
 * @property negativeActionText Optional text for the secondary (dismissal or cancel) button
 */
@Stable
data class AlertActionData(
    val titleAction: String,
    val subtitleAction: String? = null,
    val positiveActionText: String,
    val negativeActionText: String? = null
)