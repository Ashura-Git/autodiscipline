package com.autodiscipline.util

import java.time.LocalTime

interface AlarmScheduler {
    fun scheduleAlarm(time: LocalTime)
    fun cancelAlarm()
}
