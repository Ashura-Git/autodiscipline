package com.autodiscipline.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wake_up_time")
data class WakeUpTime(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hour: Int,
    val minute: Int
)
