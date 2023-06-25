package com.litgo.data.models

import com.google.gson.annotations.SerializedName

data class Municipality(
    val id: String,
    val email: String,
    val name: String,
    val phoneNumber: String,
    val createdAt: String,
    val updatedAt: String,
)

data class MunicipalityRegistration(
    @SerializedName("email")       val email: String,
    @SerializedName("password")    val password: String,
    @SerializedName("name")        val name: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
)

data class MunicipalityUpdate(
    @SerializedName("email")       val email: String?,
    @SerializedName("password")    val password: String?,
    @SerializedName("name")        val name: String?,
    @SerializedName("phoneNumber") val phoneNumber: String?,
)
