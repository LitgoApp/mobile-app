package com.litgo.ui.map

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.litgo.R
import com.litgo.data.models.Coordinates
import com.litgo.databinding.FragmentMapBinding
import com.litgo.viewModel.LitterSiteViewModel

class LitterMapFragment: Fragment(), OnMapReadyCallback, LocationListener {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var latLng: LatLng

    private val viewModel: LitterSiteViewModel by viewModels()

    private fun closeBannerContainer() {
        val bannerContainer = binding.cardHolder
        bannerContainer.visibility = View.INVISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapContainer = binding.mapContainer
        if (childFragmentManager.findFragmentById(mapContainer.id) == null) {
            mapFragment = SupportMapFragment.newInstance()
            childFragmentManager.beginTransaction()
                .replace(mapContainer.id, mapFragment)
                .commit()
        }

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(MarkerOptions()
            .position(LatLng(0.0, 0.0))
            .title("Marker"))
        closeBannerContainer()
    }

    override fun onLocationChanged(location: Location) {
        // Update the map with the new location
        latLng = LatLng(location.latitude, location.longitude)

        val userCoordinates = Coordinates(location.latitude, location.longitude)
        userCoordinates.let {
            // TODO: Change these to the new viewModel functions
            // viewModel.fetchNearbyLitterSites(it)
            // viewModel.fetchNearbyDisposalSites(it)
        }
    }

}