package com.chknkv.coresession

import androidx.compose.runtime.Stable
import kotlinx.datetime.LocalDate

/**
 * Data class representing a single weight measurement.
 */
@Stable
data class WeightRecord(val date: LocalDate, val weight: Double)
