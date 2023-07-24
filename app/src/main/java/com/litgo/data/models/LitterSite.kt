package com.litgo.data.models

import com.google.gson.annotations.SerializedName

data class LitterSite(
    val id: String,
    val reportingUserId: String,
    val collectingUserId: String?,
    val isCollected: Boolean,
    val litterCount: Int,
    val image: String?,
    val harm: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val closestDisposalSite: DisposalSite?,
    val createdAt: String,
    val updatedAt: String,
)

data class LitterSiteCreation(
    @SerializedName("latitude")    val latitude: Double,
    @SerializedName("longitude")   val longitude: Double,
    @SerializedName("harm")        val harm: String,
    @SerializedName("description") val description: String,
    @SerializedName("litterCount") val litterCount: Int,
    @SerializedName("image")       val image: String,
)
