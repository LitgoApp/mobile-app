package com.litgo.data.repositories

import com.litgo.data.dataSources.LitterSiteRemoteDataSource
import com.litgo.data.models.Coordinates
import com.litgo.data.models.LitterSite
import com.litgo.data.models.LitterSiteCreation

class LitterSiteRepository(private val litterSiteRemoteDataSource: LitterSiteRemoteDataSource) {
    suspend fun getNearbyLitterSites(userCoords: Coordinates): List<LitterSite> =
        litterSiteRemoteDataSource.getNearbyLitterSites(userCoords)

    suspend fun getLitterSitesCreatedByUser(): List<LitterSite> =
        litterSiteRemoteDataSource.getLitterSitesCreatedByUser()

    suspend fun getLitterSite(id: String, userCoords: Coordinates): LitterSite =
        litterSiteRemoteDataSource.getLitterSite(id, userCoords)

    suspend fun createLitterSite(data: LitterSiteCreation) =
        litterSiteRemoteDataSource.createLitterSite(data)

    suspend fun cleanLitterSite(id: String) =
        litterSiteRemoteDataSource.cleanLitterSite(id)

    suspend fun deleteLitterSite(id: String) =
        litterSiteRemoteDataSource.deleteLitterSite(id)
}
