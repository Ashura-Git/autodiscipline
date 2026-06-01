package com.autodiscipline.data.repository

import com.autodiscipline.data.dao.DayRecordDao
import com.autodiscipline.data.model.DayRecord
import com.autodiscipline.data.model.Observation
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class DayRecordRepository @Inject constructor(private val dayRecordDao: DayRecordDao) {
    fun getAllDayRecords(): Flow<List<DayRecord>> = dayRecordDao.getAllDayRecords()

    suspend fun getDayRecordByDate(date: Date): DayRecord? = dayRecordDao.getDayRecordByDate(date)

    suspend fun insertDayRecord(dayRecord: DayRecord): Long = dayRecordDao.insertDayRecord(dayRecord)

    suspend fun insertObservation(observation: Observation) {
        dayRecordDao.insertObservation(observation)
    }

    fun getObservationsForDay(dayRecordId: Long): Flow<List<Observation>> = dayRecordDao.getObservationsForDay(dayRecordId)
}
