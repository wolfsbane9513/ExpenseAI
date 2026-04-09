package com.expenseai.ui.screens.dashboard

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expenseai.ai.ModelStatus
import com.expenseai.domain.model.DEFAULT_CATEGORIES
import com.expenseai.domain.model.getCategoryById
import com.expenseai.ui.components.ExpenseCard
import com.expenseai.ui.components.ModelStatusIndicator
import com.expenseai.ui.components.MonthSelector
import com.expenseai.ui.theme.ExpenseAITheme
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onScanClick: () -> Unit = {},
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val modelPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let(viewModel::importModel)
    }

    DashboardContent(
        uiState = uiState,
        onPreviousMonth = viewModel::previousMonth,
        onNextMonth = viewModel::nextMonth,
        onAddExpense = viewModel::addExpense,
        onScanClick = onScanClick,
        onImportModel = { modelPickerLauncher.launch(arrayOf("*/*")) },
        onRemoveModel = viewModel::removeModel,
        modelImportSummary = viewModel.getModelImportSummary()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    uiState: DashboardUiState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onAddExpense: (String, Double, String, String) -> Unit,
    onScanClick: () -> Unit = {},
    onImportModel: () -> Unit = {},
    onRemoveModel: () -> Unit = {},
    modelImportSummary: String = ""
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Welcome back",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "ExpenseAI",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                },
                actions = {
                    ModelStatusIndicator(status = uiState.modelStatus)
                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add expense")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                MonthSelector(
                    currentMonth = uiState.currentMonth,
                    onPreviousMonth = onPreviousMonth,
                    onNextMonth = onNextMonth
                )
            }

            item {
                QuickActionsCard(
                    onScanClick = onScanClick,
                    onInstallModel = onImportModel,
                    hasInstalledModel = uiState.installedModelName != null,
                    isModelBusy = uiState.modelStatus == ModelStatus.LOADING ||
                        uiState.modelStatus == ModelStatus.DOWNLOADING
                )
            }

            item {
                DashboardHeroCard(
                    totalSpending = formatter.format(uiState.totalSpending),
                    budgetSummary = "65% of monthly budget (Rs 22,000)"
                )
            }

            item {
                ModelSetupCard(
                    uiState = uiState,
                    modelImportSummary = modelImportSummary,
                    onImportModel = onImportModel,
                    onRemoveModel = onRemoveModel
                )
            }

            if (uiState.categoryTotals.isNotEmpty()) {
                item {
                    Text(
                        text = "By Category",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                items(uiState.categoryTotals) { categoryTotal ->
                    val category = getCategoryById(categoryTotal.category)
                    val percentage = if (uiState.totalSpending > 0) {
                        (categoryTotal.total / uiState.totalSpending).toFloat()
                    } else {
                        0f
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = category.icon,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = category.label,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = formatter.format(categoryTotal.total),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { percentage },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp),
                                color = category.color,
                                trackColor = category.color.copy(alpha = 0.15f)
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Recent Expenses",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (uiState.recentExpenses.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "No expenses yet. Start with a quick scan or add one manually.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            FilledTonalButton(
                                onClick = onScanClick,
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Icon(Icons.Default.AddAPhoto, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Scan your first receipt")
                            }
                        }
                    }
                }
            } else {
                items(uiState.recentExpenses, key = { it.id }) { expense ->
                    ExpenseCard(expense = expense)
                }
            }
        }
    }

    if (showAddDialog) {
        AddExpenseDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { vendor, amount, category, date ->
                onAddExpense(vendor, amount, category, date)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun DashboardHeroCard(
    totalSpending: String,
    budgetSummary: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.72f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "TOTAL SPENDING",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.72f),
                letterSpacing = 1.6.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = totalSpending,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(18.dp))
            LinearProgressIndicator(
                progress = { 0.65f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.12f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = budgetSummary,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.65f)
            )
        }
    }
}

@Composable
private fun QuickActionsCard(
    onScanClick: () -> Unit,
    onInstallModel: () -> Unit,
    hasInstalledModel: Boolean,
    isModelBusy: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilledTonalButton(
                onClick = onScanClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(Icons.Default.AddAPhoto, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Scan receipt")
            }
            Button(
                onClick = onInstallModel,
                enabled = !isModelBusy,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (hasInstalledModel) "Upgrade AI" else "Install AI")
            }
        }
    }
}

@Composable
private fun ModelSetupCard(
    uiState: DashboardUiState,
    modelImportSummary: String,
    onImportModel: () -> Unit,
    onRemoveModel: () -> Unit
) {
    val isBusy = uiState.modelStatus == ModelStatus.LOADING ||
        uiState.modelStatus == ModelStatus.DOWNLOADING
    val statusCopy = when (uiState.modelStatus) {
        ModelStatus.NOT_DOWNLOADED -> "Fallback mode active"
        ModelStatus.DOWNLOADING -> "Installing AI model"
        ModelStatus.LOADING -> "Starting AI model"
        ModelStatus.READY -> "On-device intelligence ready"
        ModelStatus.ERROR -> "AI model unavailable"
    }
    val statusColor = when (uiState.modelStatus) {
        ModelStatus.READY -> MaterialTheme.colorScheme.primary
        ModelStatus.ERROR -> MaterialTheme.colorScheme.error
        ModelStatus.LOADING, ModelStatus.DOWNLOADING -> MaterialTheme.colorScheme.secondary
        ModelStatus.NOT_DOWNLOADED -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = "On-device AI",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = modelImportSummary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Surface(
                color = statusColor.copy(alpha = 0.12f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = statusColor,
                        shape = CircleShape,
                        modifier = Modifier.size(8.dp)
                    ) {}
                    Text(
                        text = statusCopy,
                        style = MaterialTheme.typography.labelMedium,
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            uiState.installedModelName?.let { modelName ->
                Text(
                    text = "Installed model: $modelName",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            uiState.modelMessage?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (uiState.modelStatus == ModelStatus.ERROR) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onImportModel,
                    enabled = !isBusy
                ) {
                    Icon(Icons.Default.UploadFile, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (uiState.installedModelName == null) {
                            "Install AI Model"
                        } else {
                            "Replace Installed Model"
                        }
                    )
                }
                if (uiState.installedModelName != null) {
                    OutlinedButton(
                        onClick = onRemoveModel,
                        enabled = !isBusy
                    ) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Remove")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Double, String, String) -> Unit
) {
    var vendor by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("food") }
    var expanded by remember { mutableStateOf(false) }
    val today = LocalDate.now().toString()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Expense") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = vendor,
                    onValueChange = { vendor = it },
                    label = { Text("Vendor") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("Amount (Rs)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    val category = getCategoryById(selectedCategory)
                    OutlinedTextField(
                        value = "${category.icon} ${category.label}",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DEFAULT_CATEGORIES.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text("${cat.icon} ${cat.label}") },
                                onClick = {
                                    selectedCategory = cat.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amountValue = amount.toDoubleOrNull() ?: 0.0
                    if (vendor.isNotBlank() && amountValue > 0) {
                        onConfirm(vendor, amountValue, selectedCategory, today)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    ExpenseAITheme {
        DashboardContent(
            uiState = DashboardUiState(
                totalSpending = 1250.0,
                modelStatus = ModelStatus.READY,
                modelMessage = "Gemma is ready to parse receipts, SMS, and shared email text on device.",
                installedModelName = "gemma-3n-it-int4.task",
                recentExpenses = listOf(
                    com.expenseai.domain.model.Expense(1, "Starbucks", 250.0, "food", "2024-03-20"),
                    com.expenseai.domain.model.Expense(2, "Amazon", 1000.0, "shopping", "2024-03-19")
                )
            ),
            onPreviousMonth = {},
            onNextMonth = {},
            onAddExpense = { _, _, _, _ -> },
            modelImportSummary = "Install Gemma on this phone to unlock smarter on-device parsing."
        )
    }
}
