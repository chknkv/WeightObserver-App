package com.chknkv.coreutils

import kotlinx.datetime.LocalDate

/**
 * Returns the current date in the local time zone.
 *
 * @return The current date as a [LocalDate].
 */
expect fun getCurrentDate(): LocalDate
