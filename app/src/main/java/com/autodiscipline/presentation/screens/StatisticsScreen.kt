package com.autodiscipline.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.autodiscipline.presentation.components.LineChartComponent
import com.autodiscipline.presentation.viewmodel.DayRecordViewModel
import com.github.mikephil.charting.data.Entry
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

val SoloBg = Color(0xFF0A0A0F)
val SoloPurple = Color(0xFF9B59B6)
val SoloBlue = Color(0xFF3498DB)
val SoloGold = Color(0xFFF39C12)
val SoloCard = Color(0xFF12121A)
val SoloBorder = Color(0xFF2C2C4A)

@Composable
fun StatisticsScreen(navController: NavController, dayRecordViewModel: DayRecordViewModel = hiltViewModel()) {
    val allDayRecords by dayRecordViewModel.allDayRecords.collectAsState()

    val totalDaysRecorded = allDayRecords.size
    val totalCompletedTasks = allDayRecords.sumOf { it.completedTasks.size }
    val totalFailedTasks = allDayRecords.sumOf { it.failedTasks.size }
    val overallSuccessPercentage = if (totalCompletedTasks + totalFailedTasks > 0) {
        (totalCompletedTasks.toFloat() / (totalCompletedTasks + totalFailedTasks) * 100)
    } else 0f

    val weeklyEntries = remember { mutableStateListOf<Entry>() }
    val weeklyLabels = remember { mutableStateListOf<String>() }
    val globalProgressionEntries = remember { mutableStateListOf<Entry>() }
    val globalProgressionLabels = remember { mutableStateListOf<String>() }

    LaunchedEffect(allDayRecords) {
        weeklyEntries.clear()
        weeklyLabels.clear()
        globalProgressionEntries.clear()
        globalProgressionLabels.clear()

        if (allDayRecords.isNotEmpty()) {
            val calendar = Calendar.getInstance()
            for (i in 6 downTo 0) {
                calendar.time = Date()
                calendar.add(Calendar.DAY_OF_YEAR, -i)
                val date = calendar.time
                val record = allDayRecords.find {
                    it.date.day == date.day &&
                    it.date.month == date.month &&
                    it.date.year == date.year
                }
                val total = (record?.completedTasks?.size ?: 0) + (record?.failedTasks?.size ?: 0)
                val successRate = if (total > 0) {
                    ((record?.completedTasks?.size ?: 0).toFloat() / total) * 100
                } else 0f
                weeklyEntries.add(Entry(i.toFloat(), successRate))
                weeklyLabels.add(SimpleDateFormat("EEE", Locale.getDefault()).format(date))
            }

            allDayRecords.sortedBy { it.date }.forEachIndexed { index, record ->
                val total = record.completedTasks.size + record.failedTasks.size
                val successRate = if (total > 0) {
                    (record.completedTasks.size.toFloat() / total) * 100
                } else 0f
                globalProgressionEntries.add(Entry(index.toFloat(), successRate))
                globalProgressionLabels.add(SimpleDateFormat("dd/MM", Locale.getDefault()).format(record.date))
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoloBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Titre
            Text(
                text = "⚔ STATISTIQUES",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = SoloPurple,
                letterSpacing = 4.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )

            Divider(color = SoloBorder, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Stats cards
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard(modifier = Modifier.weight(1f), title = "JOURS", value = "$totalDaysRecorded", color = SoloBlue)
                StatCard(modifier = Modifier.weight(1f), title = "RÉUSSIES", value = "$totalCompletedTasks", color = Color(0xFF2ECC71))
                StatCard(modifier = Modifier.weight(1f), title = "ÉCHOUÉES", value = "$totalFailedTasks", color = Color(0xFFE74C3C))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Taux de réussite
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoloCard)
                    .border(1.dp, SoloPurple, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("TAUX DE RÉUSSITE GLOBAL", fontSize = 12.sp, color = SoloPurple, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = String.format("%.1f%%", overallSuccessPercentage),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        color = SoloGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Graphique hebdomadaire
            SoloChartSection(
                title = "PROGRESSION HEBDOMADAIRE",
                entries = weeklyEntries,
                labels = weeklyLabels
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Graphique global
            SoloChartSection(
                title = "PROGRESSION GLOBALE",
                entries = globalProgressionEntries,
                labels = globalProgressionLabels
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, title: String, value: String, color: Color) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(SoloCard)
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontSize = 10.sp, color = color, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.White)
        }
    }
}

@Composable
fun SoloChartSection(title: String, entries: List<Entry>, labels: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SoloCard)
            .border(1.dp, SoloBorder, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(title, fontSize = 11.sp, color = SoloBlue, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(8.dp))
            if (entries.isNotEmpty()) {
                LineChartComponent(
                    modifier = Modifier.fillMaxWidth().height(180.dp),
                    entries = entries,
                    labels = labels
                )
            } else {
                Text(
                    "Aucune donnée disponible",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}
