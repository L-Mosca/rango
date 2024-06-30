package com.example.rango.domain.models.user

import android.os.Parcelable
import com.google.firebase.auth.FirebaseUser
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String,
    val name: String?,
    val email: String?,
) : Parcelable

fun FirebaseUser?.toUser(): User? {
    val user = this?.let {
        User(
            uid = this.uid,
            name = this.displayName,
            email = this.email
        )
    }
    return user
}

