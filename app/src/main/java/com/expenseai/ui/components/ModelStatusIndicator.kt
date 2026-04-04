package com.expenseai.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.expenseai.ai.ModelStatus

@Composable
fun ModelStatusIndicator(
    status: ModelStatus,
    modifier: Modifier = Modifier
) {
    val (color, label) = when (status) {
        ModelStatus.NOT_DOWNLOADED -> Color.Gray to "AI: Not Downloaded"
        ModelStatus.DOWNLOADING -> Color(0xFFFFA726) to "AI: Downloading..."
        ModelStatus.LOADING -> Color(0xFFFFA726) to "AI: Loading..."
        ModelStatus.READY -> Color(0xFF66BB6A) to "AI: Ready"
        ModelStatus.ERROR -> Color(0xFFEF5350) to "AI: Error"
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
                    color = animatedColor
                )
            }
        }
    }
}
