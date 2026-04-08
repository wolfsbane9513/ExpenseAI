package com.expenseai.ui.screens.sources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expenseai.ui.screens.review.PendingReviewSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourcesScreen(viewModel: SourcesViewModel = hiltViewModel()) {
    val pendingCount by viewModel.pendingCount.collectAsStateWithLifecycle()
    var showReviewSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Data Sources") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (pendingCount > 0) {
                ElevatedButton(
                    onClick = { showReviewSheet = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$pendingCount expense${if (pendingCount == 1) "" else "s"} pending review",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Email receipts",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Share any receipt email to ExpenseAI to stage it for review.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    if (showReviewSheet) {
        PendingReviewSheet(onDismiss = { showReviewSheet = false })
    }
}
