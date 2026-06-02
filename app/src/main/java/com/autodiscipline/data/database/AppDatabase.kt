package com.autodiscipline.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.autodiscipline.data.dao.DailyTaskDao
import com.autodiscipline.data.dao.DayRecordDao
import com.autodiscipline.data.dao.WakeUpTimeDao
import com.autodiscipline.data.model.DailyTask
import com.autodiscipline.data.model.DayRecord
import com.autodiscipline.data.model.Observation
import android.content.Context
import com.autodiscipline.data.model.WakeUpTime

@Database(entities = [WakeUpTime::class, DailyTask::class, DayRecord::class, Observation::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wakeUpTimeDao(): WakeUpTimeDao
    abstract fun dailyTaskDao(): DailyTaskDao
    abstract fun dayRecordDao(): DayRecordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "autodiscipline.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
