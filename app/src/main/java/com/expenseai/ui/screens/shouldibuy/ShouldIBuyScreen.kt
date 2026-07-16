package com.expenseai.ui.screens.shouldibuy

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
import com.expenseai.domain.fire.PurchaseDecision
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShouldIBuyScreen(
    onBack: () -> Unit,
    viewModel: ShouldIBuyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formatter = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Should I Buy This?", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                "Enter a purchase amount to see what it costs you in FIRE days.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedTextField(
                value = uiState.amountText,
                onValueChange = viewModel::updateAmount,
                label = { Text("Purchase amount") },
                prefix = { Text("₹ ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            uiState.decision?.let { decision ->
                DecisionCard(decision, formatter)
            }
        }
    }
}

@Composable
private fun DecisionCard(decision: PurchaseDecision, formatter: NumberFormat) {
    val dateFormat = remember { DateTimeFormatter.ofPattern("MMM yyyy") }
    val beyondHorizon = decision.baseFireDate != null && decision.newFireDate == null
    val verdict = when {
        beyondHorizon -> "Major setback. Are you sure?"
        decision.daysDelta <= 7 -> "Barely a blip."
        decision.daysDelta <= 30 -> "Noticeable, but fine if it matters to you."
        decision.daysDelta <= 90 -> "Significant. Sleep on it."
        else -> "Major setback. Are you sure?"
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (beyondHorizon) {
                Text(
                    "Pushes FIRE beyond the horizon",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text(
                    "+${decision.daysDelta} FIRE days",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (decision.daysDelta > 90) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
            Text(verdict, style = MaterialTheme.typography.titleMedium)
            HorizontalDivider()
            decision.baseFireDate?.let { base ->
                Text(
                    "FIRE date: ${base.format(dateFormat)} → " +
                        (decision.newFireDate?.format(dateFormat) ?: "beyond horizon"),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                "Invested instead, this becomes " +
                    "${formatter.format(decision.futureValueAtFireDate)} by your FIRE date.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
