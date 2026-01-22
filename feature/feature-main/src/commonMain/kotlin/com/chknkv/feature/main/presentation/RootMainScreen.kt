package com.chknkv.feature.main.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chknkv.coredesignsystem.Module
import com.chknkv.coredesignsystem.buttons.AcButton
import com.chknkv.coredesignsystem.buttons.AcButtonStyle
import com.chknkv.coredesignsystem.shimmered
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor
import com.chknkv.coredesignsystem.typography.Footnote1
import com.chknkv.coredesignsystem.typography.Footnote1Secondary
import com.chknkv.coresession.WeightRecord
import com.chknkv.coreutils.getAppVersion
import com.chknkv.coreutils.toFormattedString
import com.chknkv.feature.main.model.presentation.MainAction
import com.chknkv.feature.main.presentation.components.DetailedStatisticBottomSheet
import com.chknkv.feature.main.presentation.components.MeasurementBottomSheet
import com.chknkv.feature.main.presentation.components.SettingBottomSheet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import weightobserver_project.feature.feature_main.generated.resources.Res
import weightobserver_project.feature.feature_main.generated.resources.ic_settings
import weightobserver_project.feature.feature_main.generated.resources.main_add_new_measurement
import weightobserver_project.feature.feature_main.generated.resources.main_last_saved_record
import weightobserver_project.feature.feature_main.generated.resources.main_weight_unit
import weightobserver_project.feature.feature_main.generated.resources.settings_title
import weightobserver_project.feature.feature_main.generated.resources.statistic_show
import weightobserver_project.feature.feature_main.generated.resources.statistic_title

/**
 * UI screen for [RootMainComponent].
 *
 * @param component Instance of [RootMainComponent] controlling the logic of this screen.
 */
@Composable
fun RootMainScreen(component: RootMainComponent) {

    val uiResult by component.uiResult.collectAsState()
    LaunchedEffect(Unit) { component.initLoadMainScreen() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(AcTokens.Background0.getThemedColor())
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            .imePadding(),
        contentColor = AcTokens.Background0.getThemedColor(),
        containerColor = AcTokens.Background0.getThemedColor()
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                TopContentBlock { action -> component.emitAction(action) }
                StatisticBlock { action -> component.emitAction(action) }
                MeasurementBlock(uiResult.lastSavedWeight) { action -> component.emitAction(action) }
            }

            Footnote1Secondary(
                modifier = Modifier.fillMaxWidth(),
                text = getAppVersion(),
                textAlign = TextAlign.Center
            )
        }
    }

    if (uiResult.isSettingVisible) {
        SettingBottomSheet(
            onAction = { action -> component.emitAction(action) },
            onDismissRequest = { component.emitAction(MainAction.SettingsAction.HideSettings) },
            settingsUiResult = uiResult.settingsUiResult
        )
    }

    if (uiResult.isAddMeasurementVisible) {
        MeasurementBottomSheet(
            measurementUiResult = uiResult.measurementUiResult,
            onAction = { action -> component.emitAction(action) },
            onDismissRequest = { component.emitAction(MainAction.AddMeasurementAction.HideAddMeasurement) }
        )
    }

    if (uiResult.isDetailedStatisticVisible) {
        DetailedStatisticBottomSheet(
            uiResult = uiResult.detailedStatisticUiResult,
            onAction = { component.emitAction(it) },
            onDismissRequest = { component.emitAction(MainAction.DetailedStatisticAction.HideDetailedStatistic) }
        )
    }
}

@Composable
private fun TopContentBlock(onAction: (MainAction) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        IconButton(onClick = { onAction(MainAction.MainScreenAction.ShowSettings) }) {
            Icon(
                painter = painterResource(Res.drawable.ic_settings),
                contentDescription = stringResource(Res.string.settings_title),
                tint = AcTokens.IconPrimary.getThemedColor()
            )
        }
    }
}

@Composable
private fun StatisticBlock(onAction: (MainAction) -> Unit) {
    Module(outPaddingValues = PaddingValues(vertical = 4.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Footnote1(
                text = stringResource(Res.string.statistic_title),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .padding(vertical = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmered()
            )

            AcButton(
                text = stringResource(Res.string.statistic_show),
                style = AcButtonStyle.Transparent,
                verticalPadding = 0.dp,
                onClick = { onAction(MainAction.MainScreenAction.ShowDetailedStatistic) },
            )
        }
    }
}

@Composable
private fun MeasurementBlock(
    lastSavedWeight: WeightRecord?,
    onAction: (MainAction) -> Unit
) {
    Module(outPaddingValues = PaddingValues(vertical = 4.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Footnote1(
                text = stringResource(
                    Res.string.main_last_saved_record,
                    lastSavedWeight?.date.toFormattedString()
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                val displayWeight = lastSavedWeight?.weight?.toString() ?: "---.-"

                Text(
                    text = displayWeight,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = AcTokens.TextPrimary.getThemedColor(),
                        fontSize = 48.sp,
                        lineHeight = 48.sp,
                        letterSpacing = (-0.85).sp,
                        fontWeight = FontWeight.Normal
                    ),
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(Res.string.main_weight_unit),
                     modifier = Modifier.padding(bottom = 6.dp),
                    style = TextStyle(
                        color = AcTokens.TextSecondary.getThemedColor(),
                        fontSize = 24.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Normal
                    )
                )

            }

            AcButton(
                text = stringResource(Res.string.main_add_new_measurement),
                style = AcButtonStyle.Standard,
                verticalPadding = 0.dp,
                onClick = { onAction(MainAction.MainScreenAction.ShowAddMeasurement) },
            )
        }
    }
}
