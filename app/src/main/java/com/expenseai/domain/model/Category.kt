package com.expenseai.domain.model

import androidx.compose.ui.graphics.Color

data class Category(
    val id: String,
    val label: String,
    val icon: String,
    val color: Color
)

val DEFAULT_CATEGORIES = listOf(
    Category("food", "Food & Dining", "🍽️", Color(0xFFFF6B6B)),
    Category("transport", "Transport", "🚗", Color(0xFF4ECDC4)),
    Category("utilities", "Utilities", "💡", Color(0xFFFFE66D)),
    Category("shopping", "Shopping", "🛍️", Color(0xFF95E1D3)),
    Category("entertainment", "Entertainment", "🎬", Color(0xFFDDA0DD)),
    Category("health", "Health", "💊", Color(0xFF98D8C8)),
    Category("travel", "Travel", "✈️", Color(0xFFF7DC6F)),
    Category("other", "Other", "📦", Color(0xFFAED6F1))
)

fun getCategoryById(id: String): Category =
    DEFAULT_CATEGORIES.find { it.id == id } ?: DEFAULT_CATEGORIES.last()
