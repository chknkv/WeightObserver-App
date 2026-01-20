package com.chknkv.feature.main.domain

import com.chknkv.coresession.SessionRepository
import com.chknkv.coresession.WeightRecord
import com.chknkv.coresession.WeightRepository
import com.chknkv.coreutils.getCurrentDate
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus

/**
 * Interactor for the Main Screen.
 * Encapsulates business logic and data access.
 */
interface MainScreenInteractor {

    /**
     * Saves a weight record to the repository.
     *
     * @param weight The weight value in kilograms.
     */
    suspend fun saveWeight(weight: Double)

    /**
     * Retrieves weight records with pagination.
     *
     * @param limit Maximum number of records to return.
     * @param offset Number of records to skip.
     * @return List of [WeightRecordWithTrend]s.
     */
    suspend fun getPaginatedWeights(limit: Int, offset: Int): List<WeightRecordWithTrend>

    /**
     * Retrieves the most recent weight record available.
     *
     * @return The last recorded [WeightRecord], or null if no records exist.
     */
    suspend fun getLastWeight(): WeightRecord?

    /**
     * Signs out the current user and clears the session data.
     */
    suspend fun signOut()
}

/**
 * Implementation of [MainScreenInteractor]
 */
class MainScreenInteractorImpl(
    private val weightRepository: WeightRepository,
    private val sessionRepository: SessionRepository
) : MainScreenInteractor {

    override suspend fun saveWeight(weight: Double) {
        val today = getCurrentDate()
        weightRepository.saveWeight(today, weight)
    }

    override suspend fun getPaginatedWeights(limit: Int, offset: Int): List<WeightRecordWithTrend> {
        val records = weightRepository.getAllWeights(limit.toLong() + 1, offset.toLong())
        val result = mutableListOf<WeightRecordWithTrend>()

        for (i in records.indices) {
            if (i >= limit) break

            val current = records[i]
            val previous = records.getOrNull(i + 1)

            val trend = if (previous == null) {
                WeightTrend.FLAT
            } else {
                val diff = current.weight - previous.weight
                when {
                    diff > 0.2 -> WeightTrend.UP
                    diff < -0.2 -> WeightTrend.DOWN
                    else -> WeightTrend.FLAT
                }
            }

            result.add(WeightRecordWithTrend(current, trend))
        }

        return result
    }

    override suspend fun getLastWeight(): WeightRecord? {
        val today = getCurrentDate()
        val startDate = today.minus(10, DateTimeUnit.YEAR)
        val weights = weightRepository.getWeights(startDate, today)
        return weights.lastOrNull()
    }

    override suspend fun signOut() {
        weightRepository.clearWeights()
        sessionRepository.clearAll()
    }
}
