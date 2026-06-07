package com.autodiscipline.presentation.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome_screen")
    object Intro : Screen("intro_screen")
    object Home : Screen("home_screen")
    object History : Screen("history_screen")
    object Statistics : Screen("statistics_screen")
    object TaskDetail : Screen("task_detail_screen/{dayRecordId}/{taskId}") {
        fun createRoute(dayRecordId: Long, taskId: Int) = "task_detail_screen/$dayRecordId/$taskId"
    }
}
