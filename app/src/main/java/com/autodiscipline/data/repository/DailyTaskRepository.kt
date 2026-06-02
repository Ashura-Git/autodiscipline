package com.autodiscipline.data.repository

import com.autodiscipline.data.dao.DailyTaskDao
import com.autodiscipline.data.model.DailyTask
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DailyTaskRepository @Inject constructor(
    private val dailyTaskDao: DailyTaskDao
) {
    fun getAllDailyTasks(): Flow<List<DailyTask>> = dailyTaskDao.getAllDailyTasks()

    suspend fun insertDailyTask(dailyTask: DailyTask) = dailyTaskDao.insertDailyTask(dailyTask)

    suspend fun insertAllDailyTasks(dailyTasks: List<DailyTask>) = dailyTaskDao.insertAllDailyTasks(dailyTasks)

    suspend fun getTaskCount(): Int = dailyTaskDao.getTaskCount()
}
