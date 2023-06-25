package com.litgo.data.dataSources

import com.google.gson.annotations.SerializedName
import com.litgo.data.models.Coordinates
import com.litgo.data.models.Region
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

data class RegionApiModel(
    @SerializedName("regionId")       val id: String,
    @SerializedName("municipalityId") val municipalityId: String,
    @SerializedName("createdAt")      val createdAt: String,
    @SerializedName("updatedAt")      val updatedAt: String,
)

// Makes region-related synchronous requests to the database
interface RegionApi {
    fun getRegionsCreatedByMunicipality(): List<Region>
    fun getRegion(id: String): Region
    fun createRegion(coords: List<Coordinates>)
    fun updateRegion(id: String, coords: List<Coordinates>)
    fun deleteRegion(id: String)
}

class RegionRemoteDataSource(
    private val regionApi: RegionApi,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getRegionsCreatedByMunicipality(): List<Region> =
        withContext(ioDispatcher) {
            regionApi.getRegionsCreatedByMunicipality()
        }

    suspend fun getRegion(id: String): Region =
        withContext(ioDispatcher) {
            regionApi.getRegion(id)
        }

    suspend fun createRegion(coords: List<Coordinates>) =
        withContext(ioDispatcher) {
            regionApi.createRegion(coords)
        }

    suspend fun updateRegion(id: String, coords: List<Coordinates>) =
        withContext(ioDispatcher) {
            regionApi.updateRegion(id, coords)
        }

    suspend fun deleteRegion(id: String) =
        withContext(ioDispatcher) {
            regionApi.deleteRegion(id)
        }
}
