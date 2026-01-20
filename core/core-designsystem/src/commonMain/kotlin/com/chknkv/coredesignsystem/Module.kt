package com.chknkv.coredesignsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor

@Composable
fun Module(
    modifier: Modifier = Modifier,
    outPaddingValues: PaddingValues = PaddingValues(top = 4.dp, start = 8.dp, end = 8.dp),
    innerPaddingValues: PaddingValues = PaddingValues(12.dp),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(outPaddingValues)
            .clip(RoundedCornerShape(16.dp))
            .background(AcTokens.Module.getThemedColor())
    ) {
        Box(modifier = Modifier.padding(innerPaddingValues)) {
            content()
        }
    }
}