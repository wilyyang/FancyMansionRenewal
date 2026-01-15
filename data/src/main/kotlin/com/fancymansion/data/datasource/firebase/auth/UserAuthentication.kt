package com.fancymansion.data.datasource.firebase.auth

import com.fancymansion.data.datasource.firebase.auth.model.UserInitData

interface UserAuthentication {
    suspend fun signInWithGoogle(idToken: String): UserInitData
}