package com.example.coreyield.ui.finance

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coreyield.data.model.Transaction
import com.example.coreyield.data.remote.FirebaseManager
import com.example.coreyield.data.repository.Repository
import com.example.coreyield.util.MLManager
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// State UI Finance
data class FinanceState(
    val transactions: List<Transaction> = emptyList(),
    val totalBalance: Long = 0,
    val totalIncome: Long = 0,
    val totalExpense: Long = 0,
    val predictedExpense: Long = 0, // Hasil Prediksi AI
    val isLoading: Boolean = false
)

class FinanceViewModel(application: Application) : AndroidViewModel(application) {

    private val mlManager = MLManager(application) // Init AI

    // State utama
    private val _financeState = MutableStateFlow(FinanceState())
    val financeState: StateFlow<FinanceState> = _financeState

    // Helper properties untuk compatibility dengan UI lama
    val transactions: StateFlow<List<Transaction>>
        get() = MutableStateFlow(_financeState.value.transactions)
    val totalBalance: StateFlow<Long>
        get() = MutableStateFlow(_financeState.value.totalBalance)
    val isLoading: StateFlow<Boolean>
        get() = MutableStateFlow(_financeState.value.isLoading)

    init {
        loadTransactions()
    }

    fun loadTransactions() {
        val uid = Repository.currentUserId
        if (uid == null) return

        _financeState.value = _financeState.value.copy(isLoading = true)

        FirebaseManager.db.collection("users").document(uid)
            .collection("transactions")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    _financeState.value = _financeState.value.copy(isLoading = false)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list = snapshot.toObjects(Transaction::class.java)

                    // Mapping ID
                    val listWithIds = list.mapIndexed { index, transaction ->
                        transaction.id = snapshot.documents[index].id
                        transaction
                    }

                    // 1. HITUNG DATA KEUANGAN
                    val income = listWithIds.filter { it.type == "Income" }.sumOf { it.amount }
                    val expense = listWithIds.filter { it.type == "Expense" }.sumOf { it.amount }
                    val balance = income - expense

                    // 2. PANGGIL AI FINANCE (DENGAN SAFETY CHECK)
                    viewModelScope.launch {
                        var predicted = 0f

                        // Hanya panggil AI jika sudah ada data Expense
                        if (expense > 0) {
                            predicted = mlManager.predictExpense(
                                income = income.toFloat(),
                                expense = expense.toFloat(),
                                balance = balance.toFloat()
                            )
                        }

                        // 3. UPDATE STATE
                        _financeState.value = FinanceState(
                            transactions = listWithIds,
                            totalBalance = balance,
                            totalIncome = income,
                            totalExpense = expense,
                            predictedExpense = predicted.toLong(), // Simpan hasil prediksi
                            isLoading = false
                        )
                    }
                }
            }
    }

    // Fungsi CRUD (Create, Update, Delete) tetap sama
    fun addTransaction(amount: Long, type: String, category: String, note: String, timestamp: Long) {
        val uid = Repository.currentUserId ?: return
        val newTransaction = Transaction(id = "", amount = amount, type = type, category = category, note = note, timestamp = timestamp)
        FirebaseManager.db.collection("users").document(uid).collection("transactions").add(newTransaction)
    }

    fun updateTransaction(transaction: Transaction, amount: Long, type: String, category: String, note: String) {
        val uid = Repository.currentUserId ?: return
        val updates = mapOf("amount" to amount, "type" to type, "category" to category, "note" to note)
        FirebaseManager.db.collection("users").document(uid).collection("transactions").document(transaction.id).update(updates)
    }

    fun deleteTransaction(transactionId: String) {
        val uid = Repository.currentUserId ?: return
        FirebaseManager.db.collection("users").document(uid).collection("transactions").document(transactionId).delete()
    }
}