package com.autodiscipline.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autodiscipline.data.model.DayRecord
import com.autodiscipline.data.model.Observation
import com.autodiscipline.data.repository.DayRecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DayRecordViewModel @Inject constructor(
    private val repository: DayRecordRepository
) : ViewModel() {

    private val _allDayRecords = MutableStateFlow<List<DayRecord>>(emptyList())
    val allDayRecords: StateFlow<List<DayRecord>> = _allDayRecords

    private val _currentDayRecord = MutableStateFlow<DayRecord?>(null)
    val currentDayRecord: StateFlow<DayRecord?> = _currentDayRecord

    init {
        viewModelScope.launch {
            repository.getAllDayRecords().collect { records ->
                _allDayRecords.value = records
            }
        }
    }

    fun saveDayRecord(dayRecord: DayRecord) {
        viewModelScope.launch {
            repository.insertDayRecord(dayRecord)
        }
    }

    fun getDayRecordForDate(date: Date) {
        viewModelScope.launch {
            _currentDayRecord.value = repository.getDayRecordByDate(date)
        }
    }

    fun addObservation(observation: Observation) {
        viewModelScope.launch {
            repository.insertObservation(observation)
        }
    }

    // TODO: Implement statistics calculation logic here
    // This will involve processing _allDayRecords to get total days, task counts, streaks, etc.
}
