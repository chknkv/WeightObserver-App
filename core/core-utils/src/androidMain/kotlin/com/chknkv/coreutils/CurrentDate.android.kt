package com.chknkv.coreutils

import kotlinx.datetime.LocalDate
import java.util.Calendar

/**
 * Android implementation of [getCurrentDate] using [java.util.Calendar].
 *
 * @return The current date as a [LocalDate].
 */
actual fun getCurrentDate(): LocalDate {
    val calendar = Calendar.getInstance()
    return LocalDate(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH) + 1,
        day = calendar.get(Calendar.DAY_OF_MONTH)
    )
}
