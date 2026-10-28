package com.planner.persian.util

import java.util.Calendar

data class JalaliDate(
    val year: Int,
    val month: Int,
    val day: Int,
    val monthName: String,
    val dayOfWeekName: String
) {
    /**
     * Formatted string e.g. "۷ آبان ۱۴۰۵"
     */
    fun formatDisplay(): String {
        val pDay = PersianNumberFormatter.format(day)
        val pYear = PersianNumberFormatter.format(year)
        return "$pDay $monthName $pYear"
    }

    /**
     * Full string e.g. "چهارشنبه، ۷ آبان ۱۴۰۵"
     */
    fun formatFull(): String {
        return "$dayOfWeekName، ${formatDisplay()}"
    }
}

object JalaliCalendarHelper {

    private val PERSIAN_MONTHS = arrayOf(
        "فروردین", "اردیبهشت", "خرداد",
        "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر",
        "دی", "بهمن", "اسفند"
    )

    private val PERSIAN_WEEK_DAYS = arrayOf(
        "شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنج‌شنبه", "جمعه"
    )

    /**
     * Converts a Gregorian date (year, month 1-12, day 1-31) to Jalali Date.
     */
    fun gregorianToJalali(gy: Int, gm: Int, gd: Int, dayOfWeekCalendar: Int): JalaliDate {
        val gDaysInMonth = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        val gy2 = if (gm > 2) (gy + 1) else gy
        var days = 355666 + (365 * gy) + ((gy2 + 3) / 4) - ((gy2 + 99) / 100) + ((gy2 + 399) / 400) + gd + gDaysInMonth[gm - 1]
        var jy = -1595 + (33 * (days / 12053))
        days %= 12053
        jy += 4 * (days / 1461)
        days %= 1461
        if (days > 365) {
            jy += (days - 1) / 365
            days = (days - 1) % 365
        }
        val jm = if (days < 186) 1 + (days / 31) else 7 + ((days - 186) / 30)
        val jd = 1 + if (days < 186) (days % 31) else ((days - 186) % 30)

        // Map Java Calendar day of week (SUNDAY=1, MONDAY=2, ..., SATURDAY=7) to Persian week
        // شنبه (Saturday=7), یکشنبه (Sunday=1), دوشنبه (Monday=2), etc.
        val persianDayIndex = when (dayOfWeekCalendar) {
            Calendar.SATURDAY -> 0
            Calendar.SUNDAY -> 1
            Calendar.MONDAY -> 2
            Calendar.TUESDAY -> 3
            Calendar.WEDNESDAY -> 4
            Calendar.THURSDAY -> 5
            Calendar.FRIDAY -> 6
            else -> 0
        }

        return JalaliDate(
            year = jy,
            month = jm,
            day = jd,
            monthName = PERSIAN_MONTHS[jm - 1],
            dayOfWeekName = PERSIAN_WEEK_DAYS[persianDayIndex]
        )
    }

    /**
     * Gets today's Jalali date from the current system calendar.
     */
    fun getToday(): JalaliDate {
        val cal = Calendar.getInstance()
        return gregorianToJalali(
            gy = cal.get(Calendar.YEAR),
            gm = cal.get(Calendar.MONTH) + 1,
            gd = cal.get(Calendar.DAY_OF_MONTH),
            dayOfWeekCalendar = cal.get(Calendar.DAY_OF_WEEK)
        )
    }

    /**
     * Converts a millisecond timestamp to Jalali Date.
     */
    fun fromEpochMillis(millis: Long): JalaliDate {
        val cal = Calendar.getInstance().apply { timeInMillis = millis }
        return gregorianToJalali(
            gy = cal.get(Calendar.YEAR),
            gm = cal.get(Calendar.MONTH) + 1,
            gd = cal.get(Calendar.DAY_OF_MONTH),
            dayOfWeekCalendar = cal.get(Calendar.DAY_OF_WEEK)
        )
    }

    /**
     * Formats hour and minute into Persian formatted time (e.g., "۱۸:۴۵").
     */
    fun formatTime(hour: Int, minute: Int): String {
        val hStr = hour.toString().padStart(2, '0')
        val mStr = minute.toString().padStart(2, '0')
        return PersianNumberFormatter.format("$hStr:$mStr")
    }

    /**
     * Returns an appropriate Persian greeting according to current hour.
     */
    fun getPersianGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> "صبح بخیر! روزت پر از انرژی و موفقیت"
            in 12..16 -> "ظهر و عصر بخیر! پرقدرت ادامه بده"
            in 17..22 -> "عصر و شب بخیر! وقت مرور دستاوردهای امروز"
            else -> "شب آرام و آرامی داشته باشی"
        }
    }
}
