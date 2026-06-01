package com.autodiscipline.presentation.screens

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.autodiscipline.presentation.navigation.Screen
import com.autodiscipline.presentation.viewmodel.WakeUpTimeViewModel
import java.util.Calendar

@Composable
fun WelcomeScreen(navController: NavController, viewModel: WakeUpTimeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val wakeUpTime by viewModel.wakeUpTime.collectAsState()

    var selectedHour by remember { mutableStateOf(7) }
    var selectedMinute by remember { mutableStateOf(0) }

    LaunchedEffect(wakeUpTime) {
        if (wakeUpTime != null) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Welcome.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "À quelle heure veux-tu te réveiller chaque matin ?",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(onClick = {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                context,
                { _, h: Int, m: Int ->
                    selectedHour = h
                    selectedMinute = m
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }) {
            Text(text = String.format("Sélectionner l'heure: %02d:%02d", selectedHour, selectedMinute))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.saveWakeUpTime(selectedHour, selectedMinute)
            Toast.makeText(context, "Heure de réveil enregistrée !", Toast.LENGTH_SHORT).show()
            // Navigation will be handled by LaunchedEffect after wakeUpTime is saved
        }) {
            Text(text = "Enregistrer")
        }
    }
}
