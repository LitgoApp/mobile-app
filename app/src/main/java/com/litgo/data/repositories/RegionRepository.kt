package com.litgo.data.repositories

import com.litgo.data.dataSources.RegionRemoteDataSource
import com.litgo.data.models.Coordinates
import com.litgo.data.models.Region

class RegionRepository(private val regionRemoteDataSource: RegionRemoteDataSource) {
    suspend fun getRegionsCreatedByMunicipality(): List<Region> =
        regionRemoteDataSource.getRegionsCreatedByMunicipality()

    suspend fun getRegion(id: String): Region =
        regionRemoteDataSource.getRegion(id)

    suspend fun createRegion(coords: List<Coordinates>) =
        regionRemoteDataSource.createRegion(coords)

    suspend fun updateRegion(id: String, coords: List<Coordinates>) =
        regionRemoteDataSource.updateRegion(id, coords)

    suspend fun deleteRegion(id: String) =
        regionRemoteDataSource.deleteRegion(id)
}
