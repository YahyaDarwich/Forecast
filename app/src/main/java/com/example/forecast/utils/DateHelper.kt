package com.example.forecast.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateHelper {
    companion object {
        fun isDateToday(dateString: String, pattern: String = "yyyy-MM-dd HH:mm:ss"): Boolean {
            val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            val date = dateFormat.parse(dateString)

            val calendarDate = Calendar.getInstance()
            calendarDate.time = date

            val calendar = Calendar.getInstance()

            return calendarDate.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR) &&
                    calendarDate.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
        }

        fun formatDate(
            dateString: String,
            inputPattern: String = "yyyy-MM-dd HH:mm:ss",
            outputPattern: String = "h a"
        ): String {
            val inputFormatter = SimpleDateFormat(inputPattern, Locale.getDefault())
            val outputFormatter = SimpleDateFormat(outputPattern, Locale.getDefault())

            val date = inputFormatter.parse(dateString)
            return date?.let { outputFormatter.format(it) } ?: ""
        }
    }
}