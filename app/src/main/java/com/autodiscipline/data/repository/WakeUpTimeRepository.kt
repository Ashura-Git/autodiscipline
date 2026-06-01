package com.autodiscipline.data.repository

import com.autodiscipline.data.dao.WakeUpTimeDao
import com.autodiscipline.data.model.WakeUpTime
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WakeUpTimeRepository @Inject constructor(private val wakeUpTimeDao: WakeUpTimeDao) {
    fun getWakeUpTime(): Flow<WakeUpTime?> = wakeUpTimeDao.getWakeUpTime()

    suspend fun insertWakeUpTime(wakeUpTime: WakeUpTime) {
        wakeUpTimeDao.insertWakeUpTime(wakeUpTime)
    }
}
