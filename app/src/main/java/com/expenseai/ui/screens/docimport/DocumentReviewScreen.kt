package com.expenseai.ui.screens.docimport

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expenseai.ai.DocType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentReviewScreen(
    onDone: () -> Unit,
    viewModel: DocumentReviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onDone()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review Extraction", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.cancel(); onDone() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Cancel")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!uiState.hasExtraction) {
                Text(
                    "No extraction to review.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                return@Column
            }

            if (uiState.type == DocType.UNKNOWN) {
                Text(
                    "Couldn't recognise this document. Pick a type to fill the fields manually:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = uiState.type == DocType.SALARY_SLIP,
                    onClick = { viewModel.setType(DocType.SALARY_SLIP) },
                    label = { Text("Salary slip") }
                )
                FilterChip(
                    selected = uiState.type == DocType.LOAN_STATEMENT,
                    onClick = { viewModel.setType(DocType.LOAN_STATEMENT) },
                    label = { Text("Loan") }
                )
            }

            when (uiState.type) {
                DocType.SALARY_SLIP -> {
                    OutlinedTextField(
                        value = uiState.employer,
                        onValueChange = viewModel::updateEmployer,
                        label = { Text("Employer / income label") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.monthlyNet,
                        onValueChange = viewModel::updateMonthlyNet,
                        label = { Text("Monthly net pay") },
                        prefix = { Text("₹ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                DocType.LOAN_STATEMENT -> {
                    if (uiState.emiStartDate == null) {
                        Text(
                            "Estimated from current balance — sanction details weren't found, " +
                                "so the loan is modelled from today's outstanding amount.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    OutlinedTextField(
                        value = uiState.lender,
                        onValueChange = viewModel::updateLender,
                        label = { Text("Lender") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.principal,
                        onValueChange = viewModel::updatePrincipal,
                        label = {
                            Text(if (uiState.emiStartDate != null) "Sanctioned principal" else "Outstanding principal")
                        },
                        prefix = { Text("₹ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.annualRatePct,
                        onValueChange = viewModel::updateRate,
                        label = { Text("Interest rate (annual)") },
                        suffix = { Text("%") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.tenureMonths,
                        onValueChange = viewModel::updateTenure,
                        label = {
                            Text(if (uiState.emiStartDate != null) "Original tenure (months)" else "Remaining tenure (months)")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    uiState.emiStartDate?.let {
                        Text(
                            "First EMI date: $it",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                DocType.UNKNOWN -> Unit
            }

            HorizontalDivider()
            Text(
                "Extracted text (first part)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(uiState.textPreview, style = MaterialTheme.typography.bodySmall)

            Button(
                onClick = viewModel::confirm,
                enabled = viewModel.canConfirm && !uiState.isSaving,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    when (uiState.type) {
                        DocType.SALARY_SLIP -> "Add income to FIRE model"
                        DocType.LOAN_STATEMENT -> "Add loan to FIRE model"
                        DocType.UNKNOWN -> "Pick a type first"
                    }
                )
            }
        }
    }
}
