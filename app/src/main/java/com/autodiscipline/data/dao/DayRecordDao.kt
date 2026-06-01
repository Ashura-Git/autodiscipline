package com.autodiscipline.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.autodiscipline.data.model.DayRecord
import com.autodiscipline.data.model.Observation
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface DayRecordDao {
    @Query("SELECT * FROM day_records ORDER BY date DESC")
    fun getAllDayRecords(): Flow<List<DayRecord>>

    @Query("SELECT * FROM day_records WHERE date = :date LIMIT 1")
    suspend fun getDayRecordByDate(date: Date): DayRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDayRecord(dayRecord: DayRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservation(observation: Observation)

    @Transaction
    @Query("SELECT * FROM observations WHERE dayRecordId = :dayRecordId")
    fun getObservationsForDay(dayRecordId: Long): Flow<List<Observation>>
}
