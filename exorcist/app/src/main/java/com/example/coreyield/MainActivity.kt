package com.example.coreyield

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coreyield.ui.MainAppScreen
import com.example.coreyield.ui.SplashScreen
import com.example.coreyield.ui.auth.LoginScreen
import com.example.coreyield.ui.auth.RegisterScreen
import com.example.coreyield.ui.finance.FinanceViewModel
import com.example.coreyield.ui.home.HomeViewModel
import com.example.coreyield.ui.tasks.TaskViewModel
import com.example.coreyield.ui.profile.ProfileViewModel
import com.example.coreyield.ui.theme.CoreYieldTheme
import kotlinx.coroutines.launch
import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy
import com.example.coreyield.data.repository.Repository
import java.util.concurrent.TimeUnit
import com.example.coreyield.worker.DeadlineWorker // (Sesuaikan jika ada di folder worker)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- UPDATE: MINTA IZIN DENGAN TEGAS (Android 13+) ---
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val permission = android.Manifest.permission.POST_NOTIFICATIONS
            if (checkSelfPermission(permission) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // Request code 101
                requestPermissions(arrayOf(permission), 101)
            }
        }
        // ----------------------------------------------------

        val sharedPref = getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE)

        // --- TAMBAHKAN INI: JALANKAN WORKER ---
        initWorker()

        setupWorker()
        // --------------------------------------

        setContent {
            // 1. STATE TEMA GLOBAL
            // Secara default mengikuti sistem, tapi bisa diubah user
            var isDarkTheme by remember { mutableStateOf(sharedPref.getBoolean(AppConstants.KEY_IS_DARK_THEME, false)) }
            // Tips: Jika ingin default ikut HP user, ganti false dengan isSystemInDarkTheme()

            // 2. PASANG TEMA KE APPS
            CoreYieldTheme(darkTheme = isDarkTheme) {

                // ViewModel setup ...
                val financeViewModel: FinanceViewModel = viewModel()
                val taskViewModel: TaskViewModel = viewModel()
                val homeViewModel: HomeViewModel = viewModel()
                val profileViewModel: ProfileViewModel = viewModel()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("SPLASH") }

                    when (currentScreen) {
                        "SPLASH" -> {
                            SplashScreen(onTimeout = {
                                // ... (Kode cek user login biarkan sama) ...
                                val user = Repository.getCurrentUser()
                                if (user != null) {
                                    lifecycleScope.launch {
                                        if(Repository.isUserValid()) {
                                            taskViewModel.loadTasks()
                                            financeViewModel.loadTransactions()
                                            profileViewModel.loadUserProfile()
                                            homeViewModel.refreshData()
                                            currentScreen = "MAIN_APP"
                                        } else {
                                            currentScreen = "LOGIN"
                                        }
                                    }
                                } else {
                                    currentScreen = "LOGIN"
                                }
                            })
                        }
                        "LOGIN" -> {
                            LoginScreen(
                                onLoginSuccess = {
                                    // ... (Kode login success biarkan sama) ...
                                    taskViewModel.loadTasks()
                                    financeViewModel.loadTransactions()
                                    profileViewModel.loadUserProfile()
                                    homeViewModel.refreshData()
                                    currentScreen = "MAIN_APP"
                                },
                                onNavigateToRegister = { currentScreen = "REGISTER" }
                            )
                        }
                        "REGISTER" -> {
                            RegisterScreen(
                                onRegisterSuccess = { currentScreen = "LOGIN" },
                                onNavigateBack = { currentScreen = "LOGIN" }
                            )
                        }
                        "MAIN_APP" -> {
                            // 3. KIRIM KONTROL TEMA KE MAIN APP
                            MainAppScreen(
                                onLogout = {
                                    Repository.logout()
                                    currentScreen = "LOGIN"
                                },
                                onProfileClick = { /* Tidak ada aksi */ },
                                isDarkTheme = isDarkTheme,               // <--- KIRIM STATE
                                onThemeToggle = { 
                                    val newState = !isDarkTheme
                                    isDarkTheme = newState
                            
                                    // Simpan agar tidak hilang saat aplikasi ditutup
                                    sharedPref.edit()
                                        .putBoolean(AppConstants.KEY_IS_DARK_THEME, newState)
                                        .apply() } 
                            )
                        }
                        "PROFILE" -> {
                            // (Biarkan sama)
                        }
                    }
                }
            }
        }
    }

    private fun setupWorker() {
        // Init Worker (Jalankan setiap 15 menit)
        val workRequest = androidx.work.PeriodicWorkRequestBuilder<com.example.coreyield.worker.NotificationWorker>(
            15, java.util.concurrent.TimeUnit.MINUTES
        ).build()

        androidx.work.WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "CoreYieldNotifWork",
            androidx.work.ExistingPeriodicWorkPolicy.UPDATE, // GANTI DARI KEEP KE UPDATE
            workRequest
        )

    }
    private fun initWorker() {
        val workRequest = androidx.work.PeriodicWorkRequestBuilder<com.example.coreyield.worker.NotificationWorker>(
            15, java.util.concurrent.TimeUnit.MINUTES
        ).build()

        androidx.work.WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "CoreYieldNotifWork",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP, // KEEP = Jangan reset timer jika app dibuka lagi
            workRequest
        )
    }
}