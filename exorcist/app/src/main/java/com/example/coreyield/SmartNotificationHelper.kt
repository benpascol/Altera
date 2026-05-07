package com.example.coreyield

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class SmartNotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "coreyield_smart_channel"
        const val CHANNEL_NAME = "CoreYield Assistant"
        
        const val NOTIF_ID_BURNOUT = 101
        const val NOTIF_ID_BUDGET = 102
        const val NOTIF_ID_TASK_DEADLINE = 103
        
        // --- COOLDOWN CONSTANTS ---
        private const val PREFS_NAME = "coreyield_notif_prefs"
        private const val KEY_LAST_BURNOUT = "last_burnout_time"
        private const val COOLDOWN_PERIOD = 6 * 60 * 60 * 1000L // 6 Hours (in milliseconds)
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Smart notifications from AI CoreYield"
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun hasPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    // 1. Notifikasi Burnout (DENGAN COOLDOWN)
    @SuppressLint("MissingPermission")
    fun showBurnoutWarning() {
        if (!hasPermission()) return

        // --- CEK COOLDOWN ---
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastTime = prefs.getLong(KEY_LAST_BURNOUT, 0L)
        val now = System.currentTimeMillis()

        // Jika belum 6 jam sejak notifikasi terakhir, JANGAN tampilkan (Return)
        if (now - lastTime < COOLDOWN_PERIOD) {
            android.util.Log.d("NOTIF_DEBUG", "Notification Burnout has been postpone (Cooldown activate)")
            return
        }

        // Tampilkan Notifikasi
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("⚠️ Beware of Burnout!")
            .setContentText("Your task load is very high (>150). Take a break!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        
        NotificationManagerCompat.from(context).notify(NOTIF_ID_BURNOUT, builder.build())

        // --- SIMPAN WAKTU SEKARANG ---
        prefs.edit().putLong(KEY_LAST_BURNOUT, now).apply()
        android.util.Log.d("NOTIF_DEBUG", "Notification Burnout Shown & Cooldown Deactivated")
    }

    // 2. Notifikasi Keuangan
    @SuppressLint("MissingPermission")
    fun showOverBudgetWarning(predictedAmount: String) {
        if (!hasPermission()) return
        
        // (Opsional: Anda bisa tambahkan cooldown serupa di sini jika mau)
        
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setContentTitle("💸 Warning: Over Budget")
            .setContentText("AI predicts your expenses will reach $predictedAmount this month.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        NotificationManagerCompat.from(context).notify(NOTIF_ID_BUDGET, builder.build())
    }

    // 3. Notifikasi Deadline Tugas
    @SuppressLint("MissingPermission")
    fun showDeadlineReminder(taskTitle: String, deadlineTime: String) {
        if (!hasPermission()) return
        
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("⏳ Deadline Approaching!")
            .setContentText("Task '$taskTitle' deadline is $deadlineTime. Start working on it now!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val uniqueId = (System.currentTimeMillis() % 10000).toInt()
        NotificationManagerCompat.from(context).notify(uniqueId, builder.build())
    }
}