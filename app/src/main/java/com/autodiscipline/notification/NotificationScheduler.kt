package com.autodiscipline.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

object NotificationScheduler {

    fun scheduleDaily21h(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 21)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, DisciplineReceiver::class.java).apply {
            putExtra("type", "check")
            putExtra("title", "⚠ SYSTÈME DE DISCIPLINE")
            putExtra("message", "Il est 21h00 — Avez-vous accompli votre quête aujourd'hui ?")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, 100, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun sendSuccessNotification(context: Context) {
        val intent = Intent(context, DisciplineReceiver::class.java).apply {
            putExtra("type", "success")
            putExtra("title", "🏆 QUÊTE ACCOMPLIE !")
            putExtra("message", "Félicitations Chasseur ! Vous avez accompli toutes vos tâches aujourd'hui. Votre niveau augmente ! Continuez ainsi demain.")
        }
        context.sendBroadcast(intent)
    }

    fun sendFailureNotification(context: Context, failedCount: Int, punishmentTask: String) {
        val intent = Intent(context, DisciplineReceiver::class.java).apply {
            putExtra("type", "failure")
            putExtra("title", "⚠ PUNITION DU SYSTÈME")
            putExtra("message", "Vous avez échoué $failedCount tâche(s) aujourd'hui.\n\n🔴 TÂCHE PUNITION : $punishmentTask\n\nAccomplissez-la maintenant !")
        }
        context.sendBroadcast(intent)
    }
}
