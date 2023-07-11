package com.litgo.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.litgo.data.models.LitterSiteCreation
import com.litgo.data.repositories.LitterSiteRepository
import kotlinx.coroutines.launch

class LitterSiteViewModel(private val repository: LitterSiteRepository) : ViewModel() {
    //decide on littercount, latitude, longitude
    fun createLitterSite(imageUri: Uri, harm: String, description: String, litterCount: Int, latitude: Double, longitude: Double) {
        val imageUriString = imageUri.toString()

        val litterSiteCreation = LitterSiteCreation(
            latitude = latitude,
            longitude = longitude,
            harm = harm,
            description = description,
            litterCount = litterCount,
            image = imageUriString
        )

        viewModelScope.launch {
            repository.createLitterSite(litterSiteCreation)
        }
    }
}
