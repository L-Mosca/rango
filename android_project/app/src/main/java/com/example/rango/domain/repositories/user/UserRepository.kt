package com.example.rango.domain.repositories.user

import com.example.rango.domain.models.user.User
import com.example.rango.domain.models.user.toUser
import com.example.rango.domain.local.datastore.PreferencesDataStoreContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRepository @Inject constructor(private val preferencesDataStore: PreferencesDataStoreContract) :
    UserRepositoryContract {

    override suspend fun registerNewUser(name: String, email: String, password: String): User? {
        val auth = FirebaseAuth.getInstance()
        return suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user?.updateProfile(profileUpdates)?.addOnCompleteListener { profileTask ->
                        if (profileTask.isSuccessful) {
                            val newUser = auth.currentUser.toUser()
                            continuation.resume(newUser)
                        } else {
                            continuation.resume(null)
                        }
                    }
                } else {
                    continuation.resume(null)
                }
            }
        }
    }

    override suspend fun login(email: String, password: String): User? {
        val auth = FirebaseAuth.getInstance()
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(auth.currentUser.toUser())
                } else {
                    continuation.resume(null)
                }
            }
        }
    }

    override suspend fun logout() {
        Firebase.auth.signOut()
        preferencesDataStore.saveUserData(null)
    }

    override suspend fun saveUserData(user: User?) = preferencesDataStore.saveUserData(user)

    override suspend fun getUserData(): User? = preferencesDataStore.getUserData()
}