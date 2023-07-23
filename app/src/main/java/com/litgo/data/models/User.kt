package com.litgo.data.models

import com.google.gson.annotations.SerializedName

data class User(
    val id: String,
    val email: String,
    val name: String,
    val points: Int,
    val address: String,
    val createdAt: String,
)

data class UserRegistration(
    @SerializedName("email")    val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("name")     val name: String,
    @SerializedName("address")  val address: String,
)

data class UserUpdate(
    @SerializedName("email")    val email: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("name")     val name: String?,
    @SerializedName("address")  val address: String?,
)
