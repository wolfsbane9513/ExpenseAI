package com.expenseai.ui.screens.sources

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expenseai.ui.screens.review.PendingReviewSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourcesScreen(viewModel: SourcesViewModel = hiltViewModel()) {
    val pendingCount by viewModel.pendingCount.collectAsStateWithLifecycle()
    val smsEnabled by viewModel.smsEnabled.collectAsStateWithLifecycle()
    val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()
    val lastScanCount by viewModel.lastScanCount.collectAsStateWithLifecycle()
    var showReviewSheet by remember { mutableStateOf(false) }
    var showPermissionDeniedSnackbar by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val smsPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.setSmsEnabled(true)
        else showPermissionDeniedSnackbar = true
    }

    LaunchedEffect(showPermissionDeniedSnackbar) {
        if (showPermissionDeniedSnackbar) {
            snackbarHostState.showSnackbar("SMS permission required to scan transactions")
            showPermissionDeniedSnackbar = false
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Data Sources") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Sms, contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary)
                        Column {
                            Text("SMS Inbox", style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium)
                            Text(
                                text = if (isScanning) "Scanning…"
                                       else if (lastScanCount > 0) "Found $lastScanCount transaction${if (lastScanCount == 1) "" else "s"}"
                                       else "Auto-detects bank transactions",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Switch(
                        checked = smsEnabled,
                        onCheckedChange = { enabled ->
                            if (enabled) {
                                val hasPerm = ContextCompat.checkSelfPermission(
                                    context, Manifest.permission.READ_SMS
                                ) == PackageManager.PERMISSION_GRANTED
                                if (hasPerm) viewModel.setSmsEnabled(true)
                                else smsPermissionLauncher.launch(Manifest.permission.READ_SMS)
                            } else {
                                viewModel.setSmsEnabled(false)
                            }
                        }
                    )
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Default.Email, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary)
                    Column {
                        Text("Email (Share)", style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium)
                        Text(
                            text = "Share any receipt email → ExpenseAI",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    if (showReviewSheet) {
        PendingReviewSheet(onDismiss = { showReviewSheet = false })
    }
}
