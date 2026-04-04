package com.expenseai.ui.screens.insights

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expenseai.domain.model.getCategoryById
import com.expenseai.ui.components.MonthSelector
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Insights") })
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
            MonthSelector(
                currentMonth = uiState.currentMonth,
                onPreviousMonth = viewModel::previousMonth,
                onNextMonth = viewModel::nextMonth
            )

            // Pie Chart
            if (uiState.categoryBreakdown.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Spending Breakdown",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Simple pie chart
                        val entries = uiState.categoryBreakdown.entries.toList()
                        val total = entries.sumOf { it.value }
                        val colors = entries.map { getCategoryById(it.key).color }

                        Canvas(
                            modifier = Modifier
                                .size(200.dp)
                                .padding(8.dp)
                        ) {
                            var startAngle = -90f
                            entries.forEachIndexed { index, entry ->
                                val sweepAngle = (entry.value / total * 360).toFloat()
                                drawArc(
                                    color = colors[index],
                                    startAngle = startAngle,
                                    sweepAngle = sweepAngle,
                                    useCenter = true,
                                    topLeft = Offset.Zero,
                                    size = Size(size.width, size.height)
                                )
                                startAngle += sweepAngle
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Legend
                        entries.forEachIndexed { index, entry ->
                            val category = getCategoryById(entry.key)
                            val percentage = (entry.value / total * 100).toInt()
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Canvas(modifier = Modifier.size(12.dp)) {
                                    drawCircle(color = colors[index])
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "${category.icon} ${category.label}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    "${formatter.format(entry.value)} ($percentage%)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            } else {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No spending data for this month",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // AI Insights
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "AI Insights",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (uiState.isLoadingInsights) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Analyzing your spending patterns...")
                        }
                    } else if (uiState.aiInsights.isNotBlank()) {
                        Text(
                            text = uiState.aiInsights,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        Button(
                            onClick = viewModel::generateInsights,
                            enabled = uiState.categoryBreakdown.isNotEmpty()
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate Insights")
                        }
                    }
                }
            }

            // Summary stats
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Summary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Spending", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            formatter.format(uiState.totalSpending),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Categories Used", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "${uiState.categoryBreakdown.size}",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (uiState.categoryBreakdown.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        val topCategory = uiState.categoryBreakdown.maxByOrNull { it.value }
                        topCategory?.let {
                            val cat = getCategoryById(it.key)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Top Category", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "${cat.icon} ${cat.label}",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
