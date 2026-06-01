package com.autodiscipline.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "day_records")
data class DayRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Date,
    val completedTasks: List<Int>, // List of task IDs that were completed
    val failedTasks: List<Int> // List of task IDs that were not completed
)
