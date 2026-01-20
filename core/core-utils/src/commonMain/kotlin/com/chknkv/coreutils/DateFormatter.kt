package com.chknkv.coreutils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

/**
 * Formats the [LocalDate] into a localized string.
 * Supports Russian and English languages.
 *
 * Example output:
 * - RU: "15 февраля 2026"
 * - EN: "15 February 2026"
 * - null: "--.--.----"
 */
fun LocalDate?.toFormattedString(): String {
    if (this == null) return "--.--.----"

    val languageCode = getSystemLanguageCode()
    val isRussian = languageCode.lowercase() == "ru"

    val monthName = if (isRussian) {
        when (this.month) {
            Month.JANUARY -> "января"
            Month.FEBRUARY -> "февраля"
            Month.MARCH -> "марта"
            Month.APRIL -> "апреля"
            Month.MAY -> "мая"
            Month.JUNE -> "июня"
            Month.JULY -> "июля"
            Month.AUGUST -> "августа"
            Month.SEPTEMBER -> "сентября"
            Month.OCTOBER -> "октября"
            Month.NOVEMBER -> "ноября"
            Month.DECEMBER -> "декабря"
        }
    } else {
        when (this.month) {
            Month.JANUARY -> "January"
            Month.FEBRUARY -> "February"
            Month.MARCH -> "March"
            Month.APRIL -> "April"
            Month.MAY -> "May"
            Month.JUNE -> "June"
            Month.JULY -> "July"
            Month.AUGUST -> "August"
            Month.SEPTEMBER -> "September"
            Month.OCTOBER -> "October"
            Month.NOVEMBER -> "November"
            Month.DECEMBER -> "December"
        }
    }

    return "${this.day} $monthName ${this.year}"
}
