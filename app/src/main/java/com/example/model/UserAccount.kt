package com.example.model

data class UserAccount(
    val username: String,
    val password: String,
    val email: String,
    val displayName: String,
    val isGoogleUser: Boolean = false,
    val avatarEmoji: String = "👤"
)

sealed class AuthStatus {
    object Idle : AuthStatus()
    data class Success(val user: UserAccount, val message: String = "Right! Login successful.") : AuthStatus()
    data class Error(val message: String = "Wrong! Invalid username or password.") : AuthStatus()
}
