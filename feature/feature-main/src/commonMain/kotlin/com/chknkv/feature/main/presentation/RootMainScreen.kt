package com.chknkv.feature.main.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chknkv.coredesignsystem.buttons.AcButton
import com.chknkv.coredesignsystem.buttons.AcButtonStyle
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Body1
import com.chknkv.coredesignsystem.typography.Footnote1
import com.chknkv.coredesignsystem.typography.Title1

/**
 * UI screen for [RootMainComponent].
 *
 * @param component Instance of [RootMainComponent] controlling the logic of this screen.
 */
@Composable
fun RootMainScreen(component: RootMainComponent) {
    val generatedData by component.generatedData.collectAsState()
    val savedWeights by component.savedWeights.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(AcTokens.Background0.getThemedColor())
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            .imePadding(),
        contentColor = AcTokens.Background0.getThemedColor(),
        containerColor = AcTokens.Background0.getThemedColor()
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AcButton(
                    modifier = Modifier.weight(1f),
                    text = "Создать",
                    style = AcButtonStyle.Standard,
                    onClick = { component.generateData() }
                )

                AcButton(
                    modifier = Modifier.weight(1f),
                    text = "Сохранить",
                    style = AcButtonStyle.Standard,
                    onClick = { component.saveData() },
                    isEnabled = generatedData != null,
                )

                AcButton(
                    modifier = Modifier.weight(1f),
                    text = "Очистить",
                    style = AcButtonStyle.TransparentNegative,
                    onClick = { component.onSignOut() }
                )
            }

            if (generatedData != null) {
                Body1("Сгенерировано: ${generatedData?.date} - ${generatedData?.weight}")
            }

            Title1(
                text = "Записи в БД:",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (savedWeights.isEmpty()) {
                    item {
                        Body1(
                            text = "Нет данных в системе",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    items(savedWeights) { record ->
                        Footnote1(
                            text = "${record.date}: ${record.weight}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                        )
                    }
                }
            }
        }
    }
}
