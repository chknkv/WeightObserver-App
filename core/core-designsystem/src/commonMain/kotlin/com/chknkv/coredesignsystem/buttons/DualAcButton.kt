package com.chknkv.coredesignsystem.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DualAcButtonVertical(
    modifier: Modifier = Modifier,
    firstButtonText: String,
    secondButtonText: String,
    firstButtonClick: () -> Unit,
    secondButtonClick: () -> Unit,
    firstButtonStyle: AcButtonStyle = AcButtonStyle.Standard,
    secondButtonStyle: AcButtonStyle = AcButtonStyle.Transparent,
    paddingValues: PaddingValues = PaddingValues()
) {
    Column(
        modifier = modifier.padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AcButton(
            text = firstButtonText,
            style = firstButtonStyle,
            onClick = firstButtonClick,
            verticalPadding = 0.dp
        )

        AcButton(
            text = secondButtonText,
            style = secondButtonStyle,
            onClick = secondButtonClick,
            verticalPadding = 0.dp
        )
    }
}

@Composable
fun DualBlueberryHorizontal(
    modifier: Modifier = Modifier,
    firstButtonText: String,
    secondButtonText: String,
    firstButtonClick: () -> Unit,
    secondButtonClick: () -> Unit,
    firstButtonStyle: AcButtonStyle = AcButtonStyle.Negative,
    secondButtonStyle: AcButtonStyle = AcButtonStyle.Standard,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center
    ) {
        AcButton(
            modifier = Modifier.weight(1f),
            text = firstButtonText,
            style = firstButtonStyle,
            onClick = firstButtonClick,
            horizontalPadding = 0.dp
        )

        Spacer(modifier = modifier.width(12.dp))

        AcButton(
            modifier = Modifier.weight(1f),
            text = secondButtonText,
            style = secondButtonStyle,
            onClick = secondButtonClick,
            horizontalPadding = 0.dp
        )
    }
}