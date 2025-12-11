package com.example.asistenciaclases.data.remote

import com.example.asistenciaclases.data.local.Attendance
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    // Endpoint de prueba. Reemplazar con la URL real del docente.
    @POST("posts") 
    suspend fun sendAttendance(@Body attendance: Attendance): Response<Any>
}
