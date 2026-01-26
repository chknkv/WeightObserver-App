////package com.chknkv.feature.main.presentation
////
////import com.arkivanov.decompose.ComponentContext
////import com.chknkv.coresession.SessionRepository
////import com.chknkv.coresession.WeightRecord
////import com.chknkv.coresession.WeightRepository
////import com.chknkv.coreutils.getCurrentDate
////import kotlinx.coroutines.CoroutineScope
////import kotlinx.coroutines.Dispatchers
////import kotlinx.coroutines.SupervisorJob
////import kotlinx.coroutines.cancel
////import kotlinx.coroutines.flow.MutableStateFlow
////import kotlinx.coroutines.flow.StateFlow
////import kotlinx.coroutines.flow.asStateFlow
////import kotlinx.coroutines.launch
////import kotlinx.datetime.DateTimeUnit
////import kotlinx.datetime.minus
////import kotlin.math.round
////import kotlin.random.Random
////
/////**
//// * Root component for the main application feature.
//// *
//// * Responsible for the logic of the main screen available to an authorized user.
//// * Handles user actions, such as signing out.
//// */
////interface RootMainComponent {
////
////    val generatedData: StateFlow<WeightRecord?>
////    val savedWeights: StateFlow<List<WeightRecord>>
////
////    fun generateData()
////    fun saveData()
////
////    /**
////     * Called when the sign-out button is clicked.
////     *
////     * Initiates the session termination process and navigation to the welcome screen.
////     */
////    fun onSignOut()
////}
////
/////**
//// * Implementation of [RootMainComponent].
//// *
//// * @property componentContext Decompose component context for lifecycle management.
//// * @property sessionRepository Repository for managing user session (authorization flag, etc.).
//// * @property onSignOutRequested Callback notifying the parent component about the sign-out request.
//// */
////class RootMainComponentImpl(
////    componentContext: ComponentContext,
////    private val sessionRepository: SessionRepository,
////    private val weightRepository: WeightRepository,
////    private val onSignOutRequested: () -> Unit
////) : RootMainComponent, ComponentContext by componentContext {
////
////    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
////
////    private val _generatedData = MutableStateFlow<WeightRecord?>(null)
////    override val generatedData: StateFlow<WeightRecord?> = _generatedData.asStateFlow()
////
////    private val _savedWeights = MutableStateFlow<List<WeightRecord>>(emptyList())
////    override val savedWeights: StateFlow<List<WeightRecord>> = _savedWeights.asStateFlow()
////
////    init {
////        loadData()
////    }
////
////    private fun loadData() {
////        coroutineScope.launch {
////            val today = getCurrentDate()
////            // Load all weights, e.g. from 5 years ago to today
////            val startDate = today.minus(5, DateTimeUnit.YEAR)
////            _savedWeights.value = weightRepository.getWeights(startDate, today)
////        }
////    }
////
////    override fun generateData() {
////        val randomWeight = Random.nextDouble(50.0, 250.0)
////        val roundedWeight = round(randomWeight * 10) / 10.0
////        // Generate random date within last 30 days
////        val today = getCurrentDate()
////        val randomDays = Random.nextInt(0, 30)
////        val randomDate = today.minus(randomDays, DateTimeUnit.DAY)
////
////        _generatedData.value = WeightRecord(randomDate, roundedWeight)
////    }
////
////    override fun saveData() {
////        val data = _generatedData.value ?: return
////        coroutineScope.launch {
////            weightRepository.saveWeight(data.date, data.weight)
////            _generatedData.value = null
////            loadData()
////        }
////    }
////
////    /**
////     * Handles the sign-out request.
////     *
////     * Resets the authorization flag in [sessionRepository] and invokes [onSignOutRequested].
////     */
////    override fun onSignOut() {
////        coroutineScope.launch {
////            sessionRepository.clearAll()
////            onSignOutRequested()
////            coroutineScope.cancel()
////        }
////    }
////}
////
//
//package com.chknkv.feature.main.presentation
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.imePadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import com.chknkv.coredesignsystem.buttons.AcButton
//import com.chknkv.coredesignsystem.buttons.AcButtonStyle
//import com.chknkv.coredesignsystem.theming.AcTokens
//import com.chknkv.coredesignsystem.theming.getThemedColor
//import com.chknkv.coredesignsystem.typography.Body1
//import com.chknkv.coredesignsystem.typography.Footnote1
//import com.chknkv.coredesignsystem.typography.Title1
//
///**
// * UI screen for [RootMainComponent].
// *
// * @param component Instance of [RootMainComponent] controlling the logic of this screen.
// */
//@Composable
//fun RootMainScreen(component: RootMainComponent) {
//    val generatedData by component.generatedData.collectAsState()
//    val savedWeights by component.savedWeights.collectAsState()
//
//    Scaffold(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(AcTokens.Background0.getThemedColor())
//            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
//            .imePadding(),
//        contentColor = AcTokens.Background0.getThemedColor(),
//        containerColor = AcTokens.Background0.getThemedColor()
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier.padding(paddingValues),
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(4.dp)
//            ) {
//                AcButton(
//                    modifier = Modifier.weight(1f),
//                    text = "Создать",
//                    style = AcButtonStyle.Standard,
//                    onClick = { component.generateData() }
//                )
//
//                AcButton(
//                    modifier = Modifier.weight(1f),
//                    text = "Сохранить",
//                    style = AcButtonStyle.Standard,
//                    onClick = { component.saveData() },
//                    isEnabled = generatedData != null,
//                )
//
//                AcButton(
//                    modifier = Modifier.weight(1f),
//                    text = "Очистить",
//                    style = AcButtonStyle.TransparentNegative,
//                    onClick = { component.onSignOut() }
//                )
//            }
//
//            if (generatedData != null) {
//                Body1("Сгенерировано: ${generatedData?.date} - ${generatedData?.weight}")
//            }
//
//            Title1(
//                text = "Записи в БД:",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp),
//                textAlign = TextAlign.Center
//            )
//
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//            ) {
//                if (savedWeights.isEmpty()) {
//                    item {
//                        Body1(
//                            text = "Нет данных в системе",
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    }
//                } else {
//                    items(savedWeights) { record ->
//                        Footnote1(
//                            text = "${record.date}: ${record.weight}",
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 4.dp),
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
