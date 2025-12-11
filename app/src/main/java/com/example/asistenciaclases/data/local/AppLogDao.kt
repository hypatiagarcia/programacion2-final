package com.example.asistenciaclases.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AppLogDao {
    @Insert
    suspend fun insertLog(log: AppLog)

    @Query("SELECT * FROM log_table ORDER BY timestamp DESC")
    fun getAllLogs(): LiveData<List<AppLog>>
}
