package com.example.coreyield.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.coreyield.ui.finance.FinanceScreen
import com.example.coreyield.ui.home.HomeScreen
import com.example.coreyield.ui.profile.ProfileScreen
import com.example.coreyield.ui.tasks.TaskScreen

@Composable
fun MainAppScreen(
    onLogout: () -> Unit,
    onProfileClick: () -> Unit,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Home") }

    Scaffold(
        bottomBar = {
            NavigationBar {
                // HANYA 3 ITEM: Home, Tasks, Finance
                val items = listOf("Home", "Tasks", "Finance")
                val icons = listOf(Icons.Default.Home, Icons.Default.CheckCircle, Icons.Default.AttachMoney)
                val outlinedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.CheckCircle, Icons.Outlined.AttachMoney)

                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (selectedTab == item) icons[index] else outlinedIcons[index],
                                contentDescription = item
                            )
                        },
                        label = { Text(item) },
                        selected = selectedTab == item,
                        onClick = { selectedTab = item }
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                "Home" -> {
                    HomeScreen(
                        onNavigateToTask = { selectedTab = "Tasks" },
                        onNavigateToFinance = { selectedTab = "Finance" },
                        // Navigasi ke Profile hanya lewat sini (Foto di Home)
                        onNavigateToProfile = { selectedTab = "Profile" },
                        onSettingsClick = { },
                        onLogout = onLogout,
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = onThemeToggle
                    )
                }
                "Tasks" -> {
                    TaskScreen(
                        onNavigateBack = { selectedTab = "Home" }
                    )
                }
                "Finance" -> {
                    FinanceScreen(
                        onNavigateBack = { selectedTab = "Home" }
                    )
                }
                "Profile" -> {
                    // Layar Profile tetap ada, tapi tidak muncul di Bottom Bar
                    ProfileScreen(
                        onNavigateBack = { selectedTab = "Home" }
                    )
                }
            }
        }
    }
}