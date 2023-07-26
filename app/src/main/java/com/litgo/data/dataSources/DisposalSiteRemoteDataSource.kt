package com.litgo.data.dataSources

import com.google.gson.annotations.SerializedName
import com.litgo.data.models.Coordinates
import com.litgo.data.models.DisposalSite
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

data class DisposalSiteApiModel(
    @SerializedName("id")             val id: String,
    @SerializedName("municipalityId") val municipalityId: String,
    @SerializedName("latitude")       val latitude: Double,
    @SerializedName("longitude")      val longitude: Double,
    @SerializedName("createdAt")      val createdAt: String,
    @SerializedName("updatedAt")      val updatedAt: String,
)

// Makes synchronous requests related to disposal sites to the database
interface DisposalSiteApi {
    fun getNearbyDisposalSites(userCoords: Coordinates): List<DisposalSite>
    fun getDisposalSites(): List<DisposalSite>
    fun getDisposalSite(id: String): DisposalSite
    fun createDisposalSite(coords: Coordinates): DisposalSite
    fun deleteDisposalSite(id: String)
}

class DisposalSiteRemoteDataSource(
    private val disposalSiteApi: DisposalSiteApi,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getNearbyDisposalSites(userCoords: Coordinates): List<DisposalSite> =
        withContext(ioDispatcher) {
            disposalSiteApi.getNearbyDisposalSites(userCoords)
        }

    suspend fun getDisposalSites(): List<DisposalSite> =
        withContext(ioDispatcher) {
            disposalSiteApi.getDisposalSites()
        }

    suspend fun getDisposalSite(id: String): DisposalSite =
        withContext(ioDispatcher) {
            disposalSiteApi.getDisposalSite(id)
        }

    suspend fun createDisposalSite(coords: Coordinates): DisposalSite =
        withContext(ioDispatcher) {
            disposalSiteApi.createDisposalSite(coords)
        }

    suspend fun deleteDisposalSite(id: String) =
        withContext(ioDispatcher) {
            disposalSiteApi.deleteDisposalSite(id)
        }
}
