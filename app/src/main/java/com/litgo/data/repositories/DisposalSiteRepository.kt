package com.litgo.data.repositories

import com.litgo.data.dataSources.DisposalSiteRemoteDataSource
import com.litgo.data.models.Coordinates
import com.litgo.data.models.DisposalSite

class DisposalSiteRepository(
    private val disposalSiteRemoteDataSource: DisposalSiteRemoteDataSource
) {
    suspend fun getNearbyDisposalSites(userCoords: Coordinates): List<DisposalSite> =
        disposalSiteRemoteDataSource.getNearbyDisposalSites(userCoords)

    suspend fun getDisposalSites(): List<DisposalSite> =
        disposalSiteRemoteDataSource.getDisposalSites()

    suspend fun getDisposalSite(id: String): DisposalSite =
        disposalSiteRemoteDataSource.getDisposalSite(id)

    suspend fun createDisposalSite(coords: Coordinates): DisposalSite =
        disposalSiteRemoteDataSource.createDisposalSite(coords)

    suspend fun deleteDisposalSite(id: String) =
        disposalSiteRemoteDataSource.deleteDisposalSite(id)
}
