package com.litgo

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.litgo.camera.CameraFragment
import com.litgo.data.models.Coordinates
import com.litgo.databinding.ActivityMainBinding
import com.litgo.ui.RewardsFragment
import com.litgo.ui.user.UserProfileFragment
import com.litgo.ui.litterSite.LitterSiteReportsFragment
import com.litgo.viewModel.LitgoViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val viewModel: LitgoViewModel by viewModels()
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest : LocationRequest
    private lateinit var mCurrentLocation: Location
    private var requestingLocationUpdates: Boolean = false

    private fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        requestingLocationUpdates = true
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun onLocationChanged(location: Location) {
        // Update the map with the new location
        mCurrentLocation = location
        viewModel.updateUserPosition(Coordinates(location.latitude, location.longitude))

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // ACCESS_FINE_LOCATION permission check
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }



        // updates mCurrentLocation to the "Last Known Location"
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    Log.d("Location Services", "User at position: ${location.latitude}, ${location.longitude}")
                    onLocationChanged(location)
                }
            }

        val locationRequestBuilder = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
        // The map will update whenever users move 30m (30.0F)
        locationRequestBuilder.setMinUpdateDistanceMeters(30.0F)
        locationRequest = locationRequestBuilder.build()



        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    // update UI through the onLocationChanged method
                    Log.d("Location Services", "User at position: ${location.latitude}, ${location.longitude}")
                    onLocationChanged(location)
                }
            }
        }

        startLocationUpdates()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {

                }
            }
        }

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val appBarTitleTextView = findViewById<TextView>(R.id.app_bar_title_textview)

        // Add actions to navbar icons
        binding.cameraNavBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_content_main, CameraFragment())
            transaction.commit()
        }
        binding.mapNavBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.commit()
        }
        binding.userProfileNavBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_content_main, UserProfileFragment())
            transaction.commit()
            appBarTitleTextView.text = "My Profile"
        }
        binding.userReportsNavBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_content_main, LitterSiteReportsFragment())
            transaction.commit()
            appBarTitleTextView.text = "My Reports and Cleanups"
        }
        binding.rewardsNavBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_content_main, RewardsFragment())
            transaction.commit()
            appBarTitleTextView.text = "My Rewards"
        }
    }



    /**
     * DO NOT REMOVE BLOCK BELOW FOR NOW; May be required later
     */
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

    /**
     * Handle scrolling up and down navigation
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }


    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }



}