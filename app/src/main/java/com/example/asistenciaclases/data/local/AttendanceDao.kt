package com.example.asistenciaclases.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance_table ORDER BY date DESC")
    fun getAllAttendance(): LiveData<List<Attendance>>

    @Query("SELECT * FROM attendance_table WHERE isSynced = 0")
    suspend fun getUnsyncedAttendance(): List<Attendance>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(attendance: Attendance)

    @Update
    suspend fun update(attendance: Attendance)
}
