package com.autodiscipline.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_tasks")
data class DailyTask(
    @PrimaryKey
    val id: Int,
    val name: String,
    val order: Int,
    val isChecked: Boolean = false,
    val observation: String = ""
)
