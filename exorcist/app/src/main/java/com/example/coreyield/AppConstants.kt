package com.example.coreyield

object AppConstants {

    // === KEUANGAN (WAJIB ADA — dipakai di FinanceScreen.kt) ===
    val FINANCE_CATEGORIES = listOf(
        "Food",
        "Transportation",
        "Entertainment",
        "Shopping",
        "Health",
        "Education",
        "Utilities",
        "Gifts",
        "Invoice",
        "Others"
    )

    // === TUGAS (Opsional — untuk konsistensi) ===
    val TASK_CATEGORIES = listOf(
        "Work",
        "Personal",
        "Study",
        "Shopping",
        "Health",
        "Education",
        "Others"
    )

    // === GAMIFIKASI ===
    const val MAX_XP_PER_LEVEL = 100
    const val XP_BONUS_BASE = 5
    const val XP_MULTIPLIER_PER_DIFFICULTY = 10

    // === FIRESTORE PATHS ===
    const val USERS_COLLECTION = "users"
    const val TASKS_SUBCOLLECTION = "tasks"
    const val TRANSACTIONS_SUBCOLLECTION = "transactions"

    // === UI ===
    const val SPLASH_DELAY_MS = 2000L

    // === ML NORMALIZATION ===
    const val MAX_EXPENSE = 64997.0f
    const val MAX_BALANCE = 14213582.0f
    const val MAX_TASK_COUNT = 20.0f
    const val MAX_DURATION = 8.0f
    const val MAX_SLEEP = 24.0f
    const val MAX_DAILY_EXPENSE = 249647.0f

    // Nama File Penyimpanan Lokal (SharedPreferences)
    const val PREF_NAME = "CoreYieldPrefs"

    // Keys untuk Data Pengguna (Agar tersimpan)
    const val KEY_IS_DARK_THEME = "is_dark_theme"
    const val KEY_USER_TOKEN = "user_token"
    const val KEY_USER_NAME = "user_name"
    const val KEY_IS_LOGGED_IN = "is_logged_in"

    // Konstanta Lain (Opsional, sesuaikan dengan kebutuhan Anda)
    const val MIN_PASSWORD_LENGTH = 6
    const val BASE_URL = "https://api.coreyield.example.com/" // Jika ada API lain


}