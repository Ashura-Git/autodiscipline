package com.autodiscipline

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.autodiscipline.data.dao.WakeUpTimeDao
import com.autodiscipline.data.database.AppDatabase
import com.autodiscipline.data.repository.WakeUpTimeRepository
import com.autodiscipline.util.AlarmScheduler
import com.autodiscipline.util.AndroidAlarmScheduler
import com.autodiscipline.util.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalTime

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        val alarmScheduler: AlarmScheduler = AndroidAlarmScheduler(context)
        val notificationHelper = NotificationHelper(context)

        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("AlarmReceiver", "Boot completed, re-scheduling alarms")
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(context)
                val wakeUpTimeDao = db.wakeUpTimeDao()
                val wakeUpTimeRepository = WakeUpTimeRepository(wakeUpTimeDao)
                val wakeUpTime = wakeUpTimeRepository.getWakeUpTime().firstOrNull()
                wakeUpTime?.let {
                    alarmScheduler.scheduleAlarm(LocalTime.of(it.hour, it.minute))
                    Log.d("AlarmReceiver", "Alarm re-scheduled for ${it.hour}:${it.minute}")
                }
            }
        } else {
            Log.d("AlarmReceiver", "Daily alarm triggered")
            notificationHelper.showAlarmNotification("C'est l'heure de se réveiller", "Il est temps de commencer votre journée de discipline !")
        }
    }
}
