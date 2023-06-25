package com.litgo.data.repositories

import com.litgo.data.dataSources.MunicipalityRemoteDataSource
import com.litgo.data.models.Login
import com.litgo.data.models.Municipality
import com.litgo.data.models.MunicipalityRegistration
import com.litgo.data.models.MunicipalityUpdate

class MunicipalityRepository(
    private val municipalityRemoteDataSource: MunicipalityRemoteDataSource
) {
    suspend fun registerMunicipality(data: MunicipalityRegistration) =
        municipalityRemoteDataSource.registerMunicipality(data)

    suspend fun loginMunicipality(data: Login) =
        municipalityRemoteDataSource.loginMunicipality(data)

    suspend fun getMunicipality(): Municipality =
        municipalityRemoteDataSource.getMunicipality()

    suspend fun updateMunicipality(data: MunicipalityUpdate) =
        municipalityRemoteDataSource.updateMunicipality(data)

    suspend fun deleteMunicipality() =
        municipalityRemoteDataSource.deleteMunicipality()
}
