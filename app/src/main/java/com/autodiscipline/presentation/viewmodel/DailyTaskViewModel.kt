package com.autodiscipline.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autodiscipline.data.model.DailyTask
import com.autodiscipline.data.repository.DailyTaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyTaskViewModel @Inject constructor(
    private val repository: DailyTaskRepository
) : ViewModel() {

    private val _dailyTasks = MutableStateFlow<List<DailyTask>>(emptyList())
    val dailyTasks: StateFlow<List<DailyTask>> = _dailyTasks

    val checkedStates = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val observationStates = MutableStateFlow<Map<Int, String>>(emptyMap())

    init {
        viewModelScope.launch {
            repository.getAllDailyTasks().collect { tasks ->
                _dailyTasks.value = tasks
                if (checkedStates.value.isEmpty()) {
                    checkedStates.value = tasks.associate { it.id to false }
                }
            }
        }
    }

    fun setChecked(taskId: Int, checked: Boolean) {
        checkedStates.value = checkedStates.value.toMutableMap().also { it[taskId] = checked }
    }

    fun setObservation(taskId: Int, text: String) {
        observationStates.value = observationStates.value.toMutableMap().also { it[taskId] = text }
    }

    fun resetAll() {
        checkedStates.value = _dailyTasks.value.associate { it.id to false }
        observationStates.value = emptyMap()
    }

    fun insertDailyTask(dailyTask: DailyTask) {
        viewModelScope.launch { repository.insertDailyTask(dailyTask) }
    }

    fun insertAllDailyTasks(dailyTasks: List<DailyTask>) {
        viewModelScope.launch { repository.insertAllDailyTasks(dailyTasks) }
    }

    fun insertPredefinedTasksIfEmpty() {
        viewModelScope.launch {
            if (repository.getTaskCount() == 0) {
                repository.insertAllDailyTasks(getPredefinedTasks())
            }
        }
    }

    fun getPredefinedTasks(): List<DailyTask> {
        return listOf(
            DailyTask(name = "Se réveiller à l'heure choisie", order = 1),
            DailyTask(name = "Prière du matin", order = 2),
            DailyTask(name = "Sport (1 à 2 h)", order = 3),
            DailyTask(name = "Douche froide", order = 4),
            DailyTask(name = "Méditation (10 à 20 min)", order = 5),
            DailyTask(name = "Lecture matinale (1 à 1 h 30)", order = 6),
            DailyTask(name = "Planification de la journée", order = 7),
            DailyTask(name = "Ne pas se masturber", order = 8),
            DailyTask(name = "Étude concentrée (1 h 30 à 2 h)", order = 9),
            DailyTask(name = "1 h sans Internet (12 h à 13 h)", order = 10),
            DailyTask(name = "Révision et préparation du lendemain", order = 11),
            DailyTask(name = "Bilan de la journée", order = 12),
            DailyTask(name = "Prière du soir", order = 13)
        )
    }
}
