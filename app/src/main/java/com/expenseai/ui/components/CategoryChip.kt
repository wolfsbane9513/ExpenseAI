package com.expenseai.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expenseai.domain.model.Category

@Composable
fun CategoryChip(
    category: Category,
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onSelected,
        label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = category.icon)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = category.label)
            }
        },
        modifier = modifier
    )
}
