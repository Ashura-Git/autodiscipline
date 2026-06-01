package com.autodiscipline.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.autodiscipline.data.model.WakeUpTime
import kotlinx.coroutines.flow.Flow

@Dao
interface WakeUpTimeDao {
    @Query("SELECT * FROM wake_up_time LIMIT 1")
    fun getWakeUpTime(): Flow<WakeUpTime?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWakeUpTime(wakeUpTime: WakeUpTime)
}
