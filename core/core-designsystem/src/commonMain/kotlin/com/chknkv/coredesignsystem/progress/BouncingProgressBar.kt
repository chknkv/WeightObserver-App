package com.chknkv.coredesignsystem.progress

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Body1
import kotlinx.coroutines.delay

@Composable
fun BouncingProgressBar(
    modifier: Modifier = Modifier,
    text: String? = null,
    dotColor: Color = AcTokens.ProgressBar.getThemedColor(),
    dotSize: Dp = 10.dp,
    spaceBetween: Dp = 6.dp,
    animationDelay: Int = 100
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProgressBarDots(
            modifier = modifier.then(Modifier.size(48.dp)),
            dotColor = dotColor,
            dotSize = dotSize,
            spaceBetween = spaceBetween,
            animationDelay = animationDelay
        )

        if (text != null) {
            Body1(
                modifier = Modifier.padding(top = 32.dp),
                text = text,
                maxLines = 2,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProgressBarDots(
    modifier: Modifier = Modifier,
    dotColor: Color = AcTokens.ProgressBar.getThemedColor(),
    dotSize: Dp,
    spaceBetween: Dp,
    animationDelay: Int
) {
    val animations = List(3) { remember { Animatable(initialValue = 0f) } }

    animations.forEachIndexed { index, anim ->
        LaunchedEffect(Unit) {
            delay((index * animationDelay).toLong())
            anim.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 600, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        animations.forEach { anim ->
            val scale = 0.5f + (anim.value * 0.5f)
            Canvas(modifier = Modifier.size(dotSize)) {
                drawCircle(
                    color = dotColor,
                    radius = (size.minDimension / 2) * scale
                )
            }
        }
    }
}