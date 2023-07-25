package com.litgo.ui.map

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.litgo.R

class MapFragment : Fragment(), OnMapReadyCallback, LocationListener {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var litterSiteViewModel: LitterSiteViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var litterInfoFragment: LitterSiteInfoFragment

    override fun onLocationChanged(location: Location) {
        // Update the map with the new location
        val latLng = LatLng(location.latitude, location.longitude)

        // This will restrict users from navigating the map entirely.
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

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
            // Fetch the last known location directly
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
                }
            }
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
                val marker = mMap.addMarker(MarkerOptions()
                    .position(position)
                    .title("Litter Site")
                    .snippet("Harm: ${litterSite.harm}, Description: ${litterSite.description}")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                marker.tag = litterSite
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
        val userPosition = LatLng(userCoords.latitude, userCoords.longitude)
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
