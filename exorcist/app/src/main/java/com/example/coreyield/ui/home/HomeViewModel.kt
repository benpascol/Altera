package com.example.coreyield.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coreyield.data.model.Task
import com.example.coreyield.data.model.Transaction
import com.example.coreyield.data.model.UserProfile
import com.example.coreyield.data.remote.FirebaseManager
import com.example.coreyield.data.repository.Repository
import com.example.coreyield.util.MLManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar

// State Data
data class HomeDataState(
    val userName: String = "User",
    val currentPersona: String = "Analyzing...",
    val currentLevel: Int = 1,
    val currentXP: Int = 0,
    val maxXP: Int = 100,

    val totalBalance: Long = 0L,
    val spendableBalance: Long = 0L,
    val emergencyFund: Long = 0L,
    val dailyLimit: Long = 0L,
    val expenseToday: Long = 0L,
    val predictedExpense: Long = 0L,

    val recentTasks: List<Task> = emptyList(),
    val totalTaskCount: Int = 0,
    val stressAdvice: String = "No tasks.",

    val recentTransactions: List<Transaction> = emptyList(),
    val dailyQuote: String = "Stay productive!"
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val mlManager = MLManager(application)

    private val _homeState = MutableStateFlow(HomeDataState())
    val homeState: StateFlow<HomeDataState> = _homeState

    private val quotes = listOf(
        "Small steps every day.",
        "Focus on progress, not perfection.",
        "Discipline is key.",
        "Save first, spend later.",
        "Your future is created by what you do today."
    )

    fun refreshData() {
        val uid = Repository.currentUserId ?: return

        viewModelScope.launch {
            try {
                val userDoc = FirebaseManager.db.collection("users").document(uid).get().await()
                val profile = userDoc.toObject(UserProfile::class.java)

                val transSnapshot = FirebaseManager.db.collection("users").document(uid)
                    .collection("transactions")
                    .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .get().await()

                val transactions = transSnapshot.toObjects(Transaction::class.java)

                val totalIncome = transactions.filter { it.type == "Income" }.sumOf { it.amount }
                val totalExpense = transactions.filter { it.type == "Expense" }.sumOf { it.amount }
                val realBalance = totalIncome - totalExpense

                val incomeFloat = if (totalIncome > 0) totalIncome.toFloat() else 1f
                val savingsRate = (totalIncome - totalExpense).toFloat() / incomeFloat

                val aiPersona = mlManager.determinePersona(
                    income = totalIncome.toFloat(),
                    expense = totalExpense.toFloat(),
                    savingsRate = savingsRate
                )

                val aiPredictedExpense = if (totalExpense > 0) {
                    mlManager.predictExpense(
                        income = totalIncome.toFloat(),
                        expense = totalExpense.toFloat(),
                        balance = realBalance.toFloat()
                    )
                } else {
                    0f
                }

                val startOfDay = getStartOfDay()
                val expenseToday = transactions
                    .filter { it.type == "Expense" && it.timestamp >= startOfDay }
                    .sumOf { it.amount }

                val emergencyFund = (realBalance * 0.10).toLong()
                val spendableBalance = realBalance - emergencyFund
                val dailyLimit = if (spendableBalance > 0) spendableBalance / 30 else 0L

                // 3. AMBIL DATA TUGAS (Stress AI)
                FirebaseManager.db.collection("users").document(uid)
                    .collection("tasks")
                    .addSnapshotListener { snapshot, _ ->
                        if (snapshot != null) {
                            val allTasks = snapshot.toObjects(Task::class.java)

                            // --- MIGRASI DATA LAMA DI HOME ---
                            val tasksWithFixedTitle = allTasks.mapIndexed { index, task ->
                                task.id = snapshot.documents[index].id

                                if (task.title.isBlank() && task.description.isNotBlank()) {
                                    task.title = task.description.trim()
                                }

                                task
                            }

                            val activeTasks = tasksWithFixedTitle.filter { !it.isCompleted }

                            val priorityTasks = activeTasks
                                .filter { it.deadlineTimestamp != null }
                                .sortedBy { it.deadlineTimestamp }
                                .take(3)

                            var totalStressScore = 0f
                            val now = System.currentTimeMillis()
                            val oneDay = 86400000f

                            activeTasks.forEach { task ->
                                val deadline =
                                    task.deadlineTimestamp ?: (now + (30 * oneDay).toLong())
                                val rawDays = (deadline - now) / oneDay
                                val daysLeft = rawDays.coerceIn(0f, 30f)

                                val taskStress = mlManager.predictStressScore(
                                    difficulty = task.difficultyScore,
                                    daysUntilDeadline = daysLeft,
                                    taskCount = activeTasks.size
                                )
                                totalStressScore += taskStress
                            }

                            val finalScore = totalStressScore.toInt()

                            val adviceText = when {
                                finalScore >= 150 -> "DANGER: High Load! (Score: $finalScore) 🚨"
                                finalScore >= 50 -> "Warning: Workload Increasing. (Score: $finalScore) ⚠️"
                                activeTasks.isEmpty() -> "No tasks. Enjoy! 🌿"
                                else -> "Optimal. On Track. (Score: $finalScore) ✅"
                            }

                            val finalDisplayTasks =
                                if (priorityTasks.isEmpty() && activeTasks.isNotEmpty()) {
                                    activeTasks.take(3)
                                } else {
                                    priorityTasks
                                }

                            _homeState.value = HomeDataState(
                                userName = profile?.fullName ?: "User",
                                currentPersona = aiPersona,
                                currentLevel = profile?.level ?: 1,
                                currentXP = profile?.currentXP ?: 0,
                                maxXP = profile?.maxXP ?: 100,

                                totalBalance = realBalance,
                                spendableBalance = spendableBalance,
                                emergencyFund = emergencyFund,
                                dailyLimit = dailyLimit,
                                expenseToday = expenseToday,
                                predictedExpense = aiPredictedExpense.toLong(),

                                recentTasks = finalDisplayTasks,
                                totalTaskCount = activeTasks.size,
                                stressAdvice = adviceText,

                                recentTransactions = transactions.take(5),
                                dailyQuote = quotes.random()
                            )
                        }
                    }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getStartOfDay(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }


    fun clearData() {
        _homeState.value = HomeDataState() // Kembali ke state default (kosong/nol)
    }
}