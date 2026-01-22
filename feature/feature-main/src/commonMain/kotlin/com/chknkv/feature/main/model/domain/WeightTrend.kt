package com.chknkv.feature.main.model.domain

import androidx.compose.runtime.Stable
import com.chknkv.coresession.WeightRecord

/**
 * Represents the trend of the weight change compared to the previous measurement.
 */
enum class WeightTrend {
    /** Weight increased by more than 0.2 kg */
    UP,
    /** Weight decreased by more than 0.2 kg */
    DOWN,
    /** Weight changed by 0.2 kg or less */
    FLAT
}

/**
 * A wrapper around [WeightRecord] that includes the trend information.
 */
@Stable
data class WeightRecordWithTrend(
    val record: WeightRecord,
    val trend: WeightTrend
)
