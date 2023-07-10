package com.litgo.data.repositories

import com.litgo.data.dataSources.RewardRemoteDataSource
import com.litgo.data.models.Reward
import com.litgo.data.models.RewardCreation
import com.litgo.data.models.RewardUpdate

class RewardRepository(private val rewardRemoteDataSource: RewardRemoteDataSource) {
    suspend fun getRewards(): List<Reward> =
        rewardRemoteDataSource.getRewards()

    suspend fun getReward(id: String): Reward =
        rewardRemoteDataSource.getReward(id)

    suspend fun createReward(data: RewardCreation) =
        rewardRemoteDataSource.createReward(data)

    suspend fun redeemReward(id: String) =
        rewardRemoteDataSource.redeemReward(id)

    suspend fun updateReward(id: String, data: RewardUpdate) =
        rewardRemoteDataSource.updateReward(id, data)

    suspend fun deleteReward(id: String) =
        rewardRemoteDataSource.deleteReward(id)
}
