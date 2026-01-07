package com.chknkv.coredesignsystem.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Body1

@Composable
fun ProgressBarCircle(
    modifier: Modifier = Modifier,
    text: String? = null
) {
    Column(
        modifier = Modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = modifier.then(Modifier.size(48.dp)),
            color = AcTokens.ProgressBar.getThemedColor()
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