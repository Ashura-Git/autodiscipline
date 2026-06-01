package com.autodiscipline.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController, dailyTaskViewModel: DailyTaskViewModel = hiltViewModel(), dayRecordViewModel: DayRecordViewModel = hiltViewModel()) {
    val dailyTasks by dailyTaskViewModel.dailyTasks.collectAsState()
    val currentDay = remember { Calendar.getInstance().time }
    val dateFormatter = remember { SimpleDateFormat("EEEE d MMMM yyyy", Locale.getDefault()) }

    // State to hold the checked status of each task
    val checkedStates = remember { mutableStateMapOf<Int, Boolean>() }

    LaunchedEffect(dailyTasks) {
        if (dailyTasks.isEmpty()) {
            dailyTaskViewModel.insertAllDailyTasks(dailyTaskViewModel.getPredefinedTasks())
        }
        // Initialize checked states when tasks are loaded
        dailyTasks.forEach { task ->
            if (!checkedStates.containsKey(task.id)) {
                checkedStates[task.id] = false
            }
        }
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
            items(dailyTasks) {
                TaskItem(task = it, isChecked = checkedStates[it.id] ?: false) {
                    checkedStates[it.id] = it
                }
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
            // Reset checked states for the next day
            checkedStates.keys.forEach { key -> checkedStates[key] = false }
            // TODO: Navigate to a confirmation screen or show a toast
        }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text(text = "Enregistrer la journée")
        }
    }
}

@Composable
fun TaskItem(task: DailyTask, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
            Text(text = task.name, style = MaterialTheme.typography.bodyLarge)
        }
        Button(onClick = { /* TODO: Handle observation */ }) {
            Text(text = "Observation")
        }
    }
}
