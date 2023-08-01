package com.litgo.ui.map

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.litgotesting.viewModel.DisposalSiteUiState
import com.example.litgotesting.viewModel.LitterSiteUiState
import com.example.litgotesting.viewModel.UserUiState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.litgo.R
import com.litgo.data.models.Coordinates
import com.litgo.databinding.FragmentMapBinding
import com.litgo.viewModel.LitgoViewModel
import kotlinx.coroutines.launch

class LitterMapFragment: Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var litterInfoFragment: LitterSiteInfoFragment
    private var userLocation = LatLng(0.0, 0.0)

    private val viewModel: LitgoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                //viewModel.getNearbyLitterSites(
                //    Coordinates(uiState.userUiState.latitude, uiState.userUiState.longitude)
                //)
            }
        }



        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bannerCloseButton.setOnClickListener {
            binding.cardHolder.visibility = View.INVISIBLE
        }

        litterInfoFragment = childFragmentManager.findFragmentById(R.id.fragmentContainer) as LitterSiteInfoFragment



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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    val litterSites = uiState.mapUiState.nearbyLitterSites
                    val disposalSites = uiState.mapUiState.nearbyDisposalSites
                    userLocation = LatLng(uiState.userUiState.latitude, uiState.userUiState.longitude)

                    litterInfoFragment.updateFromUiState(uiState.mapUiState)


                    val userMarker = googleMap.addMarker(MarkerOptions()
                        .position(userLocation)
                        .title("My Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    )

                    userMarker?.tag = uiState.userUiState


                    for (site in litterSites) {
                        val litterMarker = googleMap.addMarker(MarkerOptions()
                            .position(LatLng(site.latitude, site.longitude))
                            .title("Litter Site")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        )

                        litterMarker?.tag = site
                    }

                    for (site in disposalSites) {
                        val disposalMarker = googleMap.addMarker(MarkerOptions()
                            .position(LatLng(site.latitude, site.longitude))
                            .title("Litter Site")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        )

                        disposalMarker?.tag = site
                    }


                }
            }


        }



        googleMap.setOnMarkerClickListener { marker ->
            marker.tag?.let {
                val bannerContainer = binding.cardHolder
                if (it is LitterSiteUiState) {
                    // viewModel.setSelectedLitterSite(marker.tag) TODO: Fix this call!
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 15f))
                    bannerContainer.visibility = View.VISIBLE

                } else if (it is UserUiState) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 15f))
                    bannerContainer.visibility = View.INVISIBLE
                } else if (it is DisposalSiteUiState){
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 15f))
                    bannerContainer.visibility = View.INVISIBLE
                }
            }
            true
        }


        binding.centerCurrentLocationButton.setOnClickListener {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
        }
    }
}