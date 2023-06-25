package com.litgo

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.litgo.ui.theme.views.CameraView
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : ComponentActivity() {

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var photoUri: Uri


    // RETRIEVED & ADAPTED FROM: https://github.com/Kilo-Loco/content/tree/main/android/camera-jetpack-compose
    // Launches the given permission request
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.i("Litgo", "Permission granted")
        } else {
            Log.i("Litgo", "Permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // RETRIEVED & ADAPTED FROM: https://github.com/Kilo-Loco/content/tree/main/android/camera-jetpack-compose
            CameraView(
                outputDirectory = outputDirectory,
                executor = cameraExecutor,
                onImageCaptured = ::handleImageCapture,
                onError = { Log.e("kilo", "View error:", it) }
            )

        }

        requestCameraPermission()

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    // RETRIEVED & ADAPTED FROM: https://github.com/Kilo-Loco/content/tree/main/android/camera-jetpack-compose
    // requests the user for permission to use the Camera if they have not already done so
    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("Litgo", "Permission previously granted")
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> Log.i("Litgo", "Show camera permissions dialog")

            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // RETRIEVED & ADAPTED FROM: https://github.com/Kilo-Loco/content/tree/main/android/camera-jetpack-compose
    // Handles image capture.
    private fun handleImageCapture(uri: Uri) {
        Log.i("Litgo", "Image captured: $uri")
        photoUri = uri
    }

    // RETRIEVED & ADAPTED FROM: https://github.com/Kilo-Loco/content/tree/main/android/camera-jetpack-compose
    // returns the directory which photos are stored
    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        // camera must be shutdown
        cameraExecutor.shutdown()
    }
}