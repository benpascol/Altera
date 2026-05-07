package com.example.coreyield.ui.tasks

import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.*

fun getHeaderDate(millis: Long, isDeadline: Boolean = false): String {
    if (isDeadline && millis == 0L) return "No Deadline"
    val input = Calendar.getInstance().apply { timeInMillis = millis }
    val today = Calendar.getInstance()
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
    return when {
        input.get(Calendar.YEAR) == today.get(Calendar.YEAR) && input.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) -> "Today"
        else -> sdf.format(input.time)
    }
}

fun getTaskCategoryColor(category: String): Color {
    return when (category.lowercase()) {
        "study", "belajar" -> Color(0xFF42A5F5)
        "work", "kerja" -> Color(0xFFEF5350)
        "hobby", "game", "hiburan" -> Color(0xFFAB47BC)
        "personal", "pacaran", "date" -> Color(0xFFEC407A)
        else -> Color(0xFF66BB6A)
    }
}