package com.expenseai.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.expenseai.ui.screens.dashboard.DashboardContent
import com.expenseai.ui.screens.dashboard.DashboardUiState
import com.expenseai.ui.screens.history.HistoryScreen
import com.expenseai.ui.screens.insights.InsightsScreen
import com.expenseai.ui.screens.scan.ScanReceiptScreen
import com.expenseai.ui.theme.ExpenseAITheme
import java.time.YearMonth

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    data object Scan : Screen("scan", "Scan", Icons.Default.CameraAlt)
    data object History : Screen("history", "History", Icons.Default.History)
    data object Insights : Screen("insights", "Insights", Icons.Default.Analytics)
}

val bottomNavItems = listOf(
    Screen.Dashboard,
    Screen.Scan,
    Screen.History,
    Screen.Insights
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseNavHost(
    isShowingPreview: Boolean = false
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                if (isShowingPreview) {
                    DashboardContent(
                        uiState = DashboardUiState(
                            currentMonth = YearMonth.now(),
                            totalSpending = 0.0,
                            categoryTotals = emptyList(),
                            recentExpenses = emptyList()
                        ),
                        onPreviousMonth = {},
                        onNextMonth = {},
                        onAddExpense = { _, _, _, _ -> },
                        onScanClick = { navController.navigate(Screen.Scan.route) }
                    )
                } else {
                    com.expenseai.ui.screens.dashboard.DashboardScreen(
                        onScanClick = { navController.navigate(Screen.Scan.route) }
                    )
                }
            }
            composable(Screen.Scan.route) { ScanReceiptScreen() }
            composable(Screen.History.route) { HistoryScreen() }
            composable(Screen.Insights.route) { InsightsScreen() }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppPreview() {
    ExpenseAITheme {
        ExpenseNavHost(isShowingPreview = true)
    }
}
