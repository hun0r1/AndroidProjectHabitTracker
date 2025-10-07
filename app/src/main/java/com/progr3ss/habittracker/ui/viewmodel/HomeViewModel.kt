package com.progr3ss.habittracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progr3ss.habittracker.data.repository.ScheduleRepository
import com.progr3ss.habittracker.domain.model.Schedule
import com.progr3ss.habittracker.util.DateUtils
import com.progr3ss.habittracker.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(DateUtils.getCurrentDate())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    val schedules: StateFlow<List<Schedule>> = _selectedDate.flatMapLatest { date ->
        scheduleRepository.getSchedulesByDate(date)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _fetchState = MutableStateFlow<Resource<List<Schedule>>?>(null)
    val fetchState: StateFlow<Resource<List<Schedule>>?> = _fetchState.asStateFlow()

    init {
        fetchSchedules()
    }

    fun fetchSchedules() {
        viewModelScope.launch {
            scheduleRepository.fetchSchedulesByDate(_selectedDate.value).collect { resource ->
                _fetchState.value = resource
            }
        }
    }

    fun setSelectedDate(date: String) {
        _selectedDate.value = date
        fetchSchedules()
    }
}
