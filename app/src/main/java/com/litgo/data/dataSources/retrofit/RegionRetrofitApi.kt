package com.litgo.data.dataSources.retrofit

import com.litgo.data.dataSources.RegionApi
import com.litgo.data.dataSources.RegionApiModel
import com.litgo.data.models.Coordinates
import com.litgo.data.models.Region
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
import retrofit2.http.Path

interface RegionRetrofitApiService {
    @GET("api/region")
    fun getRegionsCreatedByMunicipality(
        @Header("auth-token") token: String
    ): Call<List<RegionApiModel>>

    @GET("api/region/{id}")
    fun getRegion(
        @Header("auth-token") token: String,
        @Path("id") id: String
    ): Call<RegionApiModel>

    @POST("api/region")
    fun createRegion(
        @Header("auth-token") token: String,
        @Body coords: List<Coordinates>
    ): Call<RegionApiModel>

    @PUT("api/region/{id}")
    fun updateRegion(
        @Header("auth-token") token: String,
        @Path("id") id: String,
        @Body coords: List<Coordinates>
    ): Call<RegionApiModel>

    @DELETE("api/region/{id}")
    fun deleteRegion(
        @Header("auth-token") token: String,
        @Path("id") id: String
    ): Call<RegionApiModel>
}

class RegionRetrofitApi(private val retrofit: Retrofit) : RegionApi {
    private val service = retrofit.create(RegionRetrofitApiService::class.java)

    override fun getRegionsCreatedByMunicipality(): List<Region> {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.getRegionsCreatedByMunicipality(token).execute()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return body.map { region ->
                Region(
                    region.id,
                    region.municipalityId,
//                    region.createdAt,
//                    region.updatedAt
                )
            }
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun getRegion(id: String): Region {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.getRegion(token, id).execute()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return Region(
                body.id,
                body.municipalityId,
//                body.createdAt,
//                body.updatedAt
            )
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun createRegion(coords: List<Coordinates>) {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.createRegion(token, coords).execute()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun updateRegion(id: String, coords: List<Coordinates>) {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.updateRegion(token, id, coords).execute()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun deleteRegion(id: String) {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.deleteRegion(token, id).execute()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        } catch (error: Throwable) {
            throw error
        }
    }
}
