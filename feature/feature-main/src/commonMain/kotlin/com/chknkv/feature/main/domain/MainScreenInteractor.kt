package com.chknkv.feature.main.domain

import com.chknkv.coresession.SessionRepository
import com.chknkv.coresession.WeightRecord
import com.chknkv.coresession.WeightRepository
import com.chknkv.coreauthentication.domain.PasscodeRepository
import com.chknkv.coreutils.getCurrentDate
import com.chknkv.feature.main.model.domain.ChartData
import com.chknkv.feature.main.model.domain.WeightRecordWithTrend
import com.chknkv.feature.main.model.domain.WeightTrend
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
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

    /**
     * Checks if a passcode is set.
     */
    suspend fun hasPasscode(): Boolean

    /**
     * Checks if the entered passcode matches the stored one.
     */
    suspend fun checkPasscode(passcode: String): Boolean

    /**
     * Saves or removes the passcode.
     */
    suspend fun savePasscode(passcode: String?)

    /**
     * Deletes a weight record for a specific date.
     *
     * @param date The date of the measurement to delete.
     */
    suspend fun deleteWeight(date: LocalDate)

    /**
     * Returns chart data for the chart (Y = weight, X = date).
     * Loads 31 days: today-31..today. X axis is fixed; missing days have no point,
     * line connects consecutive measurements (no "empty day" on scale).
     * [ChartData.points] sorted by date ascending; [ChartData.lineTrend] from last vs previous (≥0.2 kg).
     */
    suspend fun getLast30DaysChartData(): ChartData

    /**
     * Returns chart data for the given 30-day window.
     * Window 0: [today-30, today]; window k≥1: [today-30(k+1), today-30k-1].
     */
    suspend fun getChartDataForWindow(windowIndex: Int): ChartData

    /**
     * True iff the next window (further past) has at least one weight record.
     * Used to enable/disable "swipe right" (older data).
     */
    suspend fun hasOlderWindow(windowIndex: Int): Boolean
}

/**
 * Implementation of [MainScreenInteractor]
 */
class MainScreenInteractorImpl(
    private val weightRepository: WeightRepository,
    private val sessionRepository: SessionRepository,
    private val passcodeRepository: PasscodeRepository
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

    override suspend fun hasPasscode(): Boolean {
        return passcodeRepository.getPasscodeHash() != null
    }

    override suspend fun checkPasscode(passcode: String): Boolean {
        return passcodeRepository.getPasscodeHash() == passcode
    }

    override suspend fun savePasscode(passcode: String?) {
        passcodeRepository.savePasscodeHash(passcode)
    }

    override suspend fun deleteWeight(date: LocalDate) {
        weightRepository.deleteWeight(date)
    }

    override suspend fun getLast30DaysChartData(): ChartData =
        getChartDataForWindow(0)

    override suspend fun getChartDataForWindow(windowIndex: Int): ChartData {
        val today = getCurrentDate()
        val (startDate, endDate) = windowRange(windowIndex, today)
        val points = weightRepository.getWeights(startDate, endDate)
        val lineTrend = when {
            points.size < 2 -> WeightTrend.FLAT
            else -> {
                val last = points.last().weight
                val prev = points[points.size - 2].weight
                val diff = last - prev
                when {
                    diff > 0.2 -> WeightTrend.UP
                    diff < -0.2 -> WeightTrend.DOWN
                    else -> WeightTrend.FLAT
                }
            }
        }
        return ChartData(
            points = points,
            lineTrend = lineTrend,
            startDate = startDate,
            endDate = endDate
        )
    }

    override suspend fun hasOlderWindow(windowIndex: Int): Boolean {
        val today = getCurrentDate()
        val (start, end) = windowRange(windowIndex + 1, today)
        return weightRepository.getWeights(start, end).isNotEmpty()
    }

    private fun windowRange(windowIndex: Int, today: LocalDate): Pair<LocalDate, LocalDate> {
        return if (windowIndex == 0) {
            today.minus(30, DateTimeUnit.DAY) to today
        } else {
            val start = today.minus(30 * (windowIndex + 1), DateTimeUnit.DAY)
            val end = today.minus(30 * windowIndex + 1, DateTimeUnit.DAY)
            start to end
        }
    }
}
