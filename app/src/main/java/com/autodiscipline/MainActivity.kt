package com.autodiscipline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
                AutodisciplineApp()
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
        containerColor = Color(0xFF0A0A0F),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color(0xFF0D0D15),
                    contentColor = Color(0xFF9B59B6)
                ) {
                    NavigationBarItem(
                        selected = currentRoute == Screen.Home.route,
                        onClick = { navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }},
                        icon = { Text("⚔", fontSize = 20.sp) },
                        label = { Text("Quête", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedTextColor = Color(0xFF9B59B6),
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color(0xFF1A0F2A)
                        )
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.History.route,
                        onClick = { navController.navigate(Screen.History.route) {
                            popUpTo(Screen.Home.route)
                        }},
                        icon = { Text("📜", fontSize = 20.sp) },
                        label = { Text("Historique", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedTextColor = Color(0xFF9B59B6),
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color(0xFF1A0F2A)
                        )
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Statistics.route,
                        onClick = { navController.navigate(Screen.Statistics.route) {
                            popUpTo(Screen.Home.route)
                        }},
                        icon = { Text("📊", fontSize = 20.sp) },
                        label = { Text("Stats", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedTextColor = Color(0xFF9B59B6),
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color(0xFF1A0F2A)
                        )
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
            composable(Screen.Welcome.route) { WelcomeScreen(navController = navController) }
            composable(Screen.Home.route) { HomeScreen(navController = navController) }
            composable(Screen.History.route) { HistoryScreen(navController = navController) }
            composable(Screen.Statistics.route) { StatisticsScreen(navController = navController) }
        }
    }
}
