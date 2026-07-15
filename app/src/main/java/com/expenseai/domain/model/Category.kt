package com.expenseai.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val color: Color
)

val DEFAULT_CATEGORIES = listOf(
    Category("food", "Food & Dining", Icons.Filled.Restaurant, Color(0xFFFF6B6B)),
    Category("transport", "Transport", Icons.Filled.DirectionsCar, Color(0xFF4ECDC4)),
    Category("utilities", "Utilities", Icons.Filled.Lightbulb, Color(0xFFFFE66D)),
    Category("shopping", "Shopping", Icons.Filled.ShoppingBag, Color(0xFF95E1D3)),
    Category("entertainment", "Entertainment", Icons.Filled.Movie, Color(0xFFDDA0DD)),
    Category("health", "Health", Icons.Filled.MedicalServices, Color(0xFF98D8C8)),
    Category("travel", "Travel", Icons.Filled.Flight, Color(0xFFF7DC6F)),
    Category("other", "Other", Icons.Filled.Inventory2, Color(0xFFAED6F1))
)

fun getCategoryById(id: String): Category =
    DEFAULT_CATEGORIES.find { it.id == id } ?: DEFAULT_CATEGORIES.last()
