package com.chknkv.coredesignsystem

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor

/**
 * Extension function for Modifier that adds a shimmer loading effect.
 * 
 * Usage:
 * ```
 * Box(modifier = Modifier.fillMaxSize().height(40.dp).shimmered())
 * ```
 * 
 * @param durationMillis The duration of one shimmer animation cycle in milliseconds. Defaults to 1000ms.
 */
fun Modifier.shimmered(
    durationMillis: Int = 1000
): Modifier = composed {
    val baseColor = AcTokens.ShimmerBase.getThemedColor()
    val highlightColor = AcTokens.ShimmerHighlight.getThemedColor()
    
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerTranslate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    this.background(
        brush = Brush.linearGradient(
            colors = listOf(
                baseColor,
                baseColor,
                highlightColor,
                baseColor,
                baseColor
            ),
            start = Offset(shimmerTranslate - 300f, shimmerTranslate - 300f),
            end = Offset(shimmerTranslate, shimmerTranslate)
        )
    )
}

