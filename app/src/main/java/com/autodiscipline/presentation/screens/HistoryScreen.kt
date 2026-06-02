package com.autodiscipline.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.autodiscipline.data.model.DailyTask
import com.autodiscipline.data.model.DayRecord
import com.autodiscipline.presentation.viewmodel.DailyTaskViewModel
import com.autodiscipline.presentation.viewmodel.DayRecordViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HistoryScreen(navController: NavController, dayRecordViewModel: DayRecordViewModel = hiltViewModel(), dailyTaskViewModel: DailyTaskViewModel = hiltViewModel()) {
    val allDayRecords by dayRecordViewModel.allDayRecords.collectAsState()
    val allTasks by dailyTaskViewModel.dailyTasks.collectAsState()
    val dateFormatter = remember { SimpleDateFormat("EEEE d MMMM yyyy", Locale.getDefault()) }
    var selectedRecord by remember { mutableStateOf<DayRecord?>(null) }

    if (selectedRecord != null) {
        AlertDialog(
            onDismissRequest = { selectedRecord = null },
            title = { Text(dateFormatter.format(selectedRecord!!.date)) },
            text = {
                LazyColumn {
                    val completed = selectedRecord!!.completedTasks
                    val failed = selectedRecord!!.failedTasks
                    item {
                        Text("✅ Tâches réalisées", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    items(completed) { taskId ->
                        val task = allTasks.find { it.id == taskId }
                        Text("• ${task?.name ?: "Tâche #$taskId"}", modifier = Modifier.padding(vertical = 2.dp))
                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("❌ Tâches non réalisées", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    items(failed) { taskId ->
                        val task = allTasks.find { it.id == taskId }
                        Text("• ${task?.name ?: "Tâche #$taskId"}", modifier = Modifier.padding(vertical = 2.dp))
                    }
                }
            },
            confirmButton = {
                Button(onClick = { selectedRecord = null }) { Text("Fermer") }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Historique des Journées",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (allDayRecords.isEmpty()) {
            Text("Aucun enregistrement de journée disponible.")
        } else {
            LazyColumn {
                items(allDayRecords) { record ->
                    DayRecordItem(
                        dayRecord = record,
                        dateFormatter = dateFormatter,
                        onClick = { selectedRecord = record }
                    )
                }
            }
        }
    }
}

@Composable
fun DayRecordItem(dayRecord: DayRecord, dateFormatter: SimpleDateFormat, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = dateFormatter.format(dayRecord.date),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            val total = dayRecord.completedTasks.size + dayRecord.failedTasks.size
            val percent = if (total > 0) (dayRecord.completedTasks.size * 100 / total) else 0
            Text("✅ ${dayRecord.completedTasks.size} réalisées  ❌ ${dayRecord.failedTasks.size} non réalisées")
            Text("Taux de réussite : $percent%", style = MaterialTheme.typography.bodySmall)
        }
    }
}
