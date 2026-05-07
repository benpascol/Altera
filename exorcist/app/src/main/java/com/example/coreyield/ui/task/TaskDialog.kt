package com.example.coreyield.ui.tasks

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.coreyield.data.model.Task
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDialog(
    task: Task? = null,
    onDismiss: () -> Unit,
    onSave: (String, String, Int, Long?, Long) -> Unit,
    titleDialog: String
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var category by remember { mutableStateOf(task?.category ?: "Study") }
    var difficulty by remember { mutableStateOf(task?.difficultyScore?.toFloat() ?: 1f) }

    // Deadline bisa null
    var hasDeadline by remember { mutableStateOf(task?.deadlineTimestamp != null) }
    var selectedDeadline by remember { mutableStateOf(task?.deadlineTimestamp ?: System.currentTimeMillis()) }

    // Input tanggal (kapan task dimasukkan)
    var inputDate by remember { mutableStateOf(task?.inputTimestamp ?: System.currentTimeMillis()) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val deadlineDatePicker = DatePickerDialog(
        context, { _, y, m, d ->
            calendar.set(y, m, d)
            selectedDeadline = calendar.timeInMillis
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val inputDatePicker = DatePickerDialog(
        context, { _, y, m, d ->
            calendar.set(y, m, d)
            inputDate = calendar.timeInMillis
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    var categoryExpanded by remember { mutableStateOf(false) }
    val categories = listOf("Study", "Work", "Hobby", "Personal")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(titleDialog, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                // Task Name
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // --- CATEGORY (FIXED: Menggunakan ExposedDropdownMenuBox) ---
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {}, // ReadOnly, tidak perlu onValueChange
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(), // PENTING: Menjadikan ini jangkar menu
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                // Difficulty
                Column {
                    Text("Difficulty: ${difficulty.toInt()} Stars", style = MaterialTheme.typography.labelMedium)
                    Slider(
                        value = difficulty,
                        onValueChange = { difficulty = it },
                        valueRange = 1f..5f,
                        steps = 3
                    )
                }

                // Input Date (Kapan task dimasukkan/dikerjakan)
                OutlinedTextField(
                    value = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(inputDate)),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Input Date") },
                    trailingIcon = {
                        IconButton(onClick = { inputDatePicker.show() }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { inputDatePicker.show() }, // Clickable di Modifier utama
                    enabled = false, // Disable typing
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                // Deadline (Opsional)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = hasDeadline,
                        onCheckedChange = { hasDeadline = it }
                    )
                    Text("Set Deadline")
                }

                if (hasDeadline) {
                    OutlinedTextField(
                        value = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(selectedDeadline)),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Deadline") },
                        trailingIcon = {
                            IconButton(onClick = { deadlineDatePicker.show() }) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { deadlineDatePicker.show() },
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val deadline = if (hasDeadline) selectedDeadline else null
                        onSave(title, category, difficulty.toInt(), deadline, inputDate)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}