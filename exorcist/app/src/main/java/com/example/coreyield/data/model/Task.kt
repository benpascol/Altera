package com.example.coreyield.data.model

import com.google.firebase.firestore.PropertyName

data class Task(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var category: String = "General",

    @get:PropertyName("isCompleted") @set:PropertyName("isCompleted")
    var isCompleted: Boolean = false,

    var timestamp: Long = System.currentTimeMillis(), // waktu pembuatan (otomatis)
    var inputTimestamp: Long = System.currentTimeMillis(), // waktu input (bisa diubah user)

    var difficultyScore: Int = 1,
    var deadlineTimestamp: Long? = null // opsional
) {
    constructor() : this("", "", "", "General", false, System.currentTimeMillis(), System.currentTimeMillis(), 1, null)
}