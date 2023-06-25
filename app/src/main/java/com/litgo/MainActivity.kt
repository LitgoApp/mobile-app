package com.litgo

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.litgo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}


//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.result.contract.ActivityResultContracts
//
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//
//import com.litgo.ui.theme.views.*
//import java.io.File
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//
//class MainActivity : ComponentActivity() {
//
//    private lateinit var outputDirectory: File
//    private lateinit var cameraExecutor: ExecutorService
//
//    private lateinit var photoUri: Uri
//
//
//    // RETRIEVED & ADAPTED FROM: https://github.com/Kilo-Loco/content/tree/main/android/camera-jetpack-compose
//    // Launches the given permission request
//    private val requestPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        if (isGranted) {
//            Log.i("Litgo", "Permission granted")
//        } else {
//            Log.i("Litgo", "Permission denied")
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            // RETRIEVED & ADAPTED FROM: https://github.com/Kilo-Loco/content/tree/main/android/camera-jetpack-compose
//            CameraView(
//                outputDirectory = outputDirectory,
//                executor = cameraExecutor,
//                onImageCaptured = ::handleImageCapture,
//                onError = { Log.e("Litgo", "View error:", it) }
//            )
//        }
//
//        requestCameraPermission()
//
//        outputDirectory = getOutputDirectory()
//        cameraExecutor = Executors.newSingleThreadExecutor()
//    }
//
//    // RETRIEVED & ADAPTED FROM: https://github.com/Kilo-Loco/content/tree/main/android/camera-jetpack-compose
//    // requests the user for permission to use the Camera if they have not already done so
//    private fun requestCameraPermission() {
//        when {
//            ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_GRANTED -> {
//                Log.i("Litgo", "Permission previously granted")
//            }
//
//            ActivityCompat.shouldShowRequestPermissionRationale(
//                this,
//                Manifest.permission.CAMERA
//            ) -> Log.i("Litgo", "Show camera permissions dialog")
//
//            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
//        }
//    }
//
//    // RETRIEVED & ADAPTED FROM: https://github.com/Kilo-Loco/content/tree/main/android/camera-jetpack-compose
//    // Handles image capture.
//    private fun handleImageCapture(uri: Uri) {
//        Log.i("Litgo", "Image captured: $uri")
//        photoUri = uri
//    }
//
//    // RETRIEVED & ADAPTED FROM: https://github.com/Kilo-Loco/content/tree/main/android/camera-jetpack-compose
//    // returns the directory which photos are stored
//    private fun getOutputDirectory(): File {
//        val mediaDir = externalMediaDirs.firstOrNull()?.let {
//            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
//        }
//
//        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        // camera must be shutdown
//        cameraExecutor.shutdown()
//    }
//}