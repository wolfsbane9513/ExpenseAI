package com.expenseai.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.expenseai.ai.ModelStatus

@Composable
fun ModelStatusIndicator(
    status: ModelStatus,
    modifier: Modifier = Modifier
) {
    val (color, label) = when (status) {
        ModelStatus.NOT_DOWNLOADED -> MaterialTheme.colorScheme.onSurfaceVariant to "Fallback"
        ModelStatus.DOWNLOADING -> MaterialTheme.colorScheme.secondary to "Installing"
        ModelStatus.LOADING -> MaterialTheme.colorScheme.secondary to "Starting"
        ModelStatus.READY -> MaterialTheme.colorScheme.primary to "Ready"
        ModelStatus.ERROR -> MaterialTheme.colorScheme.error to "Needs attention"
    }

    val animatedColor by animateColorAsState(targetValue = color, label = "statusColor")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = animatedColor.copy(alpha = 0.2f),
            modifier = Modifier.height(28.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = animatedColor,
                    modifier = Modifier.size(8.dp)
                ) {}
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = animatedColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
