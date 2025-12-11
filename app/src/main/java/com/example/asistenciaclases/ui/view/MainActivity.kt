package com.example.asistenciaclases.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.asistenciaclases.R
import com.example.asistenciaclases.data.local.AppDatabase
import com.example.asistenciaclases.data.repository.AttendanceRepository
import com.example.asistenciaclases.ui.viewmodel.AttendanceViewModel
import com.example.asistenciaclases.ui.viewmodel.AttendanceViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: AttendanceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = AppDatabase.getDatabase(this)
        val repository = AttendanceRepository(database.attendanceDao(), database.appLogDao())
        val factory = AttendanceViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(AttendanceViewModel::class.java)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AttendanceListFragment())
                .commit()
        }
    }
}
