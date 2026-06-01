package com.autodiscipline.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "observations")
data class Observation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dayRecordId: Long,
    val taskId: Int,
    val note: String
)
