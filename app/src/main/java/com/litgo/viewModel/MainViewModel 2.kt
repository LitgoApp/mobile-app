package com.litgo.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.litgo.data.models.Coordinates
import com.litgo.data.models.LitterSite
import com.litgo.data.models.LitterSiteCreation
import com.litgo.data.repositories.LitterSiteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LitterSiteViewModel(private val repository: LitterSiteRepository) : ViewModel() {
    //decide on littercount, latitude, longitude

    private val _uiState = MutableStateFlow(LitgoUiState())
    val uiState: StateFlow<LitgoUiState> = _uiState
    val imageUris = MutableLiveData<List<Uri>>(emptyList())

    fun addImageUri(uri: Uri) {
        val currentUiState = _uiState.value
        val newCameraUiState = currentUiState.cameraUiState.copy(
            imagesCaptured = currentUiState.cameraUiState.imagesCaptured + uri.toString()
        )
        _uiState.value = currentUiState.copy(cameraUiState = newCameraUiState)
    }

    /*
    fun createLitterSite(imageUri: Uri, harm: String, description: String, latitude: Double, longitude: Double) {
        val imageUriString = imageUri.toString()

        // assume there is a function getLitterCount that calls the AI detector service
        val litterCountDeferred = getLitterCount(imageUri)

        viewModelScope.launch {
            try {
                val litterCount = litterCountDeferred.await()

                val litterSiteCreation = LitterSiteCreation(
                    latitude = latitude,
                    longitude = longitude,
                    harm = harm,
                    description = description,
                    litterCount = litterCount,
                    image = imageUriString
                )

                repository.createLitterSite(litterSiteCreation)
            } catch (e: Exception) {
                Log.e("LitterSiteViewModel", "Error creating litter site", e)
            }
        }
    }
     */

    val nearbyLitterSites = MutableLiveData<List<LitterSite>>()

    fun fetchNearbyLitterSites(userCoords: Coordinates) {
        viewModelScope.launch {
            try {
                val litterSites = repository.getNearbyLitterSites(userCoords)
                nearbyLitterSites.value = litterSites
            } catch (e: Exception) {
                Log.e("LitterSiteViewModel", "Error fetching nearby litter sites", e)
            }
        }
    }
}

