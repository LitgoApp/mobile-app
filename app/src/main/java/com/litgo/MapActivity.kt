package com.litgo
/*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

internal class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }
    private lateinit var mMap: GoogleMap
    private lateinit var litterSiteViewModel: LitterSiteViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_view)

        litterSiteViewModel = ViewModelProvider(this).get(LitterSiteViewModel::class.java)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val userCoords = Coordinates(location.latitude, location.longitude)
                    litterSiteViewModel.fetchNearbyLitterSites(userCoords)

                    litterSiteViewModel.nearbyLitterSites.observe(this) { litterSites ->
                        // Clear old markers
                        mMap.clear()

                        // Add a new marker for each litter site
                        litterSites.forEach { litterSite ->
                            val position = LatLng(litterSite.latitude, litterSite.longitude)
                            mMap.addMarker(MarkerOptions()
                                .position(position)
                                .title("Litter Site")
                                .snippet("Harm: ${litterSite.harm}, Description: ${litterSite.description}")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                        }

                        // Add a marker for the user's current location
                        val userPosition = LatLng(userCoords.latitude, userCoords.longitude)
                        mMap.addMarker(MarkerOptions()
                            .position(userPosition)
                            .title("Your Location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                    }
                }
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getLastLocation()
    }
}

 */