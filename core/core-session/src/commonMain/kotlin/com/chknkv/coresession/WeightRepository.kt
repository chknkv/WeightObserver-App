package com.chknkv.coresession

import com.chknkv.coresession.db.WeightDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

/**
 * Repository for managing weight measurements.
 *
 * Provides functionality to:
 * - Save a weight record for a specific date (overwriting if exists).
 * - Retrieve weight records within a date range.
 * - Clear all weight data.
 */
interface WeightRepository {
    /**
     * Saves a weight measurement for a specific date.
     * If a record already exists for this date, it is overwritten.
     *
     * @param date The date of the measurement.
     * @param weight The weight value (e.g. 85.5).
     */
    suspend fun saveWeight(date: LocalDate, weight: Double)

    /**
     * Retrieves weight records for a specified date range (inclusive).
     * The records are sorted by date ascending.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return List of [WeightRecord]s within the range.
     */
    suspend fun getWeights(startDate: LocalDate, endDate: LocalDate): List<WeightRecord>

    /**
     * Retrieves all weight records with pagination.
     * The records are sorted by date descending.
     *
     * @param limit The maximum number of records to return.
     * @param offset The number of records to skip.
     * @return List of [WeightRecord]s.
     */
    suspend fun getAllWeights(limit: Long, offset: Long): List<WeightRecord>

    /**
     * Clears all weight measurements from the database.
     */
    suspend fun clearWeights()
}

/**
 * Implementation of [WeightRepository]
 */
internal class WeightRepositoryImpl(database: WeightDatabase) : WeightRepository {

    private val queries = database.weightDatabaseQueries

    override suspend fun saveWeight(date: LocalDate, weight: Double): Unit = withContext(Dispatchers.IO) {
        queries.insertWeight(date.toString(), weight)
    }

    override suspend fun getWeights(startDate: LocalDate, endDate: LocalDate): List<WeightRecord> = withContext(Dispatchers.IO) {
        queries.selectWeightsByRange(
            startDate.toString(),
            endDate.toString()
        ).executeAsList().map {
            WeightRecord(
                date = LocalDate.parse(it.date),
                weight = it.weight
            )
        }
    }

    override suspend fun getAllWeights(limit: Long, offset: Long): List<WeightRecord> = withContext(Dispatchers.IO) {
        queries.selectAllWeights(limit, offset).executeAsList().map {
            WeightRecord(
                date = LocalDate.parse(it.date),
                weight = it.weight
            )
        }
    }

    override suspend fun clearWeights(): Unit = withContext(Dispatchers.IO) {
        queries.deleteAll()
    }
}
