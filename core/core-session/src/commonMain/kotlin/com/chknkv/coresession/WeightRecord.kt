package com.chknkv.coresession

import kotlinx.datetime.LocalDate

/**
 * Data class representing a single weight measurement.
 */
data class WeightRecord(
    val date: LocalDate,
    val weight: Double
)
