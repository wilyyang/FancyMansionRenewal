package com.fancymansion.data.datasource.firebase.auth

import com.fancymansion.data.datasource.firebase.auth.model.UserInitData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class UserAuthenticationImpl(
    private val firebaseAuth: FirebaseAuth
) : UserAuthentication {
    override suspend fun signInWithGoogle(idToken: String): UserInitData {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = firebaseAuth.signInWithCredential(credential).await()
        val user = result.user ?: error("FirebaseAuth returned null user")

        val uid = user.uid
        val email = user.email ?: ""

        val initialNickname =
            user.displayName
                ?.takeIf { it.isNotBlank() }
                ?: email.substringBefore('@').takeIf { it.isNotBlank() }
                ?: "User"

        return UserInitData(
            uid = uid,
            email = email,
            initialNickname = initialNickname
        )
    }
}