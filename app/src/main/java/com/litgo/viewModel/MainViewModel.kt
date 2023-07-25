package com.litgo.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litgotesting.data.models.backendDateFormat
import com.example.litgotesting.data.models.frontendDateFormat
import com.example.litgotesting.viewModel.LitgoUiState
import com.example.litgotesting.viewModel.LitterSiteUiState
import com.example.litgotesting.viewModel.RewardUiState
import com.example.litgotesting.viewModel.UserUiState
import com.example.litgotesting.viewModel.MapUiState
import com.litgo.data.dataSources.DisposalSiteRemoteDataSource
import com.litgo.data.dataSources.LitterSiteRemoteDataSource
import com.litgo.data.dataSources.MunicipalityRemoteDataSource
import com.litgo.data.dataSources.RegionRemoteDataSource
import com.litgo.data.dataSources.RewardRemoteDataSource
import com.litgo.data.dataSources.UserRemoteDataSource
import com.litgo.data.dataSources.retrofit.DisposalSiteRetrofitApi
import com.litgo.data.dataSources.retrofit.LitterSiteRetrofitApi
import com.litgo.data.dataSources.retrofit.MunicipalityRetrofitApi
import com.litgo.data.dataSources.retrofit.RegionRetrofitApi
import com.litgo.data.dataSources.retrofit.RewardRetrofitApi
import com.litgo.data.dataSources.retrofit.UserRetrofitApi
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

class LitterSiteViewModel : ViewModel() {
    //decide on littercount, latitude, longitude

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://172.23.176.1:3001/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val userRepo = UserRepository(
        UserRemoteDataSource(UserRetrofitApi(retrofit), Dispatchers.IO)
    )

    private val municipalityRepo: MunicipalityRepository = MunicipalityRepository(
        MunicipalityRemoteDataSource(MunicipalityRetrofitApi(retrofit), Dispatchers.IO)
    )

    private val litterSiteRepo: LitterSiteRepository = LitterSiteRepository(
        LitterSiteRemoteDataSource(LitterSiteRetrofitApi(retrofit), Dispatchers.IO)
    )

    private val regionRepo: RegionRepository = RegionRepository(
        RegionRemoteDataSource(RegionRetrofitApi(retrofit), Dispatchers.IO)
    )

    private val rewardRepo: RewardRepository = RewardRepository(
        RewardRemoteDataSource(RewardRetrofitApi(retrofit), Dispatchers.IO)
    )

    private val disposalSiteRepo: DisposalSiteRepository = DisposalSiteRepository(
        DisposalSiteRemoteDataSource(DisposalSiteRetrofitApi(retrofit), Dispatchers.IO)
    )

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

    private var getLitterSitesCreatedByUserJob: Job? = null
    private var getLitterSitesCleanedByUserJob: Job? = null

    private var getEligibleRewardsJob: Job? = null

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
                    joinDate = frontendDateFormat.format(
                        backendDateFormat.parse(userData.registeredAt)
                    ),
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
                    joinDate = frontendDateFormat.format(
                        backendDateFormat.parse(municipalityData.registeredAt)
                    ),
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

    fun setSelectedLitterSite(id: String, location: Coordinates) {
        try {
            val selectedLitterSite = fetchLitterSiteById(id, location)
            val updatedState = _uiState.value.mapUiState.copy(
                litterSiteIdSelected = selectedLitterSite
            )

            _uiState.update {
                it.copy(mapUiState = updatedState)
            }

        } catch (error: Throwable) {
            throw error
        }
    }

    fun getLitterSitesCreatedByUser() {
        getLitterSitesCreatedByUserJob?.cancel()
        getLitterSitesCreatedByUserJob = viewModelScope.launch {
            try {
                val litterSites = litterSiteRepo.getLitterSitesCreatedByUser()
                val updatedState = _uiState.value.userUiState.copy(
                    reports = litterSites.map { litterSite ->
                        LitterSiteUiState(
                            id = litterSite.id,
                            reportingUserId = litterSite.reportingUserId,
                            collectingUserId = litterSite.collectingUserId,
                            isCollected = litterSite.isCollected,
                            litterCount = litterSite.litterCount,
                            image = "",
                            harm = litterSite.harm,
                            description = litterSite.description,
                            latitude = litterSite.latitude,
                            longitude = litterSite.longitude
                        )
                    }
                )

                _uiState.update {
                    it.copy(userUiState = updatedState)
                }
            } catch (error: Throwable) {
                throw error
            }
        }
    }

    fun getLitterSitesCleanedByUser() {
        getLitterSitesCleanedByUserJob?.cancel()
        getLitterSitesCleanedByUserJob = viewModelScope.launch {
            try {
                val litterSites = litterSiteRepo.getLitterSitesCleanedByUser()
                val updatedState = _uiState.value.userUiState.copy(
                    cleanups = litterSites.map { litterSite ->
                        LitterSiteUiState(
                            id = litterSite.id,
                            reportingUserId = litterSite.reportingUserId,
                            collectingUserId = litterSite.collectingUserId,
                            isCollected = litterSite.isCollected,
                            litterCount = litterSite.litterCount,
                            image = "",
                            harm = litterSite.harm,
                            description = litterSite.description,
                            latitude = litterSite.latitude,
                            longitude = litterSite.longitude
                        )
                    }
                )

                _uiState.update {
                    it.copy(userUiState = updatedState)
                }
            } catch (error: Throwable) {
                throw error
            }
        }
    }

    fun getEligibleRewards() {
        getEligibleRewardsJob?.cancel()
        getEligibleRewardsJob = viewModelScope.launch {
            try {
                val rewards = rewardRepo.getRewards()
                val updatedState = _uiState.value.userUiState.copy(
                    eligibleRewards = rewards.filter { reward ->
                        reward.cost <= _uiState.value.userUiState.points
                    }.map { reward ->
                        RewardUiState(
                            id = reward.id,
                            name = reward.name,
                            cost = reward.cost,
                            description = "",
                        )
                    }
                )

                _uiState.update {
                    it.copy(userUiState = updatedState)
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

    fun fetchLitterSiteById(id: String, userCoords: Coordinates): LiveData<LitterSite> {
        val litterSiteLiveData = MutableLiveData<LitterSite>()

        viewModelScope.launch {
            try {
                val litterSite = litterSiteRepo.getLitterSiteById(id, userCoords)
                litterSiteLiveData.postValue(litterSite)
            } catch (e: Exception) {
                Log.e("LitterSiteViewModel", "Error fetching litter site by id", e)
            }
        }
        return litterSiteLiveData
    }


    val nearbyDisposalSites = MutableLiveData<List<DisposalSite>>()
    fun fetchNearbyDisposalSites(userCoords: Coordinates) {
        viewModelScope.launch {
            try {
                val disposalSites = repository.getNearbyDisposalSites(userCoords)
                nearbyDisposalSites.value = disposalSites
            } catch (e: Exception) {
                Log.e("LitterSiteViewModel", "Error fetching nearby disposal sites", e)
            }
        }
    }

}

