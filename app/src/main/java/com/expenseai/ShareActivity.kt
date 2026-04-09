package com.expenseai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expenseai.ui.screens.review.PendingReviewSheet
import com.expenseai.ui.screens.review.ReviewViewModel
import com.expenseai.ui.theme.ExpenseAITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedText = intent?.getStringExtra(Intent.EXTRA_TEXT) ?: run {
            finish()
            return
        }
        val subject = intent?.getStringExtra(Intent.EXTRA_SUBJECT) ?: ""

        setContent {
            ExpenseAITheme {
                val viewModel: ReviewViewModel = hiltViewModel()
                val pendingExpenses by viewModel.pendingExpenses.collectAsStateWithLifecycle()
                val shareStagingState by viewModel.shareStagingState.collectAsStateWithLifecycle()

                LaunchedEffect(sharedText, subject) {
                    viewModel.processSharedText(body = sharedText, subject = subject)
                }

                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text("Review Shared Transaction") })
                    }
                ) { padding ->
                    when {
                        pendingExpenses.isNotEmpty() -> {
                            PendingReviewSheet(
                                viewModel = viewModel,
                                onDismiss = { finish() }
                            )
                        }

                        shareStagingState.isProcessing || shareStagingState.errorMessage == null -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        else -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding)
                                    .padding(24.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = shareStagingState.errorMessage
                                        ?: "We couldn't stage this shared transaction.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                FilledTonalButton(onClick = { finish() }) {
                                    Text("Close")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
