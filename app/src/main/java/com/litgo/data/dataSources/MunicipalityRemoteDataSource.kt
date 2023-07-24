package com.litgo.data.dataSources

import com.google.gson.annotations.SerializedName
import com.litgo.data.models.Login
import com.litgo.data.models.Municipality
import com.litgo.data.models.MunicipalityRegistration
import com.litgo.data.models.MunicipalityUpdate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

data class MunicipalityApiModel(
    @SerializedName("email")        val email: String,
    @SerializedName("name")         val name: String,
    @SerializedName("phoneNumber")  val phoneNumber: String,
    @SerializedName("registeredAt") val registeredAt: String,
    @SerializedName("lastLoginAt")  val lastLoginAt: String,
)

// Makes municipality-related synchronous requests to the database
interface MunicipalityApi {
    fun registerMunicipality(data: MunicipalityRegistration)
    fun loginMunicipality(data: Login)
    fun getMunicipality(): Municipality
    fun updateMunicipality(data: MunicipalityUpdate): Municipality
    fun deleteMunicipality()
}

class MunicipalityRemoteDataSource(
    private val municipalityApi: MunicipalityApi,
    private val ioDispatcher: CoroutineDispatcher
) {
    /**
     * Registers the municipality to the database and returns the municipality id.
     * This executes on an IO-optimized thread pool, the function is main-safe.
     */
    suspend fun registerMunicipality(data: MunicipalityRegistration) =
        withContext(ioDispatcher) {
            municipalityApi.registerMunicipality(data)
        }

    /**
     * Logs the municipality in and returns the token used for the auth-token header.
     * This executes on an IO-optimized thread pool, the function is main-safe.
     */
    suspend fun loginMunicipality(data: Login) =
        withContext(ioDispatcher) {
            municipalityApi.loginMunicipality(data)
        }

    /**
     * Gets the currently logged in municipality's data from the database and returns the result.
     * This executes on an IO-optimized thread pool, the function is main-safe.
     */
    suspend fun getMunicipality(): Municipality =
        withContext(ioDispatcher) {
            municipalityApi.getMunicipality()
        }

    /**
     * Updates the currently logged in municipality's data in the database and returns the
     * municipality's data.
     * This executes on an IO-optimized thread pool, the function is main-safe.
     */
    suspend fun updateMunicipality(data: MunicipalityUpdate): Municipality =
        withContext(ioDispatcher) {
            municipalityApi.updateMunicipality(data)
        }

    /**
     * Deletes the currently logged in municipality from the database and returns the deleted
     * municipality's data.
     * This executes on an IO-optimized thread pool, the function is main-safe.
     */
    suspend fun deleteMunicipality() =
        withContext(ioDispatcher) {
            municipalityApi.deleteMunicipality()
        }
}
