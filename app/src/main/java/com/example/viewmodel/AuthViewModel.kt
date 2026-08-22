package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.model.AuthStatus
import com.example.model.UserAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel : ViewModel() {

    private val _registeredAccounts = MutableStateFlow<List<UserAccount>>(
        listOf(
            UserAccount(
                username = "admin",
                password = "password123",
                email = "admin@example.com",
                displayName = "Administrator",
                avatarEmoji = "🛡️"
            ),
            UserAccount(
                username = "user",
                password = "password",
                email = "user@example.com",
                displayName = "Member",
                avatarEmoji = "✨"
            )
        )
    )
    val registeredAccounts: StateFlow<List<UserAccount>> = _registeredAccounts.asStateFlow()

    private val _authStatus = MutableStateFlow<AuthStatus>(AuthStatus.Idle)
    val authStatus: StateFlow<AuthStatus> = _authStatus.asStateFlow()

    private val _currentUser = MutableStateFlow<UserAccount?>(null)
    val currentUser: StateFlow<UserAccount?> = _currentUser.asStateFlow()

    fun login(usernameInput: String, passwordInput: String) {
        val trimmedUser = usernameInput.trim()
        val trimmedPass = passwordInput.trim()

        if (trimmedUser.isEmpty() || trimmedPass.isEmpty()) {
            _authStatus.value = AuthStatus.Error("Wrong! Please enter both username and password.")
            return
        }

        val foundAccount = _registeredAccounts.value.find { account ->
            account.username.equals(trimmedUser, ignoreCase = true) && account.password == trimmedPass
        }

        if (foundAccount != null) {
            _currentUser.value = foundAccount
            _authStatus.value = AuthStatus.Success(foundAccount, "Right! Login successful. Welcome back!")
        } else {
            _authStatus.value = AuthStatus.Error("Wrong! Invalid username or password.")
        }
    }

    fun registerAccount(account: UserAccount) {
        _registeredAccounts.update { currentList ->
            // Replace if existing with same username, else append
            currentList.filterNot { it.username.equals(account.username, ignoreCase = true) } + account
        }
        _currentUser.value = account
        _authStatus.value = AuthStatus.Success(
            account,
            if (account.isGoogleUser) "Right! Signed in with Google as ${account.email}"
            else "Right! Account created successfully."
        )
    }

    fun signOut() {
        _currentUser.value = null
        _authStatus.value = AuthStatus.Idle
    }

    fun clearStatus() {
        _authStatus.value = AuthStatus.Idle
    }
}
