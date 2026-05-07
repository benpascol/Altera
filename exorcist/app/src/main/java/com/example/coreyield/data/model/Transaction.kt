package com.example.coreyield.data.model

import com.google.firebase.firestore.PropertyName

data class Transaction(
    var id: String = "",

    @get:PropertyName("amount") @set:PropertyName("amount")
    var amount: Long = 0,

    @get:PropertyName("category") @set:PropertyName("category")
    var category: String = "",

    @get:PropertyName("type") @set:PropertyName("type")
    var type: String = "Expense", // "Income" or "Expense"

    @get:PropertyName("note") @set:PropertyName("note")
    var note: String = "",

    @get:PropertyName("timestamp") @set:PropertyName("timestamp")
    var timestamp: Long = System.currentTimeMillis()
)