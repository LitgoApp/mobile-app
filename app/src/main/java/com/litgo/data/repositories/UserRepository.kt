package com.litgo.data.repositories

import com.litgo.data.models.User
import com.litgo.data.models.UserRegistration
import com.litgo.data.dataSources.UserRemoteDataSource
import com.litgo.data.models.Login
import com.litgo.data.models.UserUpdate

class UserRepository(private val userRemoteDataSource: UserRemoteDataSource) {
    suspend fun registerUser(data: UserRegistration) = userRemoteDataSource.registerUser(data)
    suspend fun loginUser(data: Login) = userRemoteDataSource.loginUser(data)
    suspend fun getUser(): User = userRemoteDataSource.getUser()
    suspend fun updateUser(data: UserUpdate): User = userRemoteDataSource.updateUser(data)
    suspend fun deleteUser() = userRemoteDataSource.deleteUser()
}
