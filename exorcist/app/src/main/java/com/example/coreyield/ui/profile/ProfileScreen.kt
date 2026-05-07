package com.example.coreyield.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
// --- IMPORT LENGKAP AGAR TIDAK ERROR ---
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableFloatStateOf // Perhatikan: FloatStateOf (bukan StateFloatOf)
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onNavigateBack: () -> Unit
    // Parameter onLogout SUDAH DIHAPUS
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var isEditing by remember { mutableStateOf(false) }

    // State Lokal
    var name by remember { mutableStateOf("") }
    var income by remember { mutableStateOf("") }

    // INI YANG TADI ERROR: Sekarang sudah aman karena ada import androidx.compose.runtime.*
    var savingsTarget by remember { mutableFloatStateOf(20f) }

    var startHour by remember { mutableStateOf("9") }
    var endHour by remember { mutableStateOf("17") }

    LaunchedEffect(userProfile) {
        userProfile?.let {
            name = it.fullName
            income = it.monthlyIncome.toString()
            savingsTarget = it.savingsTargetPercent.toFloat()
            startHour = it.activeStartHour.toString()
            endHour = it.activeEndHour.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
                // TOMBOL LOGOUT DI KANAN ATAS SUDAH DIHAPUS TOTAL
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isEditing) {
                        val sHour = startHour.toIntOrNull()?.coerceIn(0, 23) ?: 9
                        val eHour = endHour.toIntOrNull()?.coerceIn(0, 23) ?: 17

                        viewModel.updateProfile(
                            fullName = name,
                            monthlyIncome = income.toLongOrNull() ?: 0L,
                            savingsTarget = savingsTarget.toInt(),
                            activeStartHour = sHour,
                            activeEndHour = eHour
                        )
                        isEditing = false
                    } else {
                        isEditing = true
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(if (isEditing) Icons.Default.Save else Icons.Default.Edit, contentDescription = null)
            }
        }
    ) { padding ->
        if (isLoading && userProfile == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userProfile?.fullName?.take(1)?.uppercase() ?: "?",
                        fontSize = 40.sp, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Info Nama & Email
                Text(userProfile?.fullName ?: "Loading...", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(userProfile?.email ?: "", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

                Spacer(modifier = Modifier.height(24.dp))

                // --- FORM INPUT ---

                // 1. Nama Lengkap
                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("Full Name") }, enabled = isEditing,
                    modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Default.Person, null) },
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 2. Pendapatan
                OutlinedTextField(
                    value = income,
                    onValueChange = { if (it.all { char -> char.isDigit() }) income = it },
                    label = { Text("Monthly Income (Rp)") }, enabled = isEditing,
                    modifier = Modifier.fillMaxWidth(), singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    supportingText = {
                        if (income.isNotEmpty()) {
                            val formatted = try {
                                NumberFormat.getInstance(Locale("id", "ID")).format(income.toLong())
                            } catch (e:Exception) { income }
                            Text("Rp $formatted")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))

                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                // 3. Target Tabungan (Slider)
                Text("Savings Target: ${savingsTarget.toInt()}%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Recommended: 20-30%", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                Slider(
                    value = savingsTarget,
                    onValueChange = { savingsTarget = it },
                    valueRange = 10f..50f,
                    steps = 39,
                    enabled = isEditing,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("10%", style = MaterialTheme.typography.bodySmall)
                    Text("50%", style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(24.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                // 4. Jam Kerja
                Text("Work Hours (24h Format)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = startHour,
                        onValueChange = { if (it.length <= 2 && it.all { c -> c.isDigit() }) startHour = it },
                        label = { Text("Start (0-23)") }, enabled = isEditing,
                        modifier = Modifier.weight(1f), singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        leadingIcon = { Icon(Icons.Default.Schedule, null) },
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = endHour,
                        onValueChange = { if (it.length <= 2 && it.all { c -> c.isDigit() }) endHour = it },
                        label = { Text("End (0-23)") }, enabled = isEditing,
                        modifier = Modifier.weight(1f), singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        leadingIcon = { Icon(Icons.Default.Schedule, null) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                if (isEditing) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tap save to apply changes.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}