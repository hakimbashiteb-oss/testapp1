package com.planner.persian.util

/**
 * Utility to convert Latin/English digits into Persian (Farsi) digits and format numbers.
 */
object PersianNumberFormatter {
    private val PERSIAN_DIGITS = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')

    fun format(input: String): String {
        val sb = StringBuilder()
        for (ch in input) {
            if (ch in '0'..'9') {
                sb.append(PERSIAN_DIGITS[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    fun format(number: Number): String {
        return format(number.toString())
    }

    fun toPersianPercentage(percentage: Int): String {
        return "${format(percentage)}٪"
    }
}
