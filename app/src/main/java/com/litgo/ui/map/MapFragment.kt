package com.litgo.ui.map

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.litgo.MainActivity
import com.litgo.R
import com.litgo.data.models.Coordinates
import com.litgo.data.models.LitterSite
import com.litgo.databinding.FragmentMapBinding
import com.litgo.viewModel.LitterSiteViewModel
import android.Manifest
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.google.android.gms.location.Priority

class MapFragment : Fragment(), OnMapReadyCallback, LocationListener {

    private val viewModel: LitterSiteViewModel by viewModels()

    private var _binding: FragmentMapBinding? = null

    private lateinit var latLng: LatLng
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var litterInfoFragment: LitterSiteInfoFragment
    private lateinit var userCoords: Coordinates
    private lateinit var locationCallback : LocationCallback
    private lateinit var locationRequest : LocationRequest
    private var locationPermissionGranted = false

    private fun startLocationUpdates() {
        if (locationPermissionGranted) {

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)

    }


    private fun closeBannerContainer() {
        val bannerContainer = childFragmentManager.findFragmentById(R.id.cardHolder) as SupportMapFragment
        if (bannerContainer != null) {
            childFragmentManager.beginTransaction()
                .hide(bannerContainer)
                .commit()
        }
    }


    override fun onLocationChanged(location: Location) {
        // Update the map with the new location
        latLng = LatLng(location.latitude, location.longitude)

        val userCoords = Coordinates(location.latitude, location.longitude)
        userCoords.let {
            viewModel.fetchNearbyLitterSites(it)
            viewModel.fetchNearbyDisposalSites(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val locationRequestBuilder = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
        locationRequestBuilder.setMinUpdateDistanceMeters(10.0F)

        locationRequest = locationRequestBuilder.build()

        startLocationUpdates()


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // update UI through the onLocationChanged method
                    onLocationChanged(location)
                }
            }
        }






    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        val bannerContainer = childFragmentManager.findFragmentById(R.id.cardHolder) as SupportMapFragment

        if (bannerContainer != null) {
            childFragmentManager.beginTransaction()
                .hide(bannerContainer)
                .commit()
        }

        _binding!!.centerCurrentLocationButton.setOnClickListener {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        }

        _binding!!.bannerCloseButton.setOnClickListener {
            closeBannerContainer()
        }

        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setLocationSource(this)  // Fix this?

        // Fetch nearby litter sites and disposal sites
        viewModel.fetchNearbyLitterSites(userCoords)
        viewModel.fetchNearbyDisposalSites(userCoords)

        // Observe litter sites and add markers to map
        viewModel.nearbyLitterSites.observe(viewLifecycleOwner) { litterSites ->
            // Clear old markers
            mMap.clear()

            // Add a new marker for each litter site
            litterSites.forEach { litterSite ->
                val position = LatLng(litterSite.latitude, litterSite.longitude)
                val marker = mMap.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title("Litter Site")
                        .snippet("Harm: ${litterSite.harm}, Description: ${litterSite.description}")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                marker?.tag = litterSite.id
            }
        }

        // Observe disposal sites and add markers to map
        viewModel.nearbyDisposalSites.observe(viewLifecycleOwner) { disposalSites ->
            // Add a new marker for each disposal site
            disposalSites.forEach { disposalSite ->
                val position = LatLng(disposalSite.latitude, disposalSite.longitude)
                mMap.addMarker(MarkerOptions()
                    .position(position)
                    .title("Disposal Site")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
            }
        }

        // Add a marker for the user's current location
        val userPosition = latLng
        mMap.addMarker(MarkerOptions()
            .position(userPosition)
            .title("Your Location")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))

        mMap.setOnMarkerClickListener { marker ->
            marker.tag?.let {
                if (it is LitterSite) {
                    // get the id of it as littersite
                    // query the datalyer and return the specific litter based on ID
                    viewModel.setSelectedLitterSite(it.id, userCoords)

                    val bannerContainer = childFragmentManager.findFragmentById(R.id.cardHolder) as SupportMapFragment

                    // show the banner container
                    if (bannerContainer != null) {
                        childFragmentManager.beginTransaction()
                            .show(bannerContainer)
                            .commit()
                    }

                    litterInfoFragment = LitterSiteInfoFragment()
                    val fragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragmentContainer, litterInfoFragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
            }
            true
        }
    }


    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
