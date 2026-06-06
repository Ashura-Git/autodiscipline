package com.autodiscipline.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.autodiscipline.data.dao.DailyTaskDao
import com.autodiscipline.data.dao.DayRecordDao
import com.autodiscipline.data.dao.WakeUpTimeDao
import com.autodiscipline.data.model.DailyTask
import com.autodiscipline.data.model.DayRecord
import com.autodiscipline.data.model.Observation
import android.content.Context
import com.autodiscipline.data.model.WakeUpTime

@Database(
    entities = [WakeUpTime::class, DailyTask::class, DayRecord::class, Observation::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wakeUpTimeDao(): WakeUpTimeDao
    abstract fun dailyTaskDao(): DailyTaskDao
    abstract fun dayRecordDao(): DayRecordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE IF EXISTS daily_tasks")
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS daily_tasks (
                        id INTEGER PRIMARY KEY NOT NULL,
                        name TEXT NOT NULL,
                        `order` INTEGER NOT NULL,
                        isChecked INTEGER NOT NULL DEFAULT 0,
                        observation TEXT NOT NULL DEFAULT ''
                    )
                """)
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "autodiscipline.db"
                )
                .addMigrations(MIGRATION_1_2)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
