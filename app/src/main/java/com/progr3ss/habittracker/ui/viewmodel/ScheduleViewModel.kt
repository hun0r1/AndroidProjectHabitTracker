package com.progr3ss.habittracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progr3ss.habittracker.data.repository.ScheduleRepository
import com.progr3ss.habittracker.domain.model.Schedule
import com.progr3ss.habittracker.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private val _createScheduleState = MutableStateFlow<Resource<Schedule>?>(null)
    val createScheduleState: StateFlow<Resource<Schedule>?> = _createScheduleState.asStateFlow()

    private val _updateScheduleState = MutableStateFlow<Resource<Schedule>?>(null)
    val updateScheduleState: StateFlow<Resource<Schedule>?> = _updateScheduleState.asStateFlow()

    private val _deleteScheduleState = MutableStateFlow<Resource<Unit>?>(null)
    val deleteScheduleState: StateFlow<Resource<Unit>?> = _deleteScheduleState.asStateFlow()

    fun createSchedule(
        habitId: String,
        startTime: String,
        endTime: String,
        repeatPattern: String,
        date: String
    ) {
        viewModelScope.launch {
            scheduleRepository.createSchedule(habitId, startTime, endTime, repeatPattern, date)
                .collect { resource ->
                    _createScheduleState.value = resource
                }
        }
    }

    fun updateSchedule(
        scheduleId: String,
        startTime: String? = null,
        endTime: String? = null,
        status: String? = null,
        notes: String? = null
    ) {
        viewModelScope.launch {
            scheduleRepository.updateSchedule(scheduleId, startTime, endTime, status, notes)
                .collect { resource ->
                    _updateScheduleState.value = resource
                }
        }
    }

    fun deleteSchedule(scheduleId: String) {
        viewModelScope.launch {
            scheduleRepository.deleteSchedule(scheduleId).collect { resource ->
                _deleteScheduleState.value = resource
            }
        }
    }

    fun resetCreateScheduleState() {
        _createScheduleState.value = null
    }

    fun resetUpdateScheduleState() {
        _updateScheduleState.value = null
    }

    fun resetDeleteScheduleState() {
        _deleteScheduleState.value = null
    }
}
