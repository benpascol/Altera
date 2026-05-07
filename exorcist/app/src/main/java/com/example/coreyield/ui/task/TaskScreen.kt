package com.example.coreyield.ui.tasks

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coreyield.data.model.Task
import com.example.coreyield.ui.task.TaskItem
import com.example.coreyield.ui.tasks.getHeaderDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: TaskViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val tasks by viewModel.tasks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var sortBy by remember { mutableStateOf("deadline") } // default: deadline
    val sortOptions = mapOf(
        "deadline" to "Sort by Deadline",
        "date" to "Sort by Input Date",
        "difficulty" to "Sort by Highest Difficulty"
    )

    var showAddDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }
    var showAnalysisDialog by remember { mutableStateOf(false) }
    var showTutorialDialog by remember { mutableStateOf(false) }

    var currentFilter by remember { mutableStateOf("ALL") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredTasks = remember(tasks, currentFilter, searchQuery, sortBy) {
        val filtered = tasks.filter { task ->
            val matchesCategory = if (currentFilter == "ALL") true else task.category.equals(currentFilter, ignoreCase = true)
            val matchesSearch = (task.title + " " + task.description).contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }

        val sorted = when (sortBy) {
            "deadline" -> filtered.sortedBy { it.deadlineTimestamp }
            "date" -> filtered.sortedByDescending { it.inputTimestamp } // <-- GUNAKAN inputTimestamp
            "difficulty" -> filtered.sortedByDescending { it.difficultyScore }
            else -> filtered.sortedBy { it.deadlineTimestamp }
        }

        // Group berdasarkan input date jika sort by date
        val grouped = when (sortBy) {
            "date" -> sorted.groupBy { getHeaderDate(it.inputTimestamp, isDeadline = false) } // <-- inputTimestamp
            else -> sorted.groupBy { getHeaderDate(it.deadlineTimestamp ?: 0L, isDeadline = true) }
        }

        grouped
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Task Manager", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showTutorialDialog = true }) {
                        Icon(Icons.Default.Info, contentDescription = "Tutorial", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search Task...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        FilterChipButton("All", currentFilter == "ALL") { currentFilter = "ALL" }
                        FilterChipButton("Study", currentFilter == "Study") { currentFilter = "Study" }
                        FilterChipButton("Work", currentFilter == "Work") { currentFilter = "Work" }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        var showSortMenu by remember { mutableStateOf(false) }
                        Box {
                            IconButton(onClick = { showSortMenu = true }) {
                                Icon(Icons.Default.Sort, contentDescription = "Sort", tint = MaterialTheme.colorScheme.primary)
                            }
                            DropdownMenu(
                                expanded = showSortMenu,
                                onDismissRequest = { showSortMenu = false }
                            ) {
                                sortOptions.forEach { (key, label) ->
                                    DropdownMenuItem(
                                        text = { Text(label) },
                                        onClick = {
                                            sortBy = key
                                            showSortMenu = false
                                        }
                                    )
                                }
                            }
                        }

                        FilledIconButton(
                            onClick = { showAnalysisDialog = true },
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.PieChart, contentDescription = "Analysis", tint = MaterialTheme.colorScheme.primary)
                        }

                        Button(
                            onClick = { showAddDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            } else if (filteredTasks.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tasks found.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    filteredTasks.forEach { (dateHeader, itemsInGroup) ->
                        item { DateHeaderItem(text = dateHeader) }
                        items(itemsInGroup) { item ->
                            TaskItem(
                                task = item,
                                onItemClick = { taskToEdit = item },
                                onCheckClick = { viewModel.toggleTaskCompletion(item) },
                                onDeleteClick = { viewModel.deleteTask(item.id) }
                            )
                        }
                    }
                }
            }
        }

        if (showTutorialDialog) TaskTutorialDialog(onDismiss = { showTutorialDialog = false })

        if (showAnalysisDialog) {
            AlertDialog(
                onDismissRequest = { showAnalysisDialog = false },
                title = { Text("Life Balance Analysis", fontWeight = FontWeight.Bold) },
                text = { Column(modifier = Modifier.verticalScroll(rememberScrollState())) { TaskAnalysisCard(tasks = tasks) } },
                confirmButton = { TextButton(onClick = { showAnalysisDialog = false }) { Text("Close") } }
            )
        }

        if (showAddDialog) {
            TaskDialog(
                titleDialog = "Add Task",
                onDismiss = { showAddDialog = false },
                onSave = { name, cat, diff, deadline, inputDate ->
                    viewModel.addTask(name, cat, diff, deadline, inputDate)
                    showAddDialog = false
                }
            )
        }

        if (taskToEdit != null) {
            // Untuk edit, sementara pakai addTask (nanti bisa perbarui updateTask)
            TaskDialog(
                task = taskToEdit,
                titleDialog = "Edit Task",
                onDismiss = { taskToEdit = null },
                onSave = { name, cat, diff, deadline, inputDate ->
                    // TODO: Perbarui fungsi updateTask untuk dukung inputTimestamp
                    viewModel.updateTask(taskToEdit!!, name, cat, diff, deadline)
                    taskToEdit = null
                }
            )
        }
    }
}

// Komponen pendukung (tidak diubah)
@Composable
fun TaskAnalysisCard(tasks: List<Task>) {
    val totalTasks = tasks.size.toFloat()
    if (totalTasks <= 0) { Text("No tasks yet.", color = Color.Gray); return }
    val categories = listOf("Study", "Work", "Hobby", "Personal")
    val stats = categories.map { cat -> cat to tasks.count { it.category.equals(cat, ignoreCase = true) } }.sortedByDescending { it.second }

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Focus Distribution", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
            stats.forEach { (cat, count) ->
                if (count > 0) {
                    val percentage = count / totalTasks
                    val color = getTaskCategoryColor(cat)
                    Column(modifier = Modifier.padding(vertical = 6.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(cat, fontWeight = FontWeight.SemiBold)
                            Text("${(percentage * 100).toInt()}% ($count)", color = color)
                        }
                        LinearProgressIndicator(
                            progress = percentage,
                            modifier = Modifier.fillMaxWidth().height(8.dp),
                            color = color,
                            trackColor = color.copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskTutorialDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        // Menggunakan ikon tanda tanya agar lebih pas sebagai panduan
        icon = { Icon(Icons.Outlined.HelpOutline, null, tint = MaterialTheme.colorScheme.primary) },
        title = { Text("How to Use Task Manager", fontWeight = FontWeight.Bold) },
        text = {
            // Gunakan verticalScroll agar aman di layar kecil
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                TutorialItemRow(
                    icon = Icons.Default.AddCircle,
                    title = "Create Task",
                    desc = "Tap the '+' button. Remember to set a deadline for better AI analysis!"
                )
                TutorialItemRow(
                    icon = Icons.Default.Edit,
                    title = "Edit & Details",
                    desc = "Tap directly on any task card to edit details or change dates."
                )
                TutorialItemRow(
                    icon = Icons.Default.CheckCircle,
                    title = "Complete Task",
                    desc = "Tap the circle icon on the left of a task to mark it as done."
                )
                Divider(color = Color.Gray.copy(alpha = 0.3f))
                TutorialItemRow(
                    icon = Icons.Default.FilterList,
                    title = "Filter & Search",
                    desc = "Use the search bar above or tap chips like 'Study' to filter tasks."
                )
                TutorialItemRow(
                    icon = Icons.Default.Sort,
                    title = "Sort Tasks",
                    desc = "Tap the sort icon (↓↑) to organize by Deadline, Date added, or Difficulty."
                )
                TutorialItemRow(
                    icon = Icons.Default.PieChart,
                    title = "Analysis",
                    desc = "Tap the chart icon to see how your focus is distributed across categories."
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text("Got it, thanks!") }
        }
    )
}

@Composable
private fun TutorialItemRow(
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
            Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun FilterChipButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.height(32.dp).clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 12.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DateHeaderItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 4.dp)
    )
}