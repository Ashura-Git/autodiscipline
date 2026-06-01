package com.autodiscipline.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autodiscipline.data.model.WakeUpTime
import com.autodiscipline.data.repository.WakeUpTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.autodiscipline.util.AlarmScheduler
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class WakeUpTimeViewModel @Inject constructor(
    private val repository: WakeUpTimeRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val _wakeUpTime = MutableStateFlow<WakeUpTime?>(null)
    val wakeUpTime: StateFlow<WakeUpTime?> = _wakeUpTime

    init {
        viewModelScope.launch {
            repository.getWakeUpTime().collect { time ->
                _wakeUpTime.value = time
            }
        }
    }

    fun saveWakeUpTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            repository.insertWakeUpTime(WakeUpTime(hour = hour, minute = minute))
            alarmScheduler.scheduleAlarm(LocalTime.of(hour, minute))
        }
    }
}
