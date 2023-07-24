package com.litgo.data.models

import com.google.gson.annotations.SerializedName

data class Reward(
    val id: String,
    val name: String,
    val cost: Int,
    val description: String?,
    val createdAt: String?,
    val updatedAt: String?,
)

data class RewardCreation(
    @SerializedName("name")        val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("cost")        val cost: Int,
)

data class RewardUpdate(
    @SerializedName("name")        val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("cost")        val cost: Int?,
)
