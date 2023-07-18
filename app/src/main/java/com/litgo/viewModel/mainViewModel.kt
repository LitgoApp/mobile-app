package com.litgo.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.litgo.data.models.LitterSiteCreation
import com.litgo.data.repositories.LitterSiteRepository
import kotlinx.coroutines.launch
/*
class LitterSiteViewModel(private val repository: LitterSiteRepository) : ViewModel() {
    //decide on littercount, latitude, longitude

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

 */
