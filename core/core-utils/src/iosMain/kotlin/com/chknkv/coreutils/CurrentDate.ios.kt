package com.chknkv.coreutils

import kotlinx.datetime.LocalDate
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitDay

/**
 * iOS implementation of [getCurrentDate] using [platform.Foundation.NSCalendar].
 *
 * @return The current date as a [LocalDate].
 */
actual fun getCurrentDate(): LocalDate {
    val date = NSDate()
    val calendar = NSCalendar.currentCalendar
    val components = calendar.components(
        unitFlags = NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
        fromDate = date
    )
    
    return LocalDate(
        year = components.year.toInt(),
        month = components.month.toInt(),
        day = components.day.toInt()
    )
}
