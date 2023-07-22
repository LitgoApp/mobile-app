package com.litgo.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litgotesting.viewModel.LitgoUiState
import com.example.litgotesting.viewModel.UserUiState
import com.litgo.data.models.Coordinates
import com.litgo.data.models.DisposalSite
import com.litgo.data.models.LitterSite
import com.litgo.data.models.LitterSiteCreation
import com.litgo.data.models.Login
import com.litgo.data.models.Municipality
import com.litgo.data.models.MunicipalityRegistration
import com.litgo.data.models.MunicipalityUpdate
import com.litgo.data.models.UserRegistration
import com.litgo.data.models.UserUpdate
import com.litgo.data.repositories.DisposalSiteRepository
import com.litgo.data.repositories.LitterSiteRepository
import com.litgo.data.repositories.MunicipalityRepository
import com.litgo.data.repositories.RegionRepository
import com.litgo.data.repositories.RewardRepository
import com.litgo.data.repositories.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.Date

class LitterSiteViewModel(
    private val userRepo: UserRepository,
    private val municipalityRepo: MunicipalityRepository,
    private val litterSiteRepo: LitterSiteRepository,
    private val regionRepo: RegionRepository,
    private val rewardRepo: RewardRepository,
    private val disposalSiteRepo: DisposalSiteRepository,
) : ViewModel() {
    //decide on littercount, latitude, longitude

    private val _uiState = MutableStateFlow(LitgoUiState())
    val uiState: StateFlow<LitgoUiState> = _uiState.asStateFlow()

    private var registerUserJob: Job? = null
    private var loginUserJob: Job? = null
    private var updateUserJob: Job? = null
    private var deleteUserJob: Job? = null

    private var registerMunicipalityJob: Job? = null
    private var loginMunicipalityJob: Job? = null
    private var updateMunicipalityJob: Job? = null
    private var deleteMunicipalityJob: Job? = null

    fun registerUser(data: UserRegistration) {
        registerUserJob?.cancel()
        registerUserJob = viewModelScope.launch {
            try {
                userRepo.registerUser(data)
            } catch (error: Throwable) {
                throw error
            }
        }
    }

    fun loginUser(data: Login) {
        loginUserJob?.cancel()
        loginUserJob = viewModelScope.launch {
            try {
                userRepo.loginUser(data)
                val userData = userRepo.getUser()
                val updatedState = _uiState.value.userUiState.copy(
                    name = userData.name,
                    joinDate = userData.createdAt,
                    points = userData.points,
                )

                _uiState.update {
                    it.copy(userUiState = updatedState)
                }
            } catch (error: Throwable) {
                throw error
            }
        }
    }

    fun updateUser(data: UserUpdate) {
        updateUserJob?.cancel()
        updateUserJob = viewModelScope.launch {
            try {
                val userData = userRepo.updateUser(data)
                val updatedState = _uiState.value.userUiState.copy(
                    name = userData.name
                )

                _uiState.update {
                    it.copy(userUiState = updatedState)
                }
            } catch (error: Throwable) {
                throw error
            }
        }
    }

    fun deleteUser() {
        deleteUserJob?.cancel()
        deleteUserJob = viewModelScope.launch {
            try {
                userRepo.deleteUser()
                _uiState.update {
                    it.copy(userUiState = UserUiState())
                }
            } catch (error: Throwable) {
                throw error
            }
        }
    }

    fun registerMunicipality(data: MunicipalityRegistration) {
        registerMunicipalityJob?.cancel()
        registerMunicipalityJob = viewModelScope.launch {
            try {
                municipalityRepo.registerMunicipality(data)
            } catch (error: Throwable) {
                throw error
            }
        }
    }

    fun loginMunicipality(data: Login) {
        loginMunicipalityJob?.cancel()
        loginMunicipalityJob = viewModelScope.launch {
            try {
                municipalityRepo.loginMunicipality(data)
                val municipalityData = municipalityRepo.getMunicipality()
                val updatedState = _uiState.value.userUiState.copy(
                    name = municipalityData.name,
                    joinDate = municipalityData.createdAt,
                )

                _uiState.update {
                    it.copy(userUiState = updatedState)
                }
            } catch (error: Throwable) {
                throw error
            }
        }
    }

    fun updateMunicipality(data: MunicipalityUpdate) {
        updateMunicipalityJob?.cancel()
        updateMunicipalityJob = viewModelScope.launch {
            try {
                val municipalityData = municipalityRepo.updateMunicipality(data)
                val updatedState = _uiState.value.userUiState.copy(
                    name = municipalityData.name
                )

                _uiState.update {
                    it.copy(userUiState = updatedState)
                }
            } catch (error: Throwable) {
                throw error
            }
        }
    }

    fun deleteMunicipality() {
        deleteMunicipalityJob?.cancel()
        deleteMunicipalityJob = viewModelScope.launch {
            try {
                municipalityRepo.deleteMunicipality()
                _uiState.update {
                    it.copy(userUiState = UserUiState())
                }
            } catch (error: Throwable) {
                throw error
            }
        }
    }

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

                litterSiteRepo.createLitterSite(litterSiteCreation)
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
                val litterSites = litterSiteRepo.getNearbyLitterSites(userCoords)
                nearbyLitterSites.value = litterSites
            } catch (e: Exception) {
                Log.e("LitterSiteViewModel", "Error fetching nearby litter sites", e)
            }
        }
    }
}

