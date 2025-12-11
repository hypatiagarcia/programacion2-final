package com.example.asistenciaclases.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asistenciaclases.data.local.AppLog
import com.example.asistenciaclases.data.local.Attendance
import com.example.asistenciaclases.data.repository.AttendanceRepository
import kotlinx.coroutines.launch

class AttendanceViewModel(private val repository: AttendanceRepository) : ViewModel() {

    val allAttendance: LiveData<List<Attendance>> = repository.allAttendance
    val allLogs: LiveData<List<AppLog>> = repository.allLogs

    fun insert(attendance: Attendance) = viewModelScope.launch {
        repository.insert(attendance)
    }

    fun update(attendance: Attendance) = viewModelScope.launch {
        repository.update(attendance)
    }

    fun syncData() = viewModelScope.launch {
        repository.syncAttendance()
    }
}

class AttendanceViewModelFactory(private val repository: AttendanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AttendanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AttendanceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
