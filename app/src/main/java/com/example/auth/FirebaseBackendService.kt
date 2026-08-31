package com.example.auth

import android.content.Context
import android.util.Log
import com.example.model.UserAccount
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

/**
 * Service to manage Firebase Authentication and Firestore Database persistence.
 * If Firebase has not been initialized with a google-services.json yet,
 * it safely provides informative status and fallback persistence.
 */
class FirebaseBackendService(private val context: Context) {

    private val isFirebaseAvailable: Boolean
        get() = try {
            FirebaseApp.getApps(context).isNotEmpty()
        } catch (e: Exception) {
            false
        }

    val auth: FirebaseAuth?
        get() = if (isFirebaseAvailable) {
            try {
                FirebaseAuth.getInstance()
            } catch (e: Exception) {
                null
            }
        } else null

    val firestore: FirebaseFirestore?
        get() = if (isFirebaseAvailable) {
            try {
                FirebaseFirestore.getInstance()
            } catch (e: Exception) {
                null
            }
        } else null

    fun isConfigured(): Boolean = isFirebaseAvailable

    suspend fun saveUserToFirestore(account: UserAccount): Result<Unit> {
        val db = firestore ?: return Result.failure(Exception("Firebase Firestore is not initialized."))
        return try {
            val userDoc = hashMapOf(
                "username" to account.username,
                "email" to account.email,
                "displayName" to account.displayName,
                "isGoogleUser" to account.isGoogleUser,
                "avatarEmoji" to account.avatarEmoji,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("users").document(account.username)
                .set(userDoc, SetOptions.merge())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirebaseBackendService", "Failed to save user to Firestore", e)
            Result.failure(e)
        }
    }

    suspend fun deleteUserFromFirestore(username: String): Result<Unit> {
        val db = firestore ?: return Result.failure(Exception("Firebase Firestore is not initialized."))
        return try {
            db.collection("users").document(username).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirebaseBackendService", "Failed to delete user from Firestore", e)
            Result.failure(e)
        }
    }

    suspend fun fetchUsersFromFirestore(): Result<List<UserAccount>> {
        val db = firestore ?: return Result.failure(Exception("Firebase Firestore is not initialized."))
        return try {
            val snapshot = db.collection("users").get().await()
            val list = snapshot.documents.mapNotNull { doc ->
                val username = doc.getString("username") ?: doc.id
                val email = doc.getString("email") ?: ""
                val displayName = doc.getString("displayName") ?: username
                val isGoogle = doc.getBoolean("isGoogleUser") ?: false
                val avatar = doc.getString("avatarEmoji") ?: "👤"
                UserAccount(
                    username = username,
                    password = "firebase_synced",
                    email = email,
                    displayName = displayName,
                    isGoogleUser = isGoogle,
                    avatarEmoji = avatar
                )
            }
            Result.success(list)
        } catch (e: Exception) {
            Log.e("FirebaseBackendService", "Failed to fetch users from Firestore", e)
            Result.failure(e)
        }
    }

    suspend fun registerWithEmailPassword(email: String, password: String): Result<FirebaseUser> {
        val firebaseAuth = auth ?: return Result.failure(Exception("Firebase Auth is not initialized."))
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return Result.failure(Exception("User creation failed."))
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithEmailPassword(email: String, password: String): Result<FirebaseUser> {
        val firebaseAuth = auth ?: return Result.failure(Exception("Firebase Auth is not initialized."))
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: return Result.failure(Exception("Sign in failed."))
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
