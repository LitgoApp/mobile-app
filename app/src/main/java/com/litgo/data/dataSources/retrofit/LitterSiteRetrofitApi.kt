package com.litgo.data.dataSources.retrofit

import com.litgo.data.dataSources.LitterSiteApi
import com.litgo.data.dataSources.LitterSiteApiModel
import com.litgo.data.models.Coordinates
import com.litgo.data.models.DisposalSite
import com.litgo.data.models.LitterSite
import com.litgo.data.models.LitterSiteCreation
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
import retrofit2.http.Query

interface LitterSiteRetrofitApiService {
    @GET("api/litter-site")
    fun getNearbyLitterSites(
        @Header("Authorization") token: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Call<List<LitterSiteApiModel>>

    @GET("api/litter-site/created")
    fun getLitterSitesCreatedByUser(
        @Header("Authorization") token: String
    ): Call<List<LitterSiteApiModel>>

    @GET("api/litter-site/cleaned")
    fun getLitterSitesCleanedByUser(
        @Header("Authorization") token: String
    ): Call<List<LitterSiteApiModel>>

    @GET("api/litter-site/{id}")
    fun getLitterSite(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Call<LitterSiteApiModel>

    @POST("api/litter-site")
    fun createLitterSite(
        @Header("Authorization") token: String,
        @Body data: LitterSiteCreation
    ): Call<LitterSiteApiModel>

    @POST("api/litter-site/{id}")
    fun cleanLitterSite(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Call<LitterSiteApiModel>

    @DELETE("api/litter-site/{id}")
    fun deleteLitterSite(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<LitterSiteApiModel>
}

class LitterSiteRetrofitApi(private val retrofit: Retrofit) : LitterSiteApi {
    private val service = retrofit.create(LitterSiteRetrofitApiService::class.java)

    override fun getNearbyLitterSites(userCoords: Coordinates): List<LitterSite> {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.getNearbyLitterSites(
                token,
                userCoords.latitude,
                userCoords.latitude
            ).execute()

            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return body.map { litterSite ->
                LitterSite(
                    litterSite.id,
                    litterSite.reportingUserId,
                    litterSite.collectingUserId,
                    litterSite.isCollected,
                    litterSite.litterCount,
                    null,
                    litterSite.harm,
                    litterSite.description,
                    litterSite.latitude,
                    litterSite.longitude,
                    null,
                    litterSite.createdAt,
                    litterSite.updatedAt
                )
            }
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun getLitterSitesCreatedByUser(): List<LitterSite> {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.getLitterSitesCreatedByUser(token).execute()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return body.map { litterSite ->
                LitterSite(
                    litterSite.id,
                    litterSite.reportingUserId,
                    litterSite.collectingUserId,
                    litterSite.isCollected,
                    litterSite.litterCount,
                    null,
                    litterSite.harm,
                    litterSite.description,
                    litterSite.latitude,
                    litterSite.longitude,
                    null,
                    litterSite.createdAt,
                    litterSite.updatedAt
                )
            }
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun getLitterSitesCleanedByUser(): List<LitterSite> {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.getLitterSitesCleanedByUser(token).execute()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return body.map { litterSite ->
                LitterSite(
                    litterSite.id,
                    litterSite.reportingUserId,
                    litterSite.collectingUserId,
                    litterSite.isCollected,
                    litterSite.litterCount,
                    null,
                    litterSite.harm,
                    litterSite.description,
                    litterSite.latitude,
                    litterSite.longitude,
                    null,
                    litterSite.createdAt,
                    litterSite.updatedAt
                )
            }
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun getLitterSite(id: String, userCoords: Coordinates): LitterSite {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.getLitterSite(
                token,
                id,
                userCoords.latitude,
                userCoords.longitude
            ).execute()

            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return LitterSite(
                body.id,
                body.reportingUserId,
                body.collectingUserId,
                body.isCollected,
                body.litterCount,
                body.image,
                body.harm,
                body.description,
                body.latitude,
                body.longitude,
                if (body.closestDisposalSite != null) DisposalSite(
                    body.closestDisposalSite.id,
                    body.closestDisposalSite.municipalityId,
                    body.closestDisposalSite.latitude,
                    body.closestDisposalSite.longitude,
                    body.closestDisposalSite.createdAt,
                    body.closestDisposalSite.updatedAt,
                ) else null,
                body.createdAt,
                body.updatedAt
            )
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun createLitterSite(data: LitterSiteCreation): LitterSite {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.createLitterSite(token, data).execute()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return LitterSite(
                body.id,
                body.reportingUserId,
                body.collectingUserId,
                body.isCollected,
                body.litterCount,
                null,
                body.harm,
                body.description,
                body.latitude,
                body.longitude,
                null,
                body.createdAt,
                body.updatedAt
            )
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun cleanLitterSite(id: String, userCoords: Coordinates): LitterSite {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.cleanLitterSite(
                token,
                id,
                userCoords.latitude,
                userCoords.longitude
            ).execute()

            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return LitterSite(
                body.id,
                body.reportingUserId,
                body.collectingUserId,
                body.isCollected,
                body.litterCount,
                null,
                body.harm,
                body.description,
                body.latitude,
                body.longitude,
                null,
                body.createdAt,
                body.updatedAt
            )
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun deleteLitterSite(id: String) {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.deleteLitterSite(token, id).execute()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        } catch (error: Throwable) {
            throw error
        }
    }
}
