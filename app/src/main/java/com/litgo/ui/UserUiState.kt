package com.litgo.ui

import com.litgo.ui.reports.ReportDetails

data class UserUiState(
    val loggedIn: Boolean? = false,
    val username: String? = null,
    val location: String? = null,
    val joinedDate: String? = null,
    val points: Int? = 0,
    val cleanedUp: Int? = 0,
    val reports: List<ReportDetails> = listOf(),
    val cleanups: List<ReportDetails> = listOf()
)

data class UserLoginState(
    val emailError: Boolean? = null,
    val passwordError: Boolean? = null,
    val loggedIn: Boolean? = false
)