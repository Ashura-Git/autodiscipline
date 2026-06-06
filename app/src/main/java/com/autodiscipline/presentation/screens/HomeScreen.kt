package com.autodiscipline.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.autodiscipline.data.model.DailyTask
import com.autodiscipline.presentation.viewmodel.DailyTaskViewModel
import com.autodiscipline.presentation.viewmodel.DayRecordViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController,
    dailyTaskViewModel: DailyTaskViewModel = hiltViewModel(),
    dayRecordViewModel: DayRecordViewModel = hiltViewModel()
) {
    val dailyTasks by dailyTaskViewModel.dailyTasks.collectAsState()
    val checkedStates = dailyTasks.associate { it.id to it.isChecked }
    val observationStates = dailyTasks.associate { it.id to it.observation }
    val currentDay = remember { Calendar.getInstance().time }
    val dateFormatter = remember { SimpleDateFormat("EEEE d MMMM yyyy", Locale.getDefault()) }
    var showObservationDialog by remember { mutableStateOf(false) }
    var selectedTaskId by remember { mutableStateOf<Int?>(null) }

    val completedCount = checkedStates.values.count { it }
    val totalCount = dailyTasks.size

    LaunchedEffect(Unit) {
        dailyTaskViewModel.insertPredefinedTasksIfEmpty()
        dailyTaskViewModel.addMissingTasks()
    }

    if (showObservationDialog && selectedTaskId != null) {
        val taskName = dailyTasks.find { it.id == selectedTaskId }?.name ?: ""
        AlertDialog(
            onDismissRequest = { showObservationDialog = false },
            containerColor = Color(0xFF12121A),
            titleContentColor = Color(0xFF9B59B6),
            title = { Text("📝 NOTE", letterSpacing = 2.sp, fontWeight = FontWeight.Black) },
            text = {
                Column {
                    Text(taskName, color = Color.White, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = observationStates[selectedTaskId] ?: "",
                        onValueChange = { dailyTaskViewModel.setObservation(selectedTaskId!!, it) },
                        label = { Text("Votre note", color = Color.Gray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF9B59B6),
                            unfocusedBorderColor = Color(0xFF2C2C4A),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showObservationDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9B59B6))
                ) { Text("OK", fontWeight = FontWeight.Black) }
            },
            dismissButton = {
                TextButton(onClick = { showObservationDialog = false }) {
                    Text("Annuler", color = Color.Gray)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0F))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Titre
            Text(
                text = "⚔ QUÊTE QUOTIDIENNE",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF9B59B6),
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = dateFormatter.format(currentDay).uppercase(),
                fontSize = 11.sp,
                color = Color(0xFF3498DB),
                letterSpacing = 2.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )

            // Barre de progression
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF12121A))
                    .border(1.dp, Color(0xFF2C2C4A), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("PROGRESSION", fontSize = 10.sp, color = Color(0xFF9B59B6), letterSpacing = 2.sp)
                        Text("$completedCount / $totalCount", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f,
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color = Color(0xFF9B59B6),
                        trackColor = Color(0xFF2C2C4A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Liste des tâches
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(dailyTasks) { task ->
                    SoloTaskItem(
                        task = task,
                        isChecked = checkedStates[task.id] ?: false,
                        observation = observationStates[task.id] ?: "",
                        onCheckedChange = { checked -> dailyTaskViewModel.setChecked(task.id, checked) },
                        onObservationClick = {
                            selectedTaskId = task.id
                            showObservationDialog = true
                        }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            // Bouton enregistrer
            Button(
                onClick = {
                    val completedTasks = dailyTasks.filter { checkedStates[it.id] == true }.map { it.id }
                    val failedTasks = dailyTasks.filter { checkedStates[it.id] == false }.map { it.id }
                    val dayRecord = com.autodiscipline.data.model.DayRecord(
                        date = currentDay,
                        completedTasks = completedTasks,
                        failedTasks = failedTasks
                    )
                    dayRecordViewModel.saveDayRecord(dayRecord)
                    dailyTaskViewModel.resetAll()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9B59B6)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "⚔ TERMINER LA QUÊTE",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

@Composable
fun SoloTaskItem(
    task: DailyTask,
    isChecked: Boolean,
    observation: String,
    onCheckedChange: (Boolean) -> Unit,
    onObservationClick: () -> Unit
) {
    val borderColor = if (isChecked) Color(0xFF9B59B6) else Color(0xFF2C2C4A)
    val bgColor = if (isChecked) Color(0xFF1A0F2A) else Color(0xFF12121A)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF9B59B6),
                        uncheckedColor = Color(0xFF2C2C4A),
                        checkmarkColor = Color.White
                    )
                )
                Column {
                    Text(
                        text = task.name,
                        color = if (isChecked) Color(0xFFCCA0E8) else Color.White,
                        fontSize = 14.sp,
                        fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal
                    )
                    if (observation.isNotEmpty()) {
                        Text(
                            text = "📝 $observation",
                            color = Color(0xFF3498DB),
                            fontSize = 11.sp
                        )
                    }
                }
            }
            TextButton(onClick = onObservationClick) {
                Text("+", color = Color(0xFF9B59B6), fontSize = 18.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}
