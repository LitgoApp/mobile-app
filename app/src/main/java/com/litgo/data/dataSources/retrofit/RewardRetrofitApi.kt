package com.litgo.data.dataSources.retrofit

import com.litgo.data.dataSources.RewardApi
import com.litgo.data.dataSources.RewardApiModel
import com.litgo.data.models.Reward
import com.litgo.data.models.RewardCreation
import com.litgo.data.models.RewardUpdate
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

interface RewardRetrofitApiService {
    @GET("api/reward")
    fun getRewards(
        @Header("auth-token") token: String
    ): Call<List<RewardApiModel>>

    @GET("api/reward/{id}")
    fun getReward(
        @Header("auth-token") token: String,
        @Path("id") id: String
    ): Call<RewardApiModel>

    @POST("api/reward")
    fun createReward(
        @Header("auth-token") token: String,
        @Body data: RewardCreation
    ): Call<RewardApiModel>

    @POST("api/reward/{id}")
    fun redeemReward(
        @Header("auth-token") token: String,
        @Path("id") id: String
    ): Call<RewardApiModel>

    @PUT("api/reward/{id}")
    fun updateReward(
        @Header("auth-token") token: String,
        @Path("id") id: String,
        @Body data: RewardUpdate
    ): Call<RewardApiModel>

    @DELETE("api/reward/{id}")
    fun deleteReward(
        @Header("auth-token") token: String,
        @Path("id") id: String
    ): Call<RewardApiModel>
}

class RewardRetrofitApi(private val retrofit: Retrofit) : RewardApi {
    private val service = retrofit.create(RewardRetrofitApiService::class.java)

    override fun getRewards(): List<Reward> {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.getRewards(token).execute()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return body.map { reward ->
                Reward(
                    reward.id,
                    reward.name,
                    reward.cost,
                    reward.description,
                    reward.createdAt,
                    reward.updatedAt
                )
            }
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun getReward(id: String): Reward {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.getReward(token, id).execute()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw HttpException(response)
            }

            return Reward(
                body.id,
                body.name,
                body.cost,
                body.description,
                body.createdAt,
                body.updatedAt
            )
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun createReward(data: RewardCreation) {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.createReward(token, data).execute()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun redeemReward(id: String) {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.redeemReward(token, id).execute()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun updateReward(id: String, data: RewardUpdate) {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.updateReward(token, id, data).execute()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        } catch (error: Throwable) {
            throw error
        }
    }

    override fun deleteReward(id: String) {
        try {
            val token = authToken ?: throw Exception("No auth token")
            val response = service.deleteReward(token, id).execute()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        } catch (error: Throwable) {
            throw error
        }
    }
}
