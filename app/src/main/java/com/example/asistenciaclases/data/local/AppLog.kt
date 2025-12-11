package com.example.asistenciaclases.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "log_table")
data class AppLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val message: String,
    val timestamp: Long,
    val type: String // "INFO", "ERROR", "SYNC"
)
