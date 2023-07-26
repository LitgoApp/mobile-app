package com.litgo.data.dataSources

import com.google.gson.annotations.SerializedName
import com.litgo.data.models.Coordinates
import com.litgo.data.models.DisposalSite
import com.litgo.data.models.LitterSite
import com.litgo.data.models.LitterSiteCreation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

data class LitterSiteApiModel(
    @SerializedName("id")               val id: String,
    @SerializedName("reporterUserId")  val reportingUserId: String,
    @SerializedName("collectorUserId") val collectingUserId: String?,
    @SerializedName("isCollected")      val isCollected: Boolean,
    @SerializedName("litterCount")      val litterCount: Int,
    @SerializedName("image")            val image: String?,
    @SerializedName("harm")             val harm: String,
    @SerializedName("description")      val description: String,
    @SerializedName("latitude")         val latitude: Double,
    @SerializedName("longitude")        val longitude: Double,
    @SerializedName("disposalSite")     val closestDisposalSite: DisposalSiteApiModel?,
    @SerializedName("createdAt")        val createdAt: String,
    @SerializedName("updatedAt")        val updatedAt: String,
)

// Makes synchronous requests related to litter sites to the database
interface LitterSiteApi {
    fun getNearbyLitterSites(userCoords: Coordinates): List<LitterSite>
    fun getLitterSitesCreatedByUser(): List<LitterSite>
    fun getLitterSitesCleanedByUser(): List<LitterSite>
    fun getLitterSite(id: String, userCoords: Coordinates): LitterSite
    fun createLitterSite(data: LitterSiteCreation): LitterSite
    fun cleanLitterSite(id: String, userCoords: Coordinates): LitterSite
    fun deleteLitterSite(id: String)
}

class LitterSiteRemoteDataSource(
    private val litterSiteApi: LitterSiteApi,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getNearbyLitterSites(userCoords: Coordinates): List<LitterSite> =
        withContext(ioDispatcher) {
            litterSiteApi.getNearbyLitterSites(userCoords)
        }

    suspend fun getLitterSitesCreatedByUser(): List<LitterSite> =
        withContext(ioDispatcher) {
            litterSiteApi.getLitterSitesCreatedByUser()
        }

    suspend fun getLitterSitesCleanedByUser(): List<LitterSite> =
        withContext(ioDispatcher) {
            litterSiteApi.getLitterSitesCreatedByUser()
        }

    suspend fun getLitterSite(id: String, userCoords: Coordinates): LitterSite =
        withContext(ioDispatcher) {
            litterSiteApi.getLitterSite(id, userCoords)
        }

    suspend fun createLitterSite(data: LitterSiteCreation): LitterSite =
        withContext(ioDispatcher) {
            litterSiteApi.createLitterSite(data)
        }

    suspend fun cleanLitterSite(id: String, userCoords: Coordinates): LitterSite =
        withContext(ioDispatcher) {
            litterSiteApi.cleanLitterSite(id, userCoords)
        }

    suspend fun deleteLitterSite(id: String) =
        withContext(ioDispatcher) {
            litterSiteApi.deleteLitterSite(id)
        }
}
