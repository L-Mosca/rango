package com.example.rango.domain.repositories.user

import com.example.rango.domain.models.user.User

interface UserRepositoryContract {

    suspend fun registerNewUser(name: String, email: String, password: String): User?

    suspend fun login(email: String, password: String): User?

    suspend fun logout()

    suspend fun saveUserData(user: User?)

    suspend fun getUserData(): User?
}