package com.expenseai.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.expenseai.domain.model.Expense
import com.expenseai.domain.model.getCategoryById
import com.expenseai.ui.screens.review.SourceBadge
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ExpenseCard(
    expense: Expense,
    modifier: Modifier = Modifier,
    fireImpactDays: Long? = null,
    onClick: () -> Unit = {}
) {
    val category = getCategoryById(expense.category)
    val formatter = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }
    }

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Box (CRED Style)
            Surface(
                shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                color = Color(0xFF111111),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.05f)
                ),
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    // Subtle inner glow effect
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), androidx.compose.foundation.shape.RoundedCornerShape(14.dp))
                    )
                    Icon(
                        imageVector = category.icon,
                        contentDescription = category.label,
                        tint = category.color,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.vendor,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = "${category.label.uppercase()} • ${expense.date}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    
                    if (fireImpactDays != null && fireImpactDays > 0) {
                        Surface(
                            color = Color(0xFFFF5252).copy(alpha = 0.1f),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFFF5252).copy(alpha = 0.2f))
                        ) {
                            Text(
                                text = "+$fireImpactDays FIRE DAYS",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFFF5252),
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "-${formatter.format(expense.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
                // Small pulse indicator dots
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(modifier = Modifier.size(4.dp).background(Color(0xFF222222), androidx.compose.foundation.shape.CircleShape))
                    Box(modifier = Modifier.size(4.dp).background(Color(0xFF222222), androidx.compose.foundation.shape.CircleShape))
                }
            }
        }
    }
}
