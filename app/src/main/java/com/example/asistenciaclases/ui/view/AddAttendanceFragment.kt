package com.example.asistenciaclases.ui.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.asistenciaclases.R
import com.example.asistenciaclases.data.local.Attendance
import com.example.asistenciaclases.ui.viewmodel.AttendanceViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddAttendanceFragment : Fragment() {

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var viewModel: AttendanceViewModel
    private var currentPhotoPath: String = ""

    private lateinit var viewFinder: PreviewView
    private lateinit var ivPreview: ImageView
    private lateinit var etStudentName: EditText
    private var attendanceToEdit: Attendance? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_attendance, container, false)

        viewFinder = view.findViewById(R.id.viewFinder)
        ivPreview = view.findViewById(R.id.iv_preview)
        etStudentName = view.findViewById(R.id.et_student_name)
        val btnCapture = view.findViewById<Button>(R.id.btn_capture)
        val btnSave = view.findViewById<Button>(R.id.btn_save)

        viewModel = (activity as MainActivity).viewModel

        arguments?.let {
            attendanceToEdit = it.getParcelable("attendance")
        }

        attendanceToEdit?.let { attendance ->
            etStudentName.setText(attendance.studentName)
            currentPhotoPath = attendance.photoPath
            if (currentPhotoPath.isNotEmpty()) {
                val imgFile = File(currentPhotoPath)
                if (imgFile.exists()) {
                    val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    ivPreview.setImageBitmap(bitmap)
                    ivPreview.visibility = View.VISIBLE
                    viewFinder.visibility = View.GONE
                }
            }
            btnSave.text = "Actualizar"
        }

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        btnCapture.setOnClickListener { takePhoto() }

        btnSave.setOnClickListener {
            val name = etStudentName.text.toString()
            if (name.isNotBlank() && currentPhotoPath.isNotEmpty()) {
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                
                if (attendanceToEdit != null) {
                    val updatedAttendance = attendanceToEdit!!.copy(
                        studentName = name,
                        photoPath = currentPhotoPath,
                        isSynced = false // Reset sync status on edit
                    )
                    viewModel.update(updatedAttendance)
                    Toast.makeText(context, "Asistencia actualizada", Toast.LENGTH_SHORT).show()
                } else {
                    val attendance = Attendance(studentName = name, date = date, photoPath = currentPhotoPath)
                    viewModel.insert(attendance)
                    Toast.makeText(context, "Asistencia guardada", Toast.LENGTH_SHORT).show()
                }
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(context, "Complete todos los campos y tome una foto", Toast.LENGTH_SHORT).show()
            }
        }

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        return view
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                    currentPhotoPath = photoFile.absolutePath
                    
                    // Show preview
                    val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                    ivPreview.setImageBitmap(bitmap)
                    ivPreview.visibility = View.VISIBLE
                    viewFinder.visibility = View.GONE
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return mediaDir ?: requireActivity().filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
