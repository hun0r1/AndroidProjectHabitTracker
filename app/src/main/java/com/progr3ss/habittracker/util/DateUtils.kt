package com.progr3ss.habittracker.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private const val DATE_FORMAT = "yyyy-MM-dd"
    private const val TIME_FORMAT = "HH:mm"
    
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(Date())
    }
    
    fun formatDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }
    
    fun formatTime(hour: Int, minute: Int): String {
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
    }
}
