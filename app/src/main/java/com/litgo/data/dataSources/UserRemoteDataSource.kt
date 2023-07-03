package com.litgo.data.dataSources

import com.google.gson.annotations.SerializedName
import com.litgo.data.models.Login
import com.litgo.data.models.User
import com.litgo.data.models.UserRegistration
import com.litgo.data.models.UserUpdate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

data class UserApiModel(
    @SerializedName("userId")    val id: String,
    @SerializedName("email")     val email: String,
    @SerializedName("name")      val name: String,
    @SerializedName("points")    val points: Int,
    @SerializedName("address")   val address: String,
    @SerializedName("createdAt") val createdAt: String, // Update to Instant if needed
    @SerializedName("updatedAt") val updatedAt: String, // Update to instant if needed
)

// Makes user-related synchronous requests to the database
interface UserApi {
    fun registerUser(data: UserRegistration)
    fun loginUser(data: Login)
    fun getUser(): User
    fun updateUser(data: UserUpdate)
    fun deleteUser()
}

class UserRemoteDataSource(
    private val userApi: UserApi,
    private val ioDispatcher: CoroutineDispatcher
) {
    /**
     * Registers the user to the database and returns the user id.
     * This executes on an IO-optimized thread pool, the function is main-safe.
     */
    suspend fun registerUser(data: UserRegistration) =
        withContext(ioDispatcher) {
            userApi.registerUser(data)
        }

    /**
     * Logs the user in and returns the token used for the auth-token header.
     * This executes on an IO-optimized thread pool, the function is main-safe.
     */
    suspend fun loginUser(data: Login) =
        withContext(ioDispatcher) {
            userApi.loginUser(data)
        }

    /**
     * Gets the currently logged in user's data from the database and returns the result.
     * This executes on an IO-optimized thread pool, the function is main-safe.
     */
    suspend fun getUser(): User =
        withContext(ioDispatcher) {
            userApi.getUser()
        }

    /**
     * Updates the currently logged in user's data in the database and returns the user's data
     * This executes on an IO-optimized thread pool, the function is main-safe.
     */
    suspend fun updateUser(data: UserUpdate) =
        withContext(ioDispatcher) {
            userApi.updateUser(data)
        }

    /**
     * Deletes the currently logged in user from the database and returns the deleted user's data.
     * This executes on an IO-optimized thread pool, the function is main-safe.
     */
    suspend fun deleteUser() =
        withContext(ioDispatcher) {
            userApi.deleteUser()
        }
}
