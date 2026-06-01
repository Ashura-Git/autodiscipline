package com.autodiscipline.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.autodiscipline.presentation.components.LineChartComponent
import com.autodiscipline.presentation.viewmodel.DayRecordViewModel
import com.github.mikephil.charting.data.Entry
import java.util.Calendar
import java.util.Date

@Composable
fun StatisticsScreen(navController: NavController, dayRecordViewModel: DayRecordViewModel = hiltViewModel()) {
    val allDayRecords by dayRecordViewModel.allDayRecords.collectAsState()

    val totalDaysRecorded = allDayRecords.size
    val totalCompletedTasks = allDayRecords.sumOf { it.completedTasks.size }
    val totalFailedTasks = allDayRecords.sumOf { it.failedTasks.size }
    val overallSuccessPercentage = if (totalCompletedTasks + totalFailedTasks > 0) {
        (totalCompletedTasks.toFloat() / (totalCompletedTasks + totalFailedTasks) * 100)
    } else 0f

    // Placeholder for streaks - actual calculation would be more complex
    val currentStreak = 0
    val bestStreak = 0

    // Dummy data for charts
    val weeklyEntries = remember { mutableStateListOf<Entry>() }
    val weeklyLabels = remember { mutableStateListOf<String>() }

    val monthlyEntries = remember { mutableStateListOf<Entry>() }
    val monthlyLabels = remember { mutableStateListOf<String>() }

    val globalProgressionEntries = remember { mutableStateListOf<Entry>() }
    val globalProgressionLabels = remember { mutableStateListOf<String>() }

    LaunchedEffect(allDayRecords) {
        // Populate dummy data for demonstration
        if (allDayRecords.isNotEmpty()) {
            // Weekly data (last 7 days)
            val calendar = Calendar.getInstance()
            for (i in 6 downTo 0) {
                calendar.time = Date()
                calendar.add(Calendar.DAY_OF_YEAR, -i)
                val date = calendar.time
                val record = allDayRecords.find { it.date.day == date.day && it.date.month == date.month && it.date.year == date.year }
                val successRate = record?.let { (it.completedTasks.size.toFloat() / (it.completedTasks.size + it.failedTasks.size)) * 100 } ?: 0f
                weeklyEntries.add(Entry(i.toFloat(), successRate))
                weeklyLabels.add(SimpleDateFormat("EEE", Locale.getDefault()).format(date))
            }

            // Monthly data (last 30 days, simplified)
            for (i in 29 downTo 0) {
                calendar.time = Date()
                calendar.add(Calendar.DAY_OF_YEAR, -i)
                val date = calendar.time
                val record = allDayRecords.find { it.date.day == date.day && it.date.month == date.month && it.date.year == date.year }
                val successRate = record?.let { (it.completedTasks.size.toFloat() / (it.completedTasks.size + it.failedTasks.size)) * 100 } ?: 0f
                monthlyEntries.add(Entry(i.toFloat(), successRate))
                monthlyLabels.add(SimpleDateFormat("dd", Locale.getDefault()).format(date))
            }

            // Global progression (all records)
            allDayRecords.sortedBy { it.date }.forEachIndexed { index, record ->
                val successRate = (record.completedTasks.size.toFloat() / (record.completedTasks.size + record.failedTasks.size)) * 100
                globalProgressionEntries.add(Entry(index.toFloat(), successRate))
                globalProgressionLabels.add(SimpleDateFormat("dd/MM", Locale.getDefault()).format(record.date))
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Statistiques",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text("Nombre total de jours enregistrés: $totalDaysRecorded", style = MaterialTheme.typography.bodyLarge)
        Text("Nombre de tâches réalisées: $totalCompletedTasks", style = MaterialTheme.typography.bodyLarge)
        Text("Nombre de tâches non réalisées: $totalFailedTasks", style = MaterialTheme.typography.bodyLarge)
        Text(String.format("Pourcentage global de réussite: %.2f%%", overallSuccessPercentage), style = MaterialTheme.typography.bodyLarge)
        Text("Série actuelle: $currentStreak", style = MaterialTheme.typography.bodyLarge)
        Text("Meilleure série: $bestStreak", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Graphique hebdomadaire",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (weeklyEntries.isNotEmpty()) {
            LineChartComponent(modifier = Modifier.fillMaxWidth().height(200.dp), entries = weeklyEntries, labels = weeklyLabels)
        } else {
            Text("Pas assez de données pour le graphique hebdomadaire.")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Graphique mensuel",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (monthlyEntries.isNotEmpty()) {
            LineChartComponent(modifier = Modifier.fillMaxWidth().height(200.dp), entries = monthlyEntries, labels = monthlyLabels)
        } else {
            Text("Pas assez de données pour le graphique mensuel.")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Progression globale",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (globalProgressionEntries.isNotEmpty()) {
            LineChartComponent(modifier = Modifier.fillMaxWidth().height(200.dp), entries = globalProgressionEntries, labels = globalProgressionLabels)
        } else {
            Text("Pas assez de données pour la progression globale.")
        }
    }
}
