package com.example.asistenciaclases.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asistenciaclases.R
import com.example.asistenciaclases.ui.viewmodel.AttendanceViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AttendanceListFragment : Fragment() {

    private lateinit var viewModel: AttendanceViewModel
    private lateinit var adapter: AttendanceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_attendance_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add)

        adapter = AttendanceAdapter { attendance ->
            val fragment = AddAttendanceFragment()
            val bundle = Bundle()
            bundle.putParcelable("attendance", attendance)
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel = (activity as MainActivity).viewModel

        viewModel.allAttendance.observe(viewLifecycleOwner) { attendanceList ->
            adapter.submitList(attendanceList)
        }

        fab.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddAttendanceFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sync -> {
                viewModel.syncData()
                Toast.makeText(context, "Sincronizando...", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
