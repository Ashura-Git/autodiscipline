package com.autodiscipline.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromIntList(value: String?): List<Int>? {
        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toIntList(list: List<Int>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromObservationList(value: String?): List<com.autodiscipline.data.model.Observation>? {
        val listType = object : TypeToken<List<com.autodiscipline.data.model.Observation>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toObservationList(list: List<com.autodiscipline.data.model.Observation>?): String? {
        return Gson().toJson(list)
    }
}
