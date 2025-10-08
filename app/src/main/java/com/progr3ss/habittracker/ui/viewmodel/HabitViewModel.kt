package com.progr3ss.habittracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progr3ss.habittracker.data.repository.HabitRepository
import com.progr3ss.habittracker.domain.model.Habit
import com.progr3ss.habittracker.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {

    private val _createHabitState = MutableStateFlow<Resource<Habit>?>(null)
    val createHabitState: StateFlow<Resource<Habit>?> = _createHabitState.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    fun createHabit(name: String, category: String, goal: String) {
        viewModelScope.launch {
            habitRepository.createHabit(name, category, goal).collect { resource ->
                _createHabitState.value = resource
            }
        }
    }

    fun fetchCategories() {
        viewModelScope.launch {
            habitRepository.getCategories().collect { resource ->
                if (resource is Resource.Success) {
                    _categories.value = resource.data ?: emptyList()
                }
            }
        }
    }

    fun resetCreateHabitState() {
        _createHabitState.value = null
    }
}
