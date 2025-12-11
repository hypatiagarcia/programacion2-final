package com.example.asistenciaclases.ui.view

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.asistenciaclases.R
import com.example.asistenciaclases.data.local.Attendance
import java.io.File

class AttendanceAdapter(private val onItemClicked: (Attendance) -> Unit) : ListAdapter<Attendance, AttendanceAdapter.AttendanceViewHolder>(AttendanceComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_attendance, parent, false)
        return AttendanceViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class AttendanceViewHolder(itemView: View, private val onItemClicked: (Attendance) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val tvStudentName: TextView = itemView.findViewById(R.id.tv_student_name)
        private val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        private val tvSynced: TextView = itemView.findViewById(R.id.tv_synced_status)
        private val ivPhoto: ImageView = itemView.findViewById(R.id.iv_photo)

        fun bind(attendance: Attendance) {
            itemView.setOnClickListener { onItemClicked(attendance) }
            tvStudentName.text = attendance.studentName
            tvDate.text = attendance.date
            if (attendance.isSynced) {
                tvSynced.text = "Sincronizado"
                tvSynced.setTextColor(itemView.context.getColor(R.color.green_success))
            } else {
                tvSynced.text = "No Sincronizado"
                tvSynced.setTextColor(itemView.context.getColor(R.color.error))
            }

            if (attendance.photoPath.isNotEmpty()) {
                val imgFile = File(attendance.photoPath)
                if (imgFile.exists()) {
                    val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    ivPhoto.setImageBitmap(myBitmap)
                }
            }
        }
    }

    class AttendanceComparator : DiffUtil.ItemCallback<Attendance>() {
        override fun areItemsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            return oldItem == newItem
        }
    }
}
