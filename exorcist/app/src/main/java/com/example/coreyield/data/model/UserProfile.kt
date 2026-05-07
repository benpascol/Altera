package com.example.coreyield.data.model

import com.google.firebase.firestore.DocumentId

data class UserProfile(
    @DocumentId
    val uid: String = "",
    val email: String = "",
    val fullName: String = "User",
    val monthlyIncome: Long = 0L,
    val savingsTargetPercent: Int = 20,

    // Pastikan nama variabel ini SAMA PERSIS dengan yang dipanggil di Screen
    val activeStartHour: Int = 9,
    val activeEndHour: Int = 17,

    val level: Int = 1,
    val currentXP: Int = 0,
    val maxXP: Int = 100,
    val persona: String = "Newcomer"
)