package com.litgo.data.models

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("email")    val email: String,
    @SerializedName("password") val password: String,
)

var authToken: String? = null
