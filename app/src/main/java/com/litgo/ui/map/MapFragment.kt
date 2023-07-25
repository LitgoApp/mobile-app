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
import com.litgo.Manifest
import com.litgo.R
import com.litgo.data.models.Coordinates
import com.litgo.data.models.LitterSite
import com.litgo.databinding.FragmentMapBinding
import com.litgo.viewModel.LitterSiteViewModel

class MapFragment : Fragment(), OnMapReadyCallback, LocationListener {

    private var _binding: FragmentMapBinding? = null

    private lateinit var latLng: LatLng
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var litterSiteViewModel: LitterSiteViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var litterInfoFragment: LitterSiteInfoFragment

    override fun onLocationChanged(location: Location) {
        // Update the map with the new location
        latLng = LatLng(location.latitude, location.longitude)

        val userCoords = Coordinates(location.latitude, location.longitude)
        litterSiteViewModel.fetchNearbyLitterSites(userCoords)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // Update location every 10 seconds
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    for (location in locationResult.locations){
                        // Update UI with location data
                        onLocationChanged(location)
                    }
                }
            }, Looper.getMainLooper())
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

        litterSiteViewModel = ViewModelProvider(this).get(LitterSiteViewModel::class.java)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        _binding!!.centerCurrentLocationButton.setOnClickListener {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        }

        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setLocationSource(this)

        // Fetch nearby litter sites and disposal sites
        litterSiteViewModel.fetchNearbyLitterSites(userCoords)
        litterSiteViewModel.fetchNearbyDisposalSites(userCoords)

        // Observe litter sites and add markers to map
        litterSiteViewModel.nearbyLitterSites.observe(viewLifecycleOwner) { litterSites ->
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
                marker?.tag = litterSite
            }
        }

        // Observe disposal sites and add markers to map
        litterSiteViewModel.nearbyDisposalSites.observe(viewLifecycleOwner) { disposalSites ->
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
                    litterSiteViewModel.selectLitterSite(it)

                    // show info fragment
                    TODO("Add the close button here, so we can manage it in the map fragment")
                    litterInfoFragment = LitterSiteInfoFragment()
                    val fragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.cardHolder, litterInfoFragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()

                }
            }
            true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
