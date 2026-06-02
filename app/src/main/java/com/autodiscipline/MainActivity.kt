package com.autodiscipline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.autodiscipline.presentation.navigation.Screen
import com.autodiscipline.presentation.screens.HomeScreen
import com.autodiscipline.presentation.screens.HistoryScreen
import com.autodiscipline.presentation.screens.StatisticsScreen
import com.autodiscipline.presentation.screens.WelcomeScreen
import com.autodiscipline.ui.theme.AutodisciplineTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutodisciplineTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AutodisciplineApp()
                }
            }
        }
    }
}

@Composable
fun AutodisciplineApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.History.route,
        Screen.Statistics.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Screen.Home.route,
                        onClick = { navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }},
                        icon = { Text("🏠") },
                        label = { Text("Accueil") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.History.route,
                        onClick = { navController.navigate(Screen.History.route) {
                            popUpTo(Screen.Home.route)
                        }},
                        icon = { Text("📅") },
                        label = { Text("Historique") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Statistics.route,
                        onClick = { navController.navigate(Screen.Statistics.route) {
                            popUpTo(Screen.Home.route)
                        }},
                        icon = { Text("📊") },
                        label = { Text("Statistiques") }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Welcome.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Welcome.route) {
                WelcomeScreen(navController = navController)
            }
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.History.route) {
                HistoryScreen(navController = navController)
            }
            composable(Screen.Statistics.route) {
                StatisticsScreen(navController = navController)
            }
        }
    }
}
