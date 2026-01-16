package com.chknkv.feature.main.presentation

import com.arkivanov.decompose.ComponentContext
import com.chknkv.coresession.SessionRepository
import com.chknkv.coresession.WeightRecord
import com.chknkv.coresession.WeightRepository
import com.chknkv.coreutils.getCurrentDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlin.math.round
import kotlin.random.Random

/**
 * Root component for the main application feature.
 *
 * Responsible for the logic of the main screen available to an authorized user.
 * Handles user actions, such as signing out.
 */
interface RootMainComponent {

    val generatedData: StateFlow<WeightRecord?>
    val savedWeights: StateFlow<List<WeightRecord>>

    fun generateData()
    fun saveData()

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
    private val weightRepository: WeightRepository,
    private val onSignOutRequested: () -> Unit
) : RootMainComponent, ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _generatedData = MutableStateFlow<WeightRecord?>(null)
    override val generatedData: StateFlow<WeightRecord?> = _generatedData.asStateFlow()

    private val _savedWeights = MutableStateFlow<List<WeightRecord>>(emptyList())
    override val savedWeights: StateFlow<List<WeightRecord>> = _savedWeights.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        coroutineScope.launch {
            val today = getCurrentDate()
            // Load all weights, e.g. from 5 years ago to today
            val startDate = today.minus(5, DateTimeUnit.YEAR)
            _savedWeights.value = weightRepository.getWeights(startDate, today)
        }
    }

    override fun generateData() {
        val randomWeight = Random.nextDouble(50.0, 250.0)
        val roundedWeight = round(randomWeight * 10) / 10.0
        // Generate random date within last 30 days
        val today = getCurrentDate()
        val randomDays = Random.nextInt(0, 30)
        val randomDate = today.minus(randomDays, DateTimeUnit.DAY)
        
        _generatedData.value = WeightRecord(randomDate, roundedWeight)
    }

    override fun saveData() {
        val data = _generatedData.value ?: return
        coroutineScope.launch {
            weightRepository.saveWeight(data.date, data.weight)
            _generatedData.value = null
            loadData()
        }
    }

    /**
     * Handles the sign-out request.
     *
     * Resets the authorization flag in [sessionRepository] and invokes [onSignOutRequested].
     */
    override fun onSignOut() {
        coroutineScope.launch {
            sessionRepository.clearAll()
            onSignOutRequested()
            coroutineScope.cancel()
        }
    }
}

