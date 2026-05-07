package com.example.coreyield.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.coreyield.SmartNotificationHelper
import com.example.coreyield.data.model.Task
import com.example.coreyield.data.repository.Repository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DeadlineWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // 1. Ambil User ID dari Repository (asumsikan sudah login)
            val userId = Repository.currentUserId ?: return Result.failure()

            // 2. Query tugas yang belum selesai dan punya deadline
            val db = FirebaseFirestore.getInstance()
            val tasksSnapshot = db.collection("users")
                .document(userId)
                .collection("tasks")
                .whereEqualTo("isCompleted", false)
                .whereNotEqualTo("deadlineTimestamp", null)
                .orderBy("deadlineTimestamp", Query.Direction.ASCENDING)
                .get()
                .await()

            // 3. Cek deadline yang mendekat (kurang dari 24 jam)
            val now = System.currentTimeMillis()
            val twentyFourHoursFromNow = now + (24 * 60 * 60 * 1000) // 24 jam dalam millis

            val notificationHelper = SmartNotificationHelper(applicationContext)

            for (document in tasksSnapshot.documents) {
                val task = document.toObject(Task::class.java)?.copy(id = document.id)
                if (task != null && task.deadlineTimestamp != null) {
                    if (task.deadlineTimestamp!! <= twentyFourHoursFromNow && task.deadlineTimestamp!! > now) {
                        // Deadline dalam 24 jam ke depan
                        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
                        val deadlineStr = sdf.format(Date(task.deadlineTimestamp!!))

                        notificationHelper.showDeadlineReminder(task.description, deadlineStr)
                    }
                }
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}