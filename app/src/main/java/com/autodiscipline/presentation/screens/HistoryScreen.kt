package com.autodiscipline.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.autodiscipline.data.model.DayRecord
import com.autodiscipline.presentation.viewmodel.DayRecordViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HistoryScreen(navController: NavController, dayRecordViewModel: DayRecordViewModel = hiltViewModel()) {
    val allDayRecords by dayRecordViewModel.allDayRecords.collectAsState()
    val dateFormatter = remember { SimpleDateFormat("EEEE d MMMM yyyy", Locale.getDefault()) }

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
                items(allDayRecords) {\n                    DayRecordItem(dayRecord = it, dateFormatter = dateFormatter) {
                        // TODO: Navigate to detail screen for this day record
                    }
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
            Text("Tâches réalisées: ${dayRecord.completedTasks.size}")
            Text("Tâches non réalisées: ${dayRecord.failedTasks.size}")
            // TODO: Display observations for this day record
        }
    }
}
