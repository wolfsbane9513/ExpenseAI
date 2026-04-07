package com.expenseai.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.expenseai.ui.screens.dashboard.DashboardScreen
import com.expenseai.ui.screens.history.HistoryScreen
import com.expenseai.ui.screens.insights.InsightsScreen
import com.expenseai.ui.screens.scan.ScanReceiptScreen
import com.expenseai.ui.screens.sources.SourcesScreen
import com.expenseai.ui.screens.sources.SourcesViewModel

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    data object Scan      : Screen("scan",      "Scan",      Icons.Default.CameraAlt)
    data object History   : Screen("history",   "History",   Icons.Default.History)
    data object Insights  : Screen("insights",  "Insights",  Icons.Default.Analytics)
    data object Sources   : Screen("sources",   "Sources",   Icons.Default.ReceiptLong)
}

val bottomNavItems = listOf(
    Screen.Dashboard, Screen.Scan, Screen.History, Screen.Insights, Screen.Sources
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Hoist SourcesViewModel here to read pending badge count
    val sourcesViewModel: SourcesViewModel = hiltViewModel()
    val pendingCount by sourcesViewModel.pendingCount.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = {
                            if (screen is Screen.Sources && pendingCount > 0) {
                                BadgedBox(badge = { Badge { Text(pendingCount.toString()) } }) {
                                    Icon(screen.icon, contentDescription = screen.label)
                                }
                            } else {
                                Icon(screen.icon, contentDescription = screen.label)
                            }
                        },
                        label = { Text(screen.label) },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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
            composable(Screen.Dashboard.route) { DashboardScreen() }
            composable(Screen.Scan.route)      { ScanReceiptScreen() }
            composable(Screen.History.route)   { HistoryScreen() }
            composable(Screen.Insights.route)  { InsightsScreen() }
            composable(Screen.Sources.route)   { SourcesScreen(viewModel = sourcesViewModel) }
        }
    }
}
