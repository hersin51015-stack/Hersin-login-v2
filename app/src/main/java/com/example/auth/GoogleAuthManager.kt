package com.example.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.example.model.UserAccount
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import org.json.JSONObject
import java.security.SecureRandom

class GoogleAuthManager(private val context: Context) {

    companion object {
        const val DEFAULT_WEB_CLIENT_ID = "922049258875-qbgi10cpskcmn9vhasenb1s2um3nte63.apps.googleusercontent.com"
    }

    private val credentialManager = CredentialManager.create(context)

    suspend fun signInWithGoogle(
        serverClientId: String? = DEFAULT_WEB_CLIENT_ID,
        onSuccess: (UserAccount) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            // Generate a random nonce
            val rawNonce = ByteArray(16).apply { SecureRandom().nextBytes(this) }.joinToString("") { "%02x".format(it) }

            // Use the Google Id option or fallback gracefully
            val googleIdOption = if (!serverClientId.isNullOrBlank()) {
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(serverClientId)
                    .setAutoSelectEnabled(false)
                    .setNonce(rawNonce)
                    .build()
            } else {
                null
            }

            val requestBuilder = GetCredentialRequest.Builder()
            if (googleIdOption != null) {
                requestBuilder.addCredentialOption(googleIdOption)
            }

            val request = requestBuilder.build()
            val response: GetCredentialResponse = credentialManager.getCredential(
                request = request,
                context = context
            )

            handleSignInResponse(response, onSuccess, onError)

        } catch (e: GetCredentialCancellationException) {
            Log.d("GoogleAuthManager", "Sign-in cancelled by user")
            onError("Sign-in was cancelled.")
        } catch (e: GetCredentialException) {
            Log.e("GoogleAuthManager", "Credential Manager error: ${e.message}", e)
            onError("Google Sign-In failed: ${e.localizedMessage ?: "Unknown error"}")
        } catch (e: Exception) {
            Log.e("GoogleAuthManager", "Sign-in unexpected exception: ${e.message}", e)
            onError("Google Sign-In error: ${e.localizedMessage ?: "Please try standard login."}")
        }
    }

    private fun handleSignInResponse(
        response: GetCredentialResponse,
        onSuccess: (UserAccount) -> Unit,
        onError: (String) -> Unit
    ) {
        val credential = response.credential
        when {
            credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val email = googleIdTokenCredential.id
                    val displayName = googleIdTokenCredential.displayName ?: email.substringBefore("@")
                    val username = email.substringBefore("@").replace(".", "_")

                    val userAccount = UserAccount(
                        username = username,
                        password = "google_oauth_authenticated",
                        email = email,
                        displayName = displayName,
                        isGoogleUser = true,
                        avatarEmoji = "🌐"
                    )
                    onSuccess(userAccount)
                } catch (e: GoogleIdTokenParsingException) {
                    Log.e("GoogleAuthManager", "Invalid token response", e)
                    onError("Failed to parse Google account credentials.")
                }
            }
            else -> {
                Log.w("GoogleAuthManager", "Unexpected credential type: ${credential.type}")
                onError("Received unexpected credential type from Google.")
            }
        }
    }
}
