package com.autodiscipline.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.autodiscipline.data.model.DailyTask
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyTaskDao {
    @Query("SELECT * FROM daily_tasks ORDER BY `order` ASC")
    fun getAllDailyTasks(): Flow<List<DailyTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyTask(dailyTask: DailyTask)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDailyTasks(dailyTasks: List<DailyTask>)

    @Query("SELECT COUNT(*) FROM daily_tasks")
    suspend fun getTaskCount(): Int

    @Query("UPDATE daily_tasks SET isChecked = :checked WHERE id = :taskId")
    suspend fun updateChecked(taskId: Int, checked: Boolean)

    @Query("UPDATE daily_tasks SET observation = :observation WHERE id = :taskId")
    suspend fun updateObservation(taskId: Int, observation: String)

    @Query("UPDATE daily_tasks SET isChecked = 0, observation = ''")
    suspend fun resetAll()
}
