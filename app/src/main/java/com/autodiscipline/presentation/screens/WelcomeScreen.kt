package com.autodiscipline.presentation.screens

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.autodiscipline.presentation.navigation.Screen
import com.autodiscipline.presentation.viewmodel.WakeUpTimeViewModel
import java.util.Calendar
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(navController: NavController, viewModel: WakeUpTimeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val wakeUpTime by viewModel.wakeUpTime.collectAsState()

    var selectedHour by remember { mutableStateOf(7) }
    var selectedMinute by remember { mutableStateOf(0) }

    var showSystem by remember { mutableStateOf(false) }
    var showNotification by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val borderAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border"
    )

    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    LaunchedEffect(wakeUpTime) {
        if (wakeUpTime != null) {
            navController.navigate(Screen.Intro.route) {
                popUpTo(Screen.Welcome.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(300)
        showSystem = true
        delay(800)
        showNotification = true
        delay(600)
        showContent = true
        delay(400)
        showButton = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0F)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF1A2A4A).copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            AnimatedVisibility(
                visible = showSystem,
                enter = fadeIn(animationSpec = tween(600)) +
                        slideInVertically(animationSpec = tween(600)) { -50 }
            ) {
                Text(
                    text = "— SYSTÈME —",
                    fontSize = 13.sp,
                    color = Color(0xFF3498DB),
                    letterSpacing = 6.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = showNotification,
                enter = fadeIn(animationSpec = tween(800)) +
                        scaleIn(animationSpec = tween(800, easing = EaseOutBack))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(glowScale)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF0D1520))
                        .border(
                            width = 1.5.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF3498DB).copy(alpha = borderAlpha),
                                    Color(0xFF1A6FA8).copy(alpha = borderAlpha * 0.7f),
                                    Color(0xFF3498DB).copy(alpha = borderAlpha)
                                )
                            ),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(24.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .border(1.dp, Color(0xFF3498DB), RoundedCornerShape(50))
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("!", color = Color(0xFF3498DB), fontWeight = FontWeight.Black, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFF1A3A5A), RoundedCornerShape(2.dp))
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    "NOTIFICATION",
                                    color = Color(0xFF5DADE2),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 3.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        AnimatedVisibility(
                            visible = showContent,
                            enter = fadeIn(animationSpec = tween(600)) +
                                    slideInVertically(animationSpec = tween(600)) { 30 }
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Vous avez été choisi par le",
                                    color = Color(0xFFBDC3C7),
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Système de Discipline.",
                                    color = Color(0xFFBDC3C7),
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Text("Acceptez-vous de devenir un ", color = Color(0xFFBDC3C7), fontSize = 14.sp)
                                    Text("Chasseur", color = Color(0xFF5DADE2), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text(" ?", color = Color(0xFFBDC3C7), fontSize = 14.sp)
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = "DÉFINIR L'HEURE DE RÉVEIL",
                                    color = Color(0xFF3498DB),
                                    fontSize = 10.sp,
                                    letterSpacing = 2.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedButton(
                                    onClick = {
                                        val calendar = Calendar.getInstance()
                                        TimePickerDialog(
                                            context,
                                            { _, h, m ->
                                                selectedHour = h
                                                selectedMinute = m
                                            },
                                            calendar.get(Calendar.HOUR_OF_DAY),
                                            calendar.get(Calendar.MINUTE),
                                            true
                                        ).show()
                                    },
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3498DB)),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF5DADE2)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        String.format("⏰  %02d:%02d", selectedHour, selectedMinute),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(animationSpec = tween(500)) +
                        slideInVertically(animationSpec = tween(500)) { 50 }
            ) {
                Button(
                    onClick = {
                        viewModel.saveWakeUpTime(selectedHour, selectedMinute)
                        Toast.makeText(context, "Bienvenue Chasseur !", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.Intro.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A4A7A)),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "ACCEPTER",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 4.sp,
                        color = Color(0xFF5DADE2)
                    )
                }
            }
        }
    }
}
