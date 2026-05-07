package com.example.coreyield.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.coreyield.SmartNotificationHelper
import com.example.coreyield.data.model.Task
import com.example.coreyield.data.remote.FirebaseManager
import com.example.coreyield.data.repository.Repository
import com.example.coreyield.util.MLManager
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    // Gunakan MLManager agar logika skor SAMA PERSIS dengan Home Screen
    private val mlManager = MLManager(context)

    override suspend fun doWork(): Result {
        val uid = Repository.currentUserId ?: return Result.success()

        try {
            // 1. Ambil Data Tugas
            val snapshot = FirebaseManager.db.collection("users").document(uid)
                .collection("tasks")
                .whereEqualTo("isCompleted", false)
                .get().await()

            val tasks = snapshot.toObjects(Task::class.java)
            if (tasks.isEmpty()) return Result.success()

            val helper = SmartNotificationHelper(applicationContext)
            val now = System.currentTimeMillis()
            val oneDay = 86400000f

            // 2. HITUNG SKOR STRESS (Logic sama dengan HomeViewModel)
            var totalScore = 0f
            var hasUrgentTask = false
            var urgentTaskTitle = ""

            tasks.forEach { task ->
                val deadline = task.deadlineTimestamp ?: (now + (30 * oneDay).toLong())
                val rawDays = (deadline - now) / oneDay
                val daysLeft = rawDays.coerceIn(0f, 30f) // Sanitasi Input

                // Cek Deadline Mepet (< 24 jam) untuk notifikasi spesifik
                if (daysLeft < 1.0) {
                    hasUrgentTask = true
                    urgentTaskTitle = task.description
                }

                // Hitung Score Pakai AI
                totalScore += mlManager.predictStressScore(
                    task.difficultyScore,
                    daysLeft,
                    tasks.size
                )
            }

            // 3. TRIGGER NOTIFIKASI BERDASARKAN HASIL

            // A. Notifikasi Deadline (Prioritas Utama)
            if (hasUrgentTask) {
                helper.showDeadlineReminder(urgentTaskTitle, "Today/Tomorrow")
            }

            // B. Notifikasi Burnout (Jika Skor > 150) - Sesuaikan threshold baru
            if (totalScore >= 150) {
                // Pastikan fungsi showBurnoutWarning di Helper Anda menerima parameter atau sesuaikan teksnya
                // Anda bisa memodifikasi Helper untuk menerima pesan custom skor
                helper.showBurnoutWarning()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }

        return Result.success()
    }
}