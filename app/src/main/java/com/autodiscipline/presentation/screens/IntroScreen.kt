package com.autodiscipline.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.navigation.NavController
import com.autodiscipline.presentation.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun IntroScreen(navController: NavController) {

    var phase by remember { mutableStateOf(0) }
    // 0 = rien, 1 = "Vous pouvez le faire", 2 = "Solo Leveling", 3 = "Arise"

    LaunchedEffect(Unit) {
        delay(400)
        phase = 1
        delay(1800)
        phase = 0
        delay(600)
        phase = 2
        delay(1800)
        phase = 0
        delay(600)
        phase = 3
        delay(2000)
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Intro.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0F)),
        contentAlignment = Alignment.Center
    ) {
        // Phase 1
        AnimatedVisibility(
            visible = phase == 1,
            enter = fadeIn(animationSpec = tween(600)) +
                    slideInVertically(animationSpec = tween(600)) { 40 },
            exit = fadeOut(animationSpec = tween(500))
        ) {
            Text(
                text = "Vous pouvez le faire.",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5DADE2),
                textAlign = TextAlign.Center,
                letterSpacing = 2.sp
            )
        }

        // Phase 2
        AnimatedVisibility(
            visible = phase == 2,
            enter = fadeIn(animationSpec = tween(600)) +
                    scaleIn(animationSpec = tween(600, easing = EaseOutBack)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            Text(
                text = "SOLO LEVELING",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF9B59B6),
                textAlign = TextAlign.Center,
                letterSpacing = 6.sp
            )
        }

        // Phase 3
        AnimatedVisibility(
            visible = phase == 3,
            enter = fadeIn(animationSpec = tween(400)) +
                    scaleIn(
                        initialScale = 0.5f,
                        animationSpec = tween(600, easing = EaseOutBack)
                    ),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            Text(
                text = "ARISE.",
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center,
                letterSpacing = 8.sp
            )
        }
    }
}
