package com.expenseai.ui.screens.insights

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expenseai.domain.fire.FireResult
import com.expenseai.domain.fire.Scenario
import com.expenseai.domain.model.getCategoryById
import com.expenseai.ui.components.MonthSelector
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    onShouldIBuyClick: () -> Unit = {},
    viewModel: InsightsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val projection by viewModel.projection.collectAsStateWithLifecycle()
    val formatter = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("FIRE Insights") })
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
            projection.result?.let { result ->
                ProjectionCard(
                    result = result,
                    scenarios = projection.scenarios,
                    formatter = formatter,
                    onToggleScenario = viewModel::toggleScenario,
                    onShouldIBuyClick = onShouldIBuyClick
                )
            }

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
                                Icon(
                                    imageVector = category.icon,
                                    contentDescription = null,
                                    tint = category.color,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    category.label,
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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = cat.icon,
                                        contentDescription = null,
                                        tint = cat.color,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(cat.label, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectionCard(
    result: FireResult,
    scenarios: List<Scenario>,
    formatter: java.text.NumberFormat,
    onToggleScenario: (String) -> Unit,
    onShouldIBuyClick: () -> Unit
) {
    val dateFormat = remember { DateTimeFormatter.ofPattern("MMM yyyy") }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "FIRE Projection",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "FIRE date",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        result.fireDate?.format(dateFormat) ?: "Beyond horizon",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Target corpus",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        formatter.format(result.targetCorpus),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            LinearProgressIndicator(
                progress = { (result.progressPercent / 100.0).toFloat().coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth()
            )
            TrajectorySparkline(result)
            if (scenarios.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(scenarios, key = { it.id }) { scenario ->
                        FilterChip(
                            selected = scenario.enabled,
                            onClick = { onToggleScenario(scenario.id) },
                            label = { Text(scenario.label) }
                        )
                    }
                }
                if (scenarios.any { it.amount <= 0.0 }) {
                    Text(
                        text = "Set scenario amounts in Settings for toggles to take effect.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            TextButton(onClick = onShouldIBuyClick) {
                Text("Should I buy something? →")
            }
        }
    }
}

@Composable
private fun TrajectorySparkline(result: FireResult) {
    val color = MaterialTheme.colorScheme.primary
    val points = result.trajectory
    if (points.size < 2) return
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        val maxCorpus = points.maxOf { it.corpus }.coerceAtLeast(1.0)
        val minCorpus = points.minOf { it.corpus }
        val range = (maxCorpus - minCorpus).coerceAtLeast(1.0)
        val stepX = size.width / (points.size - 1)
        val path = Path()
        points.forEachIndexed { i, p ->
            val x = i * stepX
            val y = size.height - ((p.corpus - minCorpus) / range * size.height).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path, color = color, style = Stroke(width = 4f))
    }
}
