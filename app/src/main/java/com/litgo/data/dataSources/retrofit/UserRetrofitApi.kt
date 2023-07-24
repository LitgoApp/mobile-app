package com.litgo.data.dataSources.retrofit

import com.litgo.data.dataSources.UserApi
import com.litgo.data.dataSources.UserApiModel
import com.litgo.data.models.Login
import com.litgo.data.models.User
import com.litgo.data.models.UserRegistration
import com.litgo.data.models.UserUpdate
import com.litgo.data.models.authToken
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserRetrofitApiService {
    @POST("api/user/register")
    fun registerUser(@Body data: UserRegistration): Call<Unit>

    @POST("api/user/login")
    fun loginUser(@Body data: Login): Call<HashMap<String, String>>

    @GET("api/user")
    fun getUser(@Header("Authorization") token: String): Call<UserApiModel>

    @PUT("api/user")
    fun updateUser(
        @Header("Authorization") token: String,
        @Body data: UserUpdate
    ): Call<UserApiModel>

    @DELETE("api/user")
    fun deleteUser(@Header("Authorization") token: String): Call<Unit>
}

class UserRetrofitApi(private val retrofit: Retrofit) : UserApi {
    private val service = retrofit.create(UserRetrofitApiService::class.java)

    override fun registerUser(data: UserRegistration) {
        try {
            val response = service.registerUser(data).execute()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun loginUser(data: Login) {
        try {
            val response = service.loginUser(data).execute()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            authToken = "Bearer " + body["token"]
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun getUser(): User {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.getUser(token).execute()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return User(
                body.email,
                body.name,
                body.points,
                body.address,
                body.registeredAt,
                body.lastLoginAt,
            )
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun updateUser(data: UserUpdate): User {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.updateUser(token, data).execute()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return User(
                body.email,
                body.name,
                body.points,
                body.address,
                body.registeredAt,
                body.lastLoginAt,
            )
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun deleteUser() {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.deleteUser(token).execute()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            authToken = ""
        } catch (error: Throwable) {
            throw error
        }
    }
}
