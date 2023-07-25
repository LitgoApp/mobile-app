package com.litgo.data.dataSources.retrofit

import com.litgo.data.dataSources.MunicipalityApi
import com.litgo.data.dataSources.MunicipalityApiModel
import com.litgo.data.models.Login
import com.litgo.data.models.Municipality
import com.litgo.data.models.MunicipalityRegistration
import com.litgo.data.models.MunicipalityUpdate
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

interface MunicipalityRetrofitApiService {
    @POST("api/municipality/register")
    fun registerMunicipality(@Body data: MunicipalityRegistration): Call<Unit>

    @POST("api/municipality/login")
    fun loginMunicipality(@Body data: Login): Call<HashMap<String, String>>

    @GET("api/municipality")
    fun getMunicipality(@Header("Authorization") token: String): Call<MunicipalityApiModel>

    @PUT("api/municipality")
    fun updateMunicipality(
        @Header("Authorization") token: String,
        @Body data: MunicipalityUpdate
    ): Call<MunicipalityApiModel>

    @DELETE("api/municipality")
    fun deleteMunicipality(@Header("Authorization") token: String): Call<Unit>
}

class MunicipalityRetrofitApi(private val retrofit: Retrofit) : MunicipalityApi {
    private val service = retrofit.create(MunicipalityRetrofitApiService::class.java)

    override fun registerMunicipality(data: MunicipalityRegistration) {
        try {
            val response = service.registerMunicipality(data).execute()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun loginMunicipality(data: Login) {
        try {
            val response = service.loginMunicipality(data).execute()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            authToken = "Bearer " + body["token"]
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun getMunicipality(): Municipality {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.getMunicipality(token).execute()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return Municipality(
                body.email,
                body.name,
                body.phoneNumber,
<<<<<<< HEAD
                body.createdAt,
=======
                body.registeredAt,
                body.lastLoginAt,
>>>>>>> main
            )
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun updateMunicipality(data: MunicipalityUpdate): Municipality {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.updateMunicipality(token, data).execute()

            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return Municipality(
<<<<<<< HEAD
                body.id,
                body.email,
                body.name,
                body.phoneNumber,
                body.createdAt,
=======
                body.email,
                body.name,
                body.phoneNumber,
                body.registeredAt,
                body.lastLoginAt,
>>>>>>> main
            )
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun deleteMunicipality() {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.deleteMunicipality(token).execute()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            authToken = ""
        } catch (error: Throwable) {
            throw error
        }
    }
}
