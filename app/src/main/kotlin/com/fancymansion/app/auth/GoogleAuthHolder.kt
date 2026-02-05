package com.fancymansion.app.auth

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

object GoogleAuthHolder {
    @Volatile private var client: GoogleSignInClient? = null

    fun get(context: Context): GoogleSignInClient {
        return client ?: synchronized(this) {
            client ?: run {
                val appContext = context.applicationContext
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(appContext.getString(com.fancymansion.app.R.string.default_web_client_id))
                    .requestEmail()
                    .build()

                GoogleSignIn.getClient(appContext, gso).also { client = it }
            }
        }
    }
}