package com.litgo.data.dataSources

import com.google.gson.annotations.SerializedName
import com.litgo.data.models.Reward
import com.litgo.data.models.RewardCreation
import com.litgo.data.models.RewardUpdate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

data class RewardApiModel(
    @SerializedName("rewardId")    val id: String,
    @SerializedName("name")        val name: String,
    @SerializedName("cost")        val cost: Int,
    @SerializedName("description") val description: String,
    @SerializedName("createdAt")   val createdAt: String,
    @SerializedName("updatedAt")   val updatedAt: String,
)

// Makes reward-related synchronous requests to the database
interface RewardApi {
    fun getRewards(): List<Reward>
    fun getReward(id: String): Reward
    fun createReward(data: RewardCreation)
    fun redeemReward(id: String)
    fun updateReward(id: String, data: RewardUpdate)
    fun deleteReward(id: String)
}

class RewardRemoteDataSource(
    private val rewardApi: RewardApi,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getRewards(): List<Reward> =
        withContext(ioDispatcher) {
            rewardApi.getRewards()
        }

    suspend fun getReward(id: String): Reward =
        withContext(ioDispatcher) {
            rewardApi.getReward(id)
        }

    suspend fun createReward(data: RewardCreation) =
        withContext(ioDispatcher) {
            rewardApi.createReward(data)
        }

    suspend fun redeemReward(id: String) =
        withContext(ioDispatcher) {
            rewardApi.redeemReward(id)
        }

    suspend fun updateReward(id: String, data: RewardUpdate) =
        withContext(ioDispatcher) {
            rewardApi.updateReward(id, data)
        }

    suspend fun deleteReward(id: String) =
        withContext(ioDispatcher) {
            rewardApi.deleteReward(id)
        }
}
