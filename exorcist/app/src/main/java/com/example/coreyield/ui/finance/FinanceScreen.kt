package com.example.coreyield.ui.finance

import android.app.DatePickerDialog
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Timeline // Icon untuk Estimasi
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coreyield.AppConstants
import com.example.coreyield.data.model.Transaction
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FinanceScreen(
    viewModel: FinanceViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val transactions by viewModel.transactions.collectAsState()
    val totalBalance by viewModel.totalBalance.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Ambil Data State Lengkap (termasuk prediksi)
    val financeState by viewModel.financeState.collectAsState()
    val predictedExpense = financeState.predictedExpense

    // States Dialog
    var showAnalysisDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showTutorialDialog by remember { mutableStateOf(false) }
    var transactionToEdit by remember { mutableStateOf<Transaction?>(null) }

    var currentFilter by remember { mutableStateOf("ALL") }
    var searchQuery by remember { mutableStateOf("") }

    fun formatRupiah(amount: Long): String {
        return "Rp " + NumberFormat.getInstance(Locale("id", "ID")).format(amount)
    }

    val groupedTransactions = remember(transactions, currentFilter, searchQuery) {
        transactions
            .filter { if (currentFilter == "ALL") true else it.type == currentFilter }
            .filter { trans ->
                trans.category.contains(searchQuery, ignoreCase = true) ||
                        trans.note.contains(searchQuery, ignoreCase = true)
            }
            .sortedByDescending { it.timestamp }
            .groupBy { getHeaderDate(it.timestamp) }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("My Wallet", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showTutorialDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Tutorial",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // HEADER AREA
            Column(modifier = Modifier.padding(16.dp)) {

                // --- KARTU SALDO & PREDIKSI (UPDATED) ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    shape = RoundedCornerShape(20.dp) // Sedikit lebih bulat
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        // 1. Header Kecil
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Total Balance",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // 2. Nominal Besar
                        Text(
                            text = formatRupiah(totalBalance),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        // 3. PREDIKSI AI (DI DALAM KARTU)
                        if (predictedExpense > 0) {
                            Spacer(modifier = Modifier.height(12.dp))

                            // Badge Background Tipis
                            Surface(
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Timeline, // Icon Grafik Garis
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Next Month Est: ${formatRupiah(predictedExpense)}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search Transaction...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ROW FILTER & ACTIONS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // KIRI: Filter Chips
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        FilterChipButton("All", currentFilter == "ALL") { currentFilter = "ALL" }
                        FilterChipButton("In", currentFilter == "Income") { currentFilter = "Income" }
                        FilterChipButton("Out", currentFilter == "Expense") { currentFilter = "Expense" }
                    }

                    // KANAN: Tombol Analysis & Add Transaction
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledIconButton(
                            onClick = { showAnalysisDialog = true },
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Assessment,
                                contentDescription = "Analysis",
                                tint = MaterialTheme.colorScheme.primary
                            )
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

            Spacer(modifier = Modifier.height(8.dp))

            // LIST TRANSAKSI
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            } else if (groupedTransactions.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No transactions found.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    groupedTransactions.forEach { (dateHeader, itemsInGroup) ->
                        item { DateHeaderItem(text = dateHeader) }
                        items(itemsInGroup) { item ->
                            TransactionItem(
                                item = item,
                                formatFunc = ::formatRupiah,
                                onClick = { transactionToEdit = item }
                            )
                        }
                    }
                }
            }
        }

        // --- DIALOGS ---

        if (showTutorialDialog) {
            FinanceTutorialDialog(onDismiss = { showTutorialDialog = false })
        }

        if (showAnalysisDialog) {
            AlertDialog(
                onDismissRequest = { showAnalysisDialog = false },
                title = { Text("Expense Analysis", fontWeight = FontWeight.Bold) },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        CategoryBreakdownCard(transactions = transactions)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAnalysisDialog = false }) { Text("Close") }
                }
            )
        }

        if (showAddDialog) {
            AddTransactionDialog(
                onDismiss = { showAddDialog = false },
                onSave = { transaction ->
                    viewModel.addTransaction(
                        amount = transaction.amount,
                        category = transaction.category,
                        type = transaction.type,
                        note = transaction.note,
                        timestamp = transaction.timestamp
                    )
                    showAddDialog = false
                }
            )
        }

        if (transactionToEdit != null) {
            TransactionDialog(
                title = "Edit Transaction",
                existingData = transactionToEdit,
                onDismiss = { transactionToEdit = null },
                onSave = { amt, type, cat, note ->
                    viewModel.updateTransaction(transactionToEdit!!, amt, type, cat, note)
                    transactionToEdit = null
                },
                onDelete = {
                    viewModel.deleteTransaction(transactionToEdit!!.id)
                    transactionToEdit = null
                }
            )
        }
    }
}

// --- TUTORIAL DIALOG (UPDATED DENGAN PENJELASAN AI) ---
// --- UPDATE FinanceTutorialDialog ---
@Composable
fun FinanceTutorialDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Outlined.HelpOutline, null, tint = MaterialTheme.colorScheme.primary) },
        title = { Text("Finance Features Guide", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                TutorialItemRow(
                    icon = Icons.Default.AddCircle,
                    title = "Record Transaction",
                    desc = "Tap the (+) button to log Income or Expenses. Categorize them to track where your money goes."
                )
                TutorialItemRow(
                    icon = Icons.Default.PieChart,
                    title = "Expense Breakdown",
                    desc = "Tap the Chart icon to see a pie chart of your spending (e.g., 50% Food, 20% Transport)."
                )
                TutorialItemRow(
                    icon = Icons.Default.AccountBalanceWallet,
                    title = "Monitor Balance",
                    desc = "Check 'Spendable Balance' on the Home Screen to know your safe daily spending limit."
                )
                TutorialItemRow(
                    icon = Icons.Default.Edit,
                    title = "Edit History",
                    desc = "Tap on any transaction in the list to fix an amount or delete an entry."
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text("Understood") }
        }
    )
}

// Taruh di bagian bawah file TaskScreen.kt DAN FinanceScreen.kt
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

// --- HELPER FUNCTIONS ---

fun getCategoryColor(category: String, type: String): Color {
    val cleanCategory = category.trim().lowercase()
    return if (type == "Income") {
        when (cleanCategory) {
            "gaji", "salary" -> Color(0xFF2E7D32)
            "bonus", "tunjangan" -> Color(0xFFFFD700)
            "investasi", "investment" -> Color(0xFF1976D2)
            "hadiah", "gift" -> Color(0xFFE91E63)
            "penjualan", "sales" -> Color(0xFF009688)
            else -> Color(0xFF4CAF50)
        }
    } else {
        when (cleanCategory) {
            "makanan", "food", "makan", "minum", "jajan" -> Color(0xFFEF5350)
            "transport", "transportasi", "bensin", "parkir", "gojek", "grab" -> Color(0xFF42A5F5)
            "hiburan", "entertainment", "nonton", "game", "spotify", "netflix" -> Color(0xFFAB47BC)
            "belanja", "shopping", "baju", "online", "tokopedia", "shopee" -> Color(0xFFFFA726)
            "tagihan", "bills", "listrik", "air", "internet", "wifi", "pulsa" -> Color(0xFF009688)
            "kesehatan", "health", "obat", "dokter", "rs" -> Color(0xFFEC407A)
            "pendidikan", "education", "buku", "kursus", "spp" -> Color(0xFF5C6BC0)
            "random", "lainnya", "others", "umum" -> Color(0xFF795548)
            "sedekah", "amal", "infaq" -> Color(0xFF8D6E63)
            else -> generateRandomColor(cleanCategory)
        }
    }
}

fun generateRandomColor(key: String): Color {
    val hash = key.hashCode()
    var hue = (kotlin.math.abs(hash) % 360).toFloat()
    if (hue >= 70f && hue <= 170f) hue = (hue + 150f) % 360f
    return Color(android.graphics.Color.HSVToColor(floatArrayOf(hue, 0.65f, 0.85f)))
}

fun getCategoryIcon(category: String): ImageVector {
    val cleanCategory = category.trim().lowercase()
    return when {
        cleanCategory in listOf("makanan", "food", "makan", "minum", "jajan") -> Icons.Default.Fastfood
        cleanCategory in listOf("transport", "transportasi", "bensin", "parkir", "gojek", "grab") -> Icons.Default.DirectionsCar
        cleanCategory in listOf("hiburan", "entertainment", "game", "movie", "nonton") -> Icons.Default.Movie
        cleanCategory in listOf("belanja", "shopping", "baju", "online") -> Icons.Default.ShoppingBag
        cleanCategory in listOf("gaji", "salary", "bonus", "uang") -> Icons.Default.AttachMoney
        else -> Icons.Default.Info
    }
}

fun getHeaderDate(millis: Long): String {
    val input = Calendar.getInstance().apply { timeInMillis = millis }
    val today = Calendar.getInstance()
    val isSameDay = input.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            input.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
    val isYesterday = input.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            input.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) - 1
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
    return when {
        isSameDay -> "Today"
        isYesterday -> "Yesterday"
        else -> sdf.format(input.time)
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

@Composable
fun FilterChipButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.height(32.dp).clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 12.dp)) {
            Text(text = text, style = MaterialTheme.typography.labelMedium, color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun TransactionItem(item: Transaction, formatFunc: (Long) -> String, onClick: () -> Unit) {
    fun formatJam(millis: Long): String = SimpleDateFormat("HH:mm", Locale("id", "ID")).format(Date(millis))
    val isIncome = item.type == "Income"
    val categoryColor = getCategoryColor(item.category, item.type)
    val financialColor = if (isIncome) Color(0xFF2E7D32) else Color(0xFFC62828)
    val icon = if (isIncome) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward

    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Surface(
                    shape = CircleShape,
                    color = categoryColor.copy(alpha = 0.1f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, tint = categoryColor, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(item.category, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge, color = categoryColor)
                    Text(
                        text = if(item.note.isNotEmpty()) "${formatJam(item.timestamp)} • ${item.note}" else formatJam(item.timestamp),
                        style = MaterialTheme.typography.bodySmall, color = Color.Gray, maxLines = 1
                    )
                }
            }
            Text(
                text = (if (isIncome) "+ " else "- ") + formatFunc(item.amount),
                color = financialColor, fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CategoryBreakdownCard(transactions: List<Transaction>) {
    val expenseList = transactions.filter { it.type == "Expense" }
    val totalExpense = expenseList.sumOf { it.amount }.toFloat()
    if (totalExpense <= 0) return
    val categoryMap = expenseList.groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }
        .toList().sortedByDescending { it.second }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            categoryMap.forEach { (category, amount) ->
                val percentage = amount / totalExpense
                val catColor = getCategoryColor(category, "Expense")
                Column(modifier = Modifier.padding(vertical = 6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(category, style = MaterialTheme.typography.bodyMedium)
                        Text("${(percentage * 100).toInt()}%", fontWeight = FontWeight.Bold, color = catColor)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = percentage, modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = catColor, trackColor = catColor.copy(alpha = 0.2f), strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    Text("Rp ${java.text.NumberFormat.getInstance().format(amount)}", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.align(Alignment.End))
                }
            }
        }
    }
}

// Dialogs (TransactionDialog & AddTransactionDialog tetap sama, biarkan saja)
// Pastikan TransactionDialog dan AddTransactionDialog tetap ada di file ini.
// (Copy dari kode sebelumnya jika tidak ter-copy otomatis)

@Composable
fun TransactionDialog(
    title: String, existingData: Transaction? = null, onDismiss: () -> Unit,
    onSave: (Long, String, String, String) -> Unit, onDelete: (() -> Unit)? = null
) {
    var amountText by remember { mutableStateOf(existingData?.amount?.toString() ?: "") }
    var selectedType by remember { mutableStateOf(existingData?.type ?: "Expense") }
    var selectedCategory by remember { mutableStateOf(existingData?.category ?: "") }
    var note by remember { mutableStateOf(existingData?.note ?: "") }
    var categoryExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text =  {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().height(48.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray.copy(alpha = 0.2f))
                ) {
                    Box(modifier = Modifier.weight(1f).fillMaxHeight().background(if (selectedType == "Income") Color(0xFF2E7D32).copy(alpha = 0.2f) else Color.Transparent).clickable { selectedType = "Income" }, contentAlignment = Alignment.Center) {
                        Text("Income", color = if (selectedType == "Income") Color(0xFF2E7D32) else Color.Gray, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color.Gray.copy(alpha = 0.2f)))
                    Box(modifier = Modifier.weight(1f).fillMaxHeight().background(if (selectedType == "Expense") Color(0xFFC62828).copy(alpha = 0.2f) else Color.Transparent).clickable { selectedType = "Expense" }, contentAlignment = Alignment.Center) {
                        Text("Expense", color = if (selectedType == "Expense") Color(0xFFC62828) else Color.Gray, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = amountText, onValueChange = { if (it.all { c -> c.isDigit() }) amountText = it }, label = { Text("Nominal (Rp)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Box {
                    OutlinedTextField(value = selectedCategory, onValueChange = {}, readOnly = true, label = { Text("Category") }, trailingIcon = { IconButton(onClick = { categoryExpanded = true }) { Icon(Icons.Default.ArrowDownward, null) } }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    DropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
                        AppConstants.FINANCE_CATEGORIES.forEach { cat -> DropdownMenuItem(text = { Text(cat) }, onClick = { selectedCategory = cat; categoryExpanded = false }) }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Note") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            }
        },
        confirmButton = { Button(onClick = { val amt = amountText.toLongOrNull() ?: 0L; if (amt > 0 && selectedCategory.isNotEmpty()) onSave(amt, selectedType, selectedCategory, note) }) { Text("Save") } },
        dismissButton = { Row { if (onDelete != null) TextButton(onClick = onDelete, colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)) { Icon(Icons.Default.Delete, null); Text("Delete") }; TextButton(onClick = onDismiss) { Text("Cancel") } } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(onDismiss: () -> Unit, onSave: (Transaction) -> Unit) {
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Expense") }
    var category by remember { mutableStateOf("Makanan") }
    var selectedTimestamp by remember { mutableStateOf(System.currentTimeMillis()) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))

    fun showDatePicker() {
        calendar.timeInMillis = selectedTimestamp
        android.app.DatePickerDialog(context, { _, y, m, d -> calendar.set(y, m, d); selectedTimestamp = calendar.timeInMillis }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Transaction", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    FilterChip(selected = type == "Income", onClick = { type = "Income"; category = "Gaji" }, label = { Text("Income") }, leadingIcon = { if (type == "Income") Icon(Icons.Default.Add, null) })
                    FilterChip(selected = type == "Expense", onClick = { type = "Expense"; category = "Makanan" }, label = { Text("Expense") }, leadingIcon = { if (type == "Expense") Icon(Icons.Default.ArrowDownward, null) })
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = dateFormat.format(Date(selectedTimestamp)), onValueChange = {}, label = { Text("Date") }, readOnly = true, trailingIcon = { IconButton(onClick = { showDatePicker() }) { Icon(Icons.Default.FilterList, null) } }, modifier = Modifier.fillMaxWidth().clickable { showDatePicker() })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = amount, onValueChange = { if (it.all { c -> c.isDigit() }) amount = it }, label = { Text("Amount") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Note") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = { Button(onClick = { if (amount.isNotEmpty()) onSave(Transaction("", amount.toLong(), type, category, note, selectedTimestamp)) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}