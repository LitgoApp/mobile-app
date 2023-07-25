package com.litgo.data.dataSources.retrofit

import com.litgo.data.dataSources.DisposalSiteApi
import com.litgo.data.dataSources.DisposalSiteApiModel
import com.litgo.data.models.Coordinates
import com.litgo.data.models.DisposalSite
import com.litgo.data.models.authToken
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface DisposalSiteRetrofitApiService {
    @GET("api/disposal-site")
    fun getDisposalSites(
        @Header("Authorization") token: String
    ): Call<List<DisposalSiteApiModel>>

    @GET("api/disposal-site/{id}")
    fun getDisposalSite(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DisposalSiteApiModel>

    @POST("api/disposal-site")
    fun createDisposalSite(
        @Header("Authorization") token: String,
        @Body coords: Coordinates
    ): Call<DisposalSiteApiModel>

    @DELETE("api/disposal-site/{id}")
    fun deleteDisposalSite(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DisposalSiteApiModel>
}

class DisposalSiteRetrofitApi(private val retrofit: Retrofit) : DisposalSiteApi {
    private val service = retrofit.create(DisposalSiteRetrofitApiService::class.java)
    override fun getNearbyDisposalSites(userCoords: Coordinates): List<DisposalSite> {
        TODO("Not yet implemented")
    }

    override fun getDisposalSites(): List<DisposalSite> {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.getDisposalSites(token).execute()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return body.map { disposalSite ->
                DisposalSite(
                    disposalSite.id,
                    disposalSite.municipalityId,
                    disposalSite.latitude,
                    disposalSite.longitude,
                    disposalSite.createdAt,
                    disposalSite.updatedAt
                )
            }
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun getDisposalSite(id: String): DisposalSite {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.getDisposalSite(token, id).execute()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return DisposalSite(
                body.id,
                body.municipalityId,
                body.latitude,
                body.longitude,
                body.createdAt,
                body.updatedAt
            )
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun createDisposalSite(coords: Coordinates): DisposalSite {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.createDisposalSite(token, coords).execute()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return DisposalSite(
                body.id,
                body.municipalityId,
                body.latitude,
                body.longitude,
                body.createdAt,
                body.updatedAt
            )
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun deleteDisposalSite(id: String) {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.deleteDisposalSite(token, id).execute()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        } catch (error: Throwable) {
            throw error
        }
    }
}
