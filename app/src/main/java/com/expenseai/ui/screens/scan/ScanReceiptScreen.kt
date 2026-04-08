package com.expenseai.ui.screens.scan

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expenseai.domain.model.DEFAULT_CATEGORIES
import com.expenseai.domain.model.getCategoryById
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanReceiptScreen(
    viewModel: ScanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Camera capture
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempImageUri != null) {
            viewModel.processImageUri(tempImageUri!!)
        }
    }

    // Gallery picker
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.processImageUri(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Scan Receipt") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (uiState.scanState) {
                ScanState.IDLE -> {
                    Spacer(modifier = Modifier.height(48.dp))

                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Scan a receipt to automatically extract expense details",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            val imageFile = File(context.cacheDir, "receipt_${System.currentTimeMillis()}.jpg")
                            tempImageUri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileProvider",
                                imageFile
                            )
                            cameraLauncher.launch(tempImageUri!!)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Take Photo")
                    }

                    OutlinedButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Choose from Gallery")
                    }
                }

                ScanState.CAPTURING -> {
                    CircularProgressIndicator()
                    Text("Capturing...")
                }

                ScanState.PROCESSING_OCR -> {
                    Spacer(modifier = Modifier.height(80.dp))
                    CircularProgressIndicator(modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Extracting text from receipt...",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                ScanState.PROCESSING_AI -> {
                    Spacer(modifier = Modifier.height(80.dp))
                    CircularProgressIndicator(modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "AI is analyzing the receipt...",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "Extracting vendor, amount, and category",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                ScanState.REVIEW -> {
                    ReviewForm(
                        uiState = uiState,
                        onVendorChange = viewModel::updateVendor,
                        onAmountChange = viewModel::updateAmount,
                        onCategoryChange = viewModel::updateCategory,
                        onSave = viewModel::saveExpense,
                        onRetry = viewModel::reset
                    )
                }

                ScanState.SAVING -> {
                    CircularProgressIndicator()
                    Text("Saving expense...")
                }

                ScanState.SAVED -> {
                    Spacer(modifier = Modifier.height(80.dp))
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Expense saved!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = viewModel::reset) {
                        Text("Scan Another")
                    }
                }

                ScanState.ERROR -> {
                    Spacer(modifier = Modifier.height(80.dp))
                    Text(
                        "Something went wrong",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                    uiState.errorMessage?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = viewModel::reset) {
                        Text("Try Again")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewForm(
    uiState: ScanUiState,
    onVendorChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onSave: () -> Unit,
    onRetry: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Text(
        "Review Parsed Receipt",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold
    )

    OutlinedTextField(
        value = uiState.editVendor,
        onValueChange = onVendorChange,
        label = { Text("Vendor") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = uiState.editAmount,
        onValueChange = { onAmountChange(it.filter { c -> c.isDigit() || c == '.' }) },
        label = { Text("Amount (₹)") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        val category = getCategoryById(uiState.editCategory)
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
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DEFAULT_CATEGORIES.forEach { cat ->
                DropdownMenuItem(
                    text = { Text("${cat.icon} ${cat.label}") },
                    onClick = {
                        onCategoryChange(cat.id)
                        expanded = false
                    }
                )
            }
        }
    }

    OutlinedTextField(
        value = uiState.editDate,
        onValueChange = {},
        label = { Text("Date") },
        readOnly = true,
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )

    if (uiState.ocrText.isNotBlank()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    "OCR Text",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = uiState.ocrText.take(500),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onRetry,
            modifier = Modifier.weight(1f)
        ) {
            Text("Retry")
        }
        Button(
            onClick = onSave,
            modifier = Modifier.weight(1f),
            enabled = uiState.editVendor.isNotBlank() &&
                    (uiState.editAmount.toDoubleOrNull() ?: 0.0) > 0
        ) {
            Text("Save")
        }
    }
}
