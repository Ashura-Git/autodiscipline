package com.autodiscipline.di

import android.content.Context
import androidx.room.Room
import com.autodiscipline.data.database.AppDatabase
import com.autodiscipline.data.dao.DailyTaskDao
import com.autodiscipline.data.dao.DayRecordDao
import com.autodiscipline.data.dao.WakeUpTimeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "autodiscipline.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideWakeUpTimeDao(database: AppDatabase): WakeUpTimeDao {
        return database.wakeUpTimeDao()
    }

    @Singleton
    @Provides
    fun provideDailyTaskDao(database: AppDatabase): DailyTaskDao {
        return database.dailyTaskDao()
    }

    @Singleton
    @Provides
    fun provideDayRecordDao(database: AppDatabase): DayRecordDao {
        return database.dayRecordDao()
    }
}
