package com.expenseai.ui.screens.firemodel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expenseai.domain.fire.TargetMode
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FireModelScreen(
    onBack: () -> Unit,
    viewModel: FireModelViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            snackbarHostState.showSnackbar("FIRE Profile Saved")
            viewModel.resetSavedStatus()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("FIRE Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::save) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Target Strategy",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = uiState.targetMode == TargetMode.ANNUAL_EXPENSES_25X,
                    onClick = { viewModel.updateTargetMode(TargetMode.ANNUAL_EXPENSES_25X) },
                    label = { Text("25x Expenses") }
                )
                FilterChip(
                    selected = uiState.targetMode == TargetMode.EXPLICIT_TARGET,
                    onClick = { viewModel.updateTargetMode(TargetMode.EXPLICIT_TARGET) },
                    label = { Text("Custom Target") }
                )
            }

            if (uiState.targetMode == TargetMode.ANNUAL_EXPENSES_25X) {
                OutlinedTextField(
                    value = uiState.annualExpenses,
                    onValueChange = viewModel::updateAnnualExpenses,
                    label = { Text("Desired Annual Retirement Expenses") },
                    prefix = { Text("₹ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Calculated Target: ${currencyFormatter.format((uiState.annualExpenses.toDoubleOrNull() ?: 0.0) * 25)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider()

            Text(
                text = "Investment Assumptions",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = uiState.equityCagr,
                onValueChange = viewModel::updateEquityCagr,
                label = { Text("Expected Annual Return (Equity CAGR %)") },
                suffix = { Text("%") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.startCorpus,
                onValueChange = viewModel::updateStartCorpus,
                label = { Text("Current Invested Corpus") },
                prefix = { Text("₹ ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save FIRE Profile")
            }
        }
    }
}
