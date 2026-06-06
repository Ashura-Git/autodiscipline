package com.autodiscipline.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.autodiscipline.data.model.DayRecord
import com.autodiscipline.presentation.viewmodel.DailyTaskViewModel
import com.autodiscipline.presentation.viewmodel.DayRecordViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HistoryScreen(
    navController: NavController,
    dayRecordViewModel: DayRecordViewModel = hiltViewModel(),
    dailyTaskViewModel: DailyTaskViewModel = hiltViewModel()
) {
    val allDayRecords by dayRecordViewModel.allDayRecords.collectAsState()
    val allTasks by dailyTaskViewModel.dailyTasks.collectAsState()
    val dateFormatter = remember { SimpleDateFormat("EEEE d MMMM yyyy", Locale.getDefault()) }
    var selectedRecord by remember { mutableStateOf<DayRecord?>(null) }

    if (selectedRecord != null) {
        AlertDialog(
            onDismissRequest = { selectedRecord = null },
            containerColor = Color(0xFF12121A),
            titleContentColor = Color(0xFF9B59B6),
            title = {
                Text(
                    dateFormatter.format(selectedRecord!!.date).uppercase(),
                    fontSize = 14.sp,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Black
                )
            },
            text = {
                LazyColumn {
                    item {
                        Text("✅ COMPLÉTÉES", fontSize = 11.sp, color = Color(0xFF2ECC71), letterSpacing = 2.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    items(selectedRecord!!.completedTasks) { taskId ->
                        val task = allTasks.find { it.id == taskId }
                        Text("• ${task?.name ?: "Tâche #$taskId"}", color = Color.White, fontSize = 13.sp, modifier = Modifier.padding(vertical = 2.dp))
                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("❌ ÉCHOUÉES", fontSize = 11.sp, color = Color(0xFFE74C3C), letterSpacing = 2.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    items(selectedRecord!!.failedTasks) { taskId ->
                        val task = allTasks.find { it.id == taskId }
                        Text("• ${task?.name ?: "Tâche #$taskId"}", color = Color.Gray, fontSize = 13.sp, modifier = Modifier.padding(vertical = 2.dp))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedRecord = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9B59B6))
                ) { Text("FERMER", fontWeight = FontWeight.Black) }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0F))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "⚔ HISTORIQUE",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF9B59B6),
                letterSpacing = 4.sp,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )

            Divider(color = Color(0xFF2C2C4A), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            if (allDayRecords.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Aucune quête enregistrée",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn {
                    items(allDayRecords.sortedByDescending { it.date }) { record ->
                        SoloDayRecordItem(
                            dayRecord = record,
                            dateFormatter = dateFormatter,
                            onClick = { selectedRecord = record }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SoloDayRecordItem(dayRecord: DayRecord, dateFormatter: SimpleDateFormat, onClick: () -> Unit) {
    val total = dayRecord.completedTasks.size + dayRecord.failedTasks.size
    val percent = if (total > 0) (dayRecord.completedTasks.size * 100 / total) else 0
    val borderColor = when {
        percent >= 80 -> Color(0xFF9B59B6)
        percent >= 50 -> Color(0xFF3498DB)
        else -> Color(0xFFE74C3C)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF12121A))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = dateFormatter.format(dayRecord.date).uppercase(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "✅ ${dayRecord.completedTasks.size}   ❌ ${dayRecord.failedTasks.size}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
            Text(
                text = "$percent%",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = borderColor
            )
        }
    }
}
