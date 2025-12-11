package com.example.asistenciaclases.data.repository

import androidx.lifecycle.LiveData
import com.example.asistenciaclases.data.local.AppLog
import com.example.asistenciaclases.data.local.AppLogDao
import com.example.asistenciaclases.data.local.Attendance
import com.example.asistenciaclases.data.local.AttendanceDao
import com.example.asistenciaclases.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AttendanceRepository(
    private val attendanceDao: AttendanceDao,
    private val appLogDao: AppLogDao
) {

    val allAttendance: LiveData<List<Attendance>> = attendanceDao.getAllAttendance()
    val allLogs: LiveData<List<AppLog>> = appLogDao.getAllLogs()

    suspend fun insert(attendance: Attendance) {
        withContext(Dispatchers.IO) {
            attendanceDao.insert(attendance)
            logEvent("Asistencia creada: ${attendance.studentName}", "INFO")
        }
    }

    suspend fun update(attendance: Attendance) {
        withContext(Dispatchers.IO) {
            attendanceDao.update(attendance)
            logEvent("Asistencia actualizada: ${attendance.studentName}", "INFO")
        }
    }

    suspend fun syncAttendance() {
        withContext(Dispatchers.IO) {
            val unsyncedList = attendanceDao.getUnsyncedAttendance()
            for (attendance in unsyncedList) {
                try {
                    val response = RetrofitClient.instance.sendAttendance(attendance)
                    if (response.isSuccessful) {
                        val updatedAttendance = attendance.copy(isSynced = true)
                        attendanceDao.update(updatedAttendance)
                        logEvent("Sincronización exitosa: ${attendance.studentName}", "SYNC")
                    } else {
                        logEvent("Error al sincronizar: ${attendance.studentName} - Code: ${response.code()}", "ERROR")
                    }
                } catch (e: Exception) {
                    logEvent("Excepción al sincronizar: ${e.message}", "ERROR")
                }
            }
        }
    }

    private suspend fun logEvent(message: String, type: String) {
        val log = AppLog(message = message, timestamp = System.currentTimeMillis(), type = type)
        appLogDao.insertLog(log)
    }
}
