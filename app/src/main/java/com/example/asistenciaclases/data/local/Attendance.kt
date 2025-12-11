package com.example.asistenciaclases.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "attendance_table")
data class Attendance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentName: String,
    val date: String,
    val photoPath: String,
    val isSynced: Boolean = false
) : Parcelable
