package com.example.coreyield.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coreyield.data.model.Task
import com.example.coreyield.data.model.Transaction
import com.example.coreyield.ui.tasks.getTaskCategoryColor
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToTask: () -> Unit,
    onNavigateToFinance: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogout: () -> Unit,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val homeState by viewModel.homeState.collectAsState()

    // State untuk Tutorial
    var showHomeTutorial by remember { mutableStateOf(false) }
    var showAiInfoDialog by remember { mutableStateOf(false) }

    // Logic Warna Stress Advice
    val adviceColor = when {
        homeState.stressAdvice.contains("DANGER", ignoreCase = true) -> MaterialTheme.colorScheme.error
        homeState.stressAdvice.contains("Warning", ignoreCase = true) -> Color(0xFFFFA000)
        else -> Color(0xFF4CAF50)
    }

    val adviceIcon = when {
        homeState.stressAdvice.contains("DANGER", ignoreCase = true) -> Icons.Default.Warning
        homeState.stressAdvice.contains("Warning", ignoreCase = true) -> Icons.Default.Info
        else -> Icons.Default.CheckCircle
    }

    LaunchedEffect(Unit) {
        viewModel.refreshData()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HeaderSection(
                name = homeState.userName,
                persona = homeState.currentPersona,
                level = homeState.currentLevel,
                onProfileClick = onNavigateToProfile,
                onLogout = {
                    viewModel.clearData() // 1. Bersihkan data layar Home dulu
                    onLogout()            // 2. Baru jalankan proses logout (pindah screen)
                },
                isDark = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onShowTutorial = { showHomeTutorial = true } // <-- Trigger Tutorial
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // QUOTE CARD
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("💡", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "\"${homeState.dailyQuote}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = Color(0xFF5D4037)
                    )
                }
            }

            // STRESS AI CARD
            if (homeState.totalTaskCount > 0) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { showAiInfoDialog = true },
                    colors = CardDefaults.cardColors(
                        containerColor = adviceColor.copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = adviceIcon,
                            contentDescription = null,
                            tint = adviceColor,
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Mental Health Analysis",
                                style = MaterialTheme.typography.labelMedium,
                                color = adviceColor.copy(alpha = 0.8f)
                            )
                            Text(
                                text = homeState.stressAdvice,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = adviceColor
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                WelcomeCard(homeState.userName)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // FINANCE SUMMARY CARD
            FinanceSummaryCard(
                balance = homeState.totalBalance,
                dailyLimit = homeState.dailyLimit,
                expenseToday = homeState.expenseToday,
                emergencyFund = homeState.emergencyFund,
                predictedExpense = homeState.predictedExpense,
                onSeeDetails = onNavigateToFinance
            )

            Spacer(modifier = Modifier.height(24.dp))

            // PRIORITY TASKS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Priority Tasks", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                TextButton(onClick = onNavigateToTask) { Text("See All") }
            }

            if (homeState.recentTasks.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                    Text("No tasks pending. Great job! 🎉", color = Color.Gray)
                }
            } else {
                homeState.recentTasks.forEach { task ->
                    HomeTaskItem(task = task)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // RECENT TRANSACTIONS
            Text("Recent Transactions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
            if (homeState.recentTransactions.isEmpty()) {
                Text("No transactions yet.", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp))
            } else {
                homeState.recentTransactions.forEach { trans ->
                    MiniTransactionItem(trans)
                }
            }

            // DIALOGS
            if (showAiInfoDialog) {
                AlertDialog(
                    onDismissRequest = { showAiInfoDialog = false },
                    icon = { Icon(Icons.Default.Psychology, contentDescription = null) },
                    title = { Text("CoreYield AI Logic") },
                    text = { Text("CoreYield uses AI to analyze your workload difficulty vs deadlines to predict burnout risk.") },
                    confirmButton = { TextButton(onClick = { showAiInfoDialog = false }) { Text("OK") } }
                )
            }

            if (showHomeTutorial) {
                HomeTutorialDialog(onDismiss = { showHomeTutorial = false })
            }
        }
    }
}

// --- TUTORIAL DIALOG ---
@Composable
fun HomeTutorialDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        // Menggunakan icon Lightbulb/Info
        icon = { Icon(Icons.Outlined.Lightbulb, null, tint = MaterialTheme.colorScheme.primary) },
        title = { Text("Dashboard Guide", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                TutorialItemRow(
                    icon = Icons.Default.Face,
                    title = "Your Persona & Level",
                    desc = "Your rank (e.g., 'Wealth Builder') updates automatically based on your savings rate & XP."
                )
                TutorialItemRow(
                    icon = Icons.Default.Psychology,
                    title = "Stress Analysis AI",
                    desc = "AI monitors your deadlines & difficulty load. If it says 'DANGER', try to complete some tasks!"
                )
                TutorialItemRow(
                    icon = Icons.Default.AccountBalanceWallet,
                    title = "Spendable Balance",
                    desc = "This is your 'Safe-to-Spend' money. We automatically set aside 10% for your Emergency Fund."
                )
                TutorialItemRow(
                    icon = Icons.Default.Speed,
                    title = "Daily Limit",
                    desc = "Try not to exceed this daily limit so your money lasts until the end of the month."
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text("Awesome!") }
        }
    )
}

// --- HEADER SECTION (Updated with Tutorial Menu) ---
@Composable
fun HeaderSection(
    name: String,
    persona: String,
    level: Int,
    onProfileClick: () -> Unit,
    onLogout: () -> Unit,
    isDark: Boolean,
    onThemeToggle: () -> Unit,
    onShowTutorial: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clickable { onProfileClick() }
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (name.isNotEmpty()) name.take(1).uppercase() else "?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Hello, $name", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("$persona • Lvl $level", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }

        Box {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                // New Tutorial Menu Item
                DropdownMenuItem(
                    text = { Text("App Guide") },
                    leadingIcon = { Icon(Icons.Outlined.HelpOutline, null) },
                    onClick = {
                        menuExpanded = false
                        onShowTutorial()
                    }
                )

                HorizontalDivider()

                DropdownMenuItem(
                    text = { Text(if (isDark) "Light Mode" else "Dark Mode") },
                    leadingIcon = { Icon(Icons.Default.Brightness4, null) },
                    onClick = {
                        menuExpanded = false
                        onThemeToggle()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Logout", color = Color.Red) },
                    leadingIcon = { Icon(Icons.Default.Logout, null, tint = Color.Red) },
                    onClick = {
                        menuExpanded = false
                        onLogout()
                    }
                )
            }
        }
    }
}

// --- COMPONENTS ---

@Composable
fun FinanceSummaryCard(
    balance: Long,
    dailyLimit: Long,
    expenseToday: Long,
    emergencyFund: Long,
    predictedExpense: Long,
    onSeeDetails: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Finance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                TextButton(onClick = onSeeDetails) {
                    Text("See All >", color = Color(0xFF64B5F6))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Spendable Balance", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(
                text = formatCurrency(balance),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Daily Limit: ${formatCurrency(dailyLimit)}", style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
                val usedColor = if (expenseToday > dailyLimit) Color(0xFFEF5350) else Color(0xFF66BB6A)
                Text("Used: ${formatCurrency(expenseToday)}", style = MaterialTheme.typography.bodySmall, color = usedColor)
            }
            Spacer(modifier = Modifier.height(4.dp))
            val progress = if (dailyLimit > 0) (expenseToday.toFloat() / dailyLimit.toFloat()).coerceIn(0f, 1f) else 0f
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = if (expenseToday > dailyLimit) Color(0xFFEF5350) else Color.Gray,
                trackColor = Color.DarkGray,
            )

            Spacer(modifier = Modifier.height(20.dp))

            FinanceRowItem(
                icon = Icons.Default.Lock,
                bgColor = Color(0xFFFFB300).copy(alpha = 0.2f),
                iconColor = Color(0xFFFFB300),
                label = "Emergency Fund (Set Aside)",
                value = formatCurrency(emergencyFund)
            )

            Spacer(modifier = Modifier.height(12.dp))

            FinanceRowItem(
                icon = Icons.Default.Analytics,
                bgColor = Color(0xFF42A5F5).copy(alpha = 0.2f),
                iconColor = Color(0xFF42A5F5),
                label = "Monthly Expense Estimate",
                value = formatCurrency(predictedExpense)
            )
        }
    }
}

@Composable
fun FinanceRowItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bgColor: Color,
    iconColor: Color,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray, fontSize = 11.sp)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun HomeTaskItem(task: Task) {
    val color = getTaskCategoryColor(task.category)
    val displayName = when {
        task.title.trim().isNotEmpty() -> task.title
        task.description.trim().isNotEmpty() -> task.description.trim()
        else -> "Untitled Task"
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if(task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                null,
                tint = if(task.isCompleted) Color.Green else MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(displayName, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                if (task.deadlineTimestamp != null) {
                    val sdf = SimpleDateFormat("dd MMM", Locale("id", "ID"))
                    Text("Deadline: ${sdf.format(Date(task.deadlineTimestamp!!))}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun MiniTransactionItem(transaction: Transaction) {
    val isIncome = transaction.type == "Income"
    val color = if (isIncome) Color(0xFF4CAF50) else Color(0xFFE53935)
    val sign = if (isIncome) "+" else "-"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(transaction.note.ifEmpty { transaction.category }, fontWeight = FontWeight.Medium)
            val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale("id", "ID"))
            Text(sdf.format(Date(transaction.timestamp)), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Text(
            text = "$sign${formatCurrency(transaction.amount)}",
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun WelcomeCard(name: String) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Welcome back!", fontWeight = FontWeight.Bold)
            Text("Let's stay productive today.")
        }
    }
}

// Komponen Helper Tutorial (Wajib ada)
@Composable
fun TutorialItemRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun formatCurrency(amount: Long): String {
    return "Rp " + NumberFormat.getInstance(Locale("id", "ID")).format(amount)
}