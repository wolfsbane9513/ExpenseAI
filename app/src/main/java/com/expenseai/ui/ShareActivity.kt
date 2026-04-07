package com.expenseai.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.expenseai.ui.screens.review.PendingReviewSheet
import com.expenseai.ui.screens.review.ReviewViewModel
import com.expenseai.ui.theme.ExpenseAITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedText = intent?.getStringExtra(Intent.EXTRA_TEXT) ?: run { finish(); return }
        val subject = intent?.getStringExtra(Intent.EXTRA_SUBJECT) ?: ""

        setContent {
            ExpenseAITheme {
                val viewModel: ReviewViewModel = hiltViewModel()
                viewModel.processSharedEmail(body = sharedText, subject = subject)
                PendingReviewSheet(
                    viewModel = viewModel,
                    onDismiss = { finish() }
                )
            }
        }
    }
}
