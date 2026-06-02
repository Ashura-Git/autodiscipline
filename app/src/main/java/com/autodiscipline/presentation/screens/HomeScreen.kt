
package com.autodiscipline.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.autodiscipline.data.model.DailyTask
import com.autodiscipline.presentation.viewmodel.DailyTaskViewModel
import com.autodiscipline.presentation.viewmodel.DayRecordViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController, dailyTaskViewModel: DailyTaskViewModel = hiltViewModel(), dayRecordViewModel: DayRecordViewModel = hiltViewModel()) {
    val dailyTasks by dailyTaskViewModel.dailyTasks.collectAsState()
    val currentDay = remember { Calendar.getInstance().time }
    val dateFormatter = remember { SimpleDateFormat("EEEE d MMMM yyyy", Locale.getDefault()) }
    val checkedStates = remember { mutableStateMapOf<Int, Boolean>() }
    val observationStates = remember { mutableStateMapOf<Int, String>() }
    var showObservationDialog by remember { mutableStateOf(false) }
    var selectedTaskId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        dailyTaskViewModel.insertPredefinedTasksIfEmpty()
    }

    LaunchedEffect(dailyTasks) {
        dailyTasks.forEach { task ->
            if (!checkedStates.containsKey(task.id)) {
                checkedStates[task.id] = false
            }
        }
    }

    if (showObservationDialog && selectedTaskId != null) {
        val taskName = dailyTasks.find { it.id == selectedTaskId }?.name ?: ""
        AlertDialog(
            onDismissRequest = { showObservationDialog = false },
            title = { Text("Observation") },
            text = {
                Column {
                    Text(taskName, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = observationStates[selectedTaskId] ?: "",
                        onValueChange = { observationStates[selectedTaskId!!] = it },
                        label = { Text("Votre observation") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showObservationDialog = false }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showObservationDialog = false }) { Text("Annuler") }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Discipline Quotidienne",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = dateFormatter.format(currentDay),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(dailyTasks) { task ->
                TaskItem(
                    task = task,
                    isChecked = checkedStates[task.id] ?: false,
                    observation = observationStates[task.id] ?: "",
                    onCheckedChange = { checked -> checkedStates[task.id] = checked },
                    onObservationClick = {
                        selectedTaskId = task.id
                        showObservationDialog = true
                    }
                )
            }
        }

        Button(onClick = {
            val completedTasks = dailyTasks.filter { checkedStates[it.id] == true }.map { it.id }
            val failedTasks = dailyTasks.filter { checkedStates[it.id] == false }.map { it.id }
            val dayRecord = com.autodiscipline.data.model.DayRecord(
                date = currentDay,
                completedTasks = completedTasks,
                failedTasks = failedTasks
            )
            dayRecordViewModel.saveDayRecord(dayRecord)
            checkedStates.keys.forEach { key -> checkedStates[key] = false }
            observationStates.clear()
        }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text(text = "Enregistrer la journée")
        }
    }
}

@Composable
fun TaskItem(
    task: DailyTask,
    isChecked: Boolean,
    observation: String,
    onCheckedChange: (Boolean) -> Unit,
    onObservationClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
                Column {
                    Text(text = task.name, style = MaterialTheme.typography.bodyLarge)
                    if (observation.isNotEmpty()) {
                        Text(
                            text = "📝 $observation",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            TextButton(onClick = onObservationClick) {
                Text("+ Note")
            }
        }
    }
}
