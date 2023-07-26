package com.example.litgotesting.viewModel

import java.util.Date

data class CameraUiState(
    val imagesCaptured: List<String> = listOf(),
)

data class LitterSiteUiState(
    val id: String = "",
    val reportingUserId: String = "",
    val collectingUserId: String? = null,
    val isCollected: Boolean = false,
    val litterCount: Int = 0,
    val image: String = "",
    val harm: String = "",
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val createdAt: String = "",
    val updatedAt: String = "",
)

val LitterSiteUiState.reportPoints: Int get() = litterCount * 3
val LitterSiteUiState.collectPoints: Int get() = litterCount * 10

data class RewardUiState(
    val id: String = "",
    val name: String = "",
    val cost: Int = 0,
    val description: String = "",
)

data class UserUiState(
    val id: String = "",
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val joinDate: String = "",
    val points: Int = 0,
    val reports: List<LitterSiteUiState> = listOf(),
    val cleanups: List<LitterSiteUiState> = listOf(),
    val eligibleRewards: List<RewardUiState> = listOf(),
)

data class LitgoUiState(
    val cameraUiState: CameraUiState = CameraUiState(),
    val userUiState: UserUiState = UserUiState(),
)
