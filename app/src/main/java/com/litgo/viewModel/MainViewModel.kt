package com.litgo.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litgotesting.data.models.backendDateFormat
import com.example.litgotesting.data.models.frontendDateFormat
import com.example.litgotesting.viewModel.DisposalSiteUiState
import com.example.litgotesting.viewModel.LitgoUiState
import com.example.litgotesting.viewModel.LitterSiteUiState
import com.example.litgotesting.viewModel.RewardUiState
import com.example.litgotesting.viewModel.UserUiState
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
import com.litgo.data.models.RewardCreation
import com.litgo.data.models.RewardUpdate
import com.litgo.data.models.UserRegistration
import com.litgo.data.models.UserUpdate
import com.litgo.data.repositories.DisposalSiteRepository
import com.litgo.data.repositories.LitterSiteRepository
import com.litgo.data.repositories.MunicipalityRepository
import com.litgo.data.repositories.RegionRepository
import com.litgo.data.repositories.RewardRepository
import com.litgo.data.repositories.UserRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Thread.State

class LitgoViewModel : ViewModel() {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://backend-service-v0b8.onrender.com/")
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

    private val throwExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throw throwable
    }

    private var registerUserJob: Job? = null
    private var loginUserJob: Job? = null
    private var updateUserJob: Job? = null
    private var deleteUserJob: Job? = null

    private var registerMunicipalityJob: Job? = null
    private var loginMunicipalityJob: Job? = null
    private var updateMunicipalityJob: Job? = null
    private var deleteMunicipalityJob: Job? = null

    private var getNearbyLitterSitesJob: Job? = null
    private var getLitterSitesCreatedByUserJob: Job? = null
    private var getLitterSitesCleanedByUserJob: Job? = null
    private var getLitterSiteJob: Job? = null
    private var createLitterSiteJob: Job? = null
    private var cleanLitterSiteJob: Job? = null
    private var deleteLitterSiteJob: Job? = null

    private var getRegionsCreatedByMunicipalityJob: Job? = null
    private var getRegionJob: Job? = null
    private var createRegionJob: Job? = null
    private var updateRegionJob: Job? = null
    private var deleteRegionJob: Job? = null

    private var getEligibleRewardsJob: Job? = null
    private var getRewardJob: Job? = null
    private var createRewardJob: Job? = null
    private var redeemRewardJob: Job? = null
    private var updateRewardJob: Job? = null
    private var deleteRewardJob: Job? = null

    private var getNearbyDisposalSitesJob: Job? = null
    private var getDisposalSitesJob: Job? = null
    private var getDisposalSiteJob: Job? = null
    private var createDisposalSiteJob: Job? = null
    private var deleteDisposalSiteJob: Job? = null

    fun observeState(): StateFlow<LitgoUiState> {
        return _uiState
    }

    fun registerUser(data: UserRegistration) {
        registerUserJob?.cancel()
        registerUserJob = viewModelScope.launch(throwExceptionHandler) {
            userRepo.registerUser(data)
        }
    }

    fun loginUser(data: Login) {
        loginUserJob?.cancel()
        loginUserJob = viewModelScope.launch(throwExceptionHandler) {
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
        }
    }

    fun updateUser(data: UserUpdate) {
        updateUserJob?.cancel()
        updateUserJob = viewModelScope.launch(throwExceptionHandler) {
            val userData = userRepo.updateUser(data)
            val updatedState = _uiState.value.userUiState.copy(
                name = userData.name
            )

            _uiState.update {
                it.copy(userUiState = updatedState)
            }
        }
    }

    fun updateUserPosition(userCoords: Coordinates) {
        val updatedState = _uiState.value.userUiState.copy(
            latitude = userCoords.latitude,
            longitude = userCoords.longitude
        )

        _uiState.update {
            it.copy(userUiState = updatedState)
        }
    }

    fun deleteUser() {
        deleteUserJob?.cancel()
        deleteUserJob = viewModelScope.launch(throwExceptionHandler) {
            userRepo.deleteUser()
            _uiState.update {
                it.copy(userUiState = UserUiState())
            }
        }
    }

    fun registerMunicipality(data: MunicipalityRegistration) {
        registerMunicipalityJob?.cancel()
        registerMunicipalityJob = viewModelScope.launch(throwExceptionHandler) {
            municipalityRepo.registerMunicipality(data)
        }
    }

    fun loginMunicipality(data: Login) {
        loginMunicipalityJob?.cancel()
        loginMunicipalityJob = viewModelScope.launch(throwExceptionHandler) {
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
        }
    }

    fun updateMunicipality(data: MunicipalityUpdate) {
        updateMunicipalityJob?.cancel()
        updateMunicipalityJob = viewModelScope.launch(throwExceptionHandler) {
            val municipalityData = municipalityRepo.updateMunicipality(data)
            val updatedState = _uiState.value.userUiState.copy(
                name = municipalityData.name
            )

            _uiState.update {
                it.copy(userUiState = updatedState)
            }
        }
    }

    fun deleteMunicipality() {
        deleteMunicipalityJob?.cancel()
        deleteMunicipalityJob = viewModelScope.launch(throwExceptionHandler) {
            municipalityRepo.deleteMunicipality()
            _uiState.update {
                it.copy(userUiState = UserUiState())
            }
        }
    }

    fun getNearbyLitterSites(userCoords: Coordinates) {
        getNearbyLitterSitesJob?.cancel()
        getNearbyLitterSitesJob = viewModelScope.launch(throwExceptionHandler) {
            val litterSites = litterSiteRepo.getNearbyLitterSites(userCoords)
            val updatedState = _uiState.value.mapUiState.copy(
                nearbyLitterSites = litterSites.map { litterSite ->
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
                        longitude = litterSite.longitude,
                        createdAt = frontendDateFormat.format(
                            backendDateFormat.parse(litterSite.createdAt)
                        ),
                        updatedAt = frontendDateFormat.format(
                            backendDateFormat.parse(litterSite.updatedAt)
                        ),
                    )
                }
            )

            _uiState.update {
                it.copy(mapUiState = updatedState)
            }
        }
    }

    fun getLitterSitesCreatedByUser() {
        getLitterSitesCreatedByUserJob?.cancel()
        getLitterSitesCreatedByUserJob = viewModelScope.launch(throwExceptionHandler) {
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
                        longitude = litterSite.longitude,
                        createdAt = frontendDateFormat.format(
                            backendDateFormat.parse(litterSite.createdAt)
                        ),
                        updatedAt = frontendDateFormat.format(
                            backendDateFormat.parse(litterSite.updatedAt)
                        ),
                    )
                }
            )

            _uiState.update {
                it.copy(userUiState = updatedState)
            }
        }
    }

    fun getLitterSitesCleanedByUser() {
        getLitterSitesCleanedByUserJob?.cancel()
        getLitterSitesCleanedByUserJob = viewModelScope.launch(throwExceptionHandler) {
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
                        longitude = litterSite.longitude,
                        createdAt = frontendDateFormat.format(
                            backendDateFormat.parse(litterSite.createdAt)
                        ),
                        updatedAt = frontendDateFormat.format(
                            backendDateFormat.parse(litterSite.updatedAt)
                        ),
                    )
                }
            )

            _uiState.update {
                it.copy(userUiState = updatedState)
            }
        }
    }

    fun getLitterSite(id: String, userCoords: Coordinates) {
        getLitterSiteJob?.cancel()
        getLitterSiteJob = viewModelScope.launch(throwExceptionHandler) {
            val litterSite = litterSiteRepo.getLitterSite(id, userCoords)
            val updatedState = _uiState.value.mapUiState.copy(
                currentlySelectedLitterSite = LitterSiteUiState(
                    id = litterSite.id,
                    reportingUserId = litterSite.reportingUserId,
                    collectingUserId = litterSite.collectingUserId,
                    isCollected = litterSite.isCollected,
                    litterCount = litterSite.litterCount,
                    image = litterSite.image ?: "",
                    harm = litterSite.harm,
                    description = litterSite.description,
                    latitude = litterSite.latitude,
                    longitude = litterSite.longitude,
                    createdAt = frontendDateFormat.format(
                        backendDateFormat.parse(litterSite.createdAt)
                    ),
                    updatedAt = frontendDateFormat.format(
                        backendDateFormat.parse(litterSite.updatedAt)
                    ),
                )
            )

            _uiState.update {
                it.copy(mapUiState = updatedState)
            }
        }
    }

    fun createLitterSite(data: LitterSiteCreation) {
        createLitterSiteJob?.cancel()
        createLitterSiteJob = viewModelScope.launch(throwExceptionHandler) {
            litterSiteRepo.createLitterSite(data)
        }
    }

    fun cleanLitterSite(id: String, userCoords: Coordinates) {
        cleanLitterSiteJob?.cancel()
        cleanLitterSiteJob = viewModelScope.launch(throwExceptionHandler) {
            // TODO: Populate viewmodel and update
        }
    }

    fun deleteLitterSite(id: String) {
        deleteLitterSiteJob?.cancel()
        deleteLitterSiteJob = viewModelScope.launch(throwExceptionHandler) {
            // TODO: Populate viewmodel and update
        }
    }

    fun getEligibleRewards() {
        getEligibleRewardsJob?.cancel()
        getEligibleRewardsJob = viewModelScope.launch(throwExceptionHandler) {
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
        }
    }

    fun getReward(id: String) {
        getRewardJob?.cancel()
        getRewardJob = viewModelScope.launch(throwExceptionHandler) {
            // TODO: Populate viewmodel and update
        }
    }

    fun createReward(data: RewardCreation) {
        createRewardJob?.cancel()
        createRewardJob = viewModelScope.launch(throwExceptionHandler) {
            // TODO: Populate viewmodel and update
        }
    }

    fun redeemReward(id: String) {
        redeemRewardJob?.cancel()
        redeemRewardJob = viewModelScope.launch(throwExceptionHandler) {
            // TODO: Populate viewmodel and update
        }
    }

    fun updateReward(id: String, data: RewardUpdate) {
        updateRewardJob?.cancel()
        updateRewardJob = viewModelScope.launch(throwExceptionHandler) {
            // TODO: Populate viewmodel and update
        }
    }

    fun deleteReward(id: String) {
        deleteRewardJob?.cancel()
        deleteRewardJob = viewModelScope.launch(throwExceptionHandler) {
            // TODO: Populate viewmodel and update
        }
    }

    fun getNearbyDisposalSites(userCoords: Coordinates) {
        getNearbyDisposalSitesJob?.cancel()
        getNearbyDisposalSitesJob = viewModelScope.launch(throwExceptionHandler) {
            val disposalSites = disposalSiteRepo.getNearbyDisposalSites(userCoords)
            val updatedState = _uiState.value.mapUiState.copy(
                nearbyDisposalSites = disposalSites.map { disposalSite ->
                    DisposalSiteUiState(
                        id = disposalSite.id,
                        municipalityId = disposalSite.municipalityId,
                        latitude = disposalSite.latitude,
                        longitude = disposalSite.longitude
                    )
                }
            )

            _uiState.update {
                it.copy(mapUiState = updatedState)
            }
        }
    }

    fun getDisposalSites() {
        getDisposalSitesJob?.cancel()
        getDisposalSitesJob = viewModelScope.launch(throwExceptionHandler) {
            // TODO: Populate viewmodel and update
        }
    }

    fun getDisposalSite(id: String) {
        getDisposalSiteJob?.cancel()
        getDisposalSiteJob = viewModelScope.launch(throwExceptionHandler) {
            // TODO: Populate viewmodel and update
        }
    }

    fun createDisposalSite(coords: Coordinates) {
        createDisposalSiteJob?.cancel()
        createDisposalSiteJob = viewModelScope.launch(throwExceptionHandler) {
            // TODO: Populate viewmodel and update
        }
    }

    fun deleteDisposalSite(id: String) {
        deleteDisposalSiteJob?.cancel()
        deleteDisposalSiteJob = viewModelScope.launch(throwExceptionHandler) {
            // TODO: Populate viewmodel and update
        }
    }

    fun takePicture(uri: Uri) {
        val currState = _uiState.value.cameraUiState
        val updatedState = currState.copy(
            imagesCaptured = currState.imagesCaptured.toMutableList().apply {
                add(uri)
            }
        )

        _uiState.update {
            it.copy(cameraUiState = updatedState)
        }
    }

    fun clearPictures() {
        val updatedState = _uiState.value.cameraUiState.copy(
            imagesCaptured = emptyList()
        )

        _uiState.update {
            it.copy(cameraUiState = updatedState)
        }
    }
}
