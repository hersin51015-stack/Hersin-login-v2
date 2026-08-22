package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AuthStatus
import com.example.model.UserAccount
import com.example.ui.CreateAccountDialog
import com.example.ui.LoggedInScreen
import com.example.ui.LoginScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    containerColor = Color.Black
                ) { innerPadding ->
                    MainAppContent(
                        viewModel = authViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainAppContent(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val registeredAccounts by viewModel.registeredAccounts.collectAsStateWithLifecycle()
    val authStatus by viewModel.authStatus.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    var showCreateAccountDialog by remember { mutableStateOf(false) }

    if (currentUser != null) {
        // Authenticated "Right!" Screen
        LoggedInScreen(
            user = currentUser!!,
            onSignOut = {
                viewModel.signOut()
            },
            modifier = modifier
        )
    } else {
        // Login Screen styled like Untitled.png
        LoginScreen(
            userAccounts = registeredAccounts,
            authStatus = authStatus,
            onLoginAttempt = { user, pass ->
                viewModel.login(user, pass)
            },
            onOpenCreateAccount = {
                showCreateAccountDialog = true
            },
            onGoogleSignIn = { googleAccount ->
                viewModel.registerAccount(googleAccount)
            },
            modifier = modifier
        )
    }

    if (showCreateAccountDialog) {
        CreateAccountDialog(
            defaultGoogleEmail = "user@gmail.com",
            onDismiss = { showCreateAccountDialog = false },
            onAccountCreated = { newAccount ->
                viewModel.registerAccount(newAccount)
                showCreateAccountDialog = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    MyApplicationTheme(darkTheme = true) {
        LoginScreen(
            userAccounts = emptyList(),
            authStatus = AuthStatus.Idle,
            onLoginAttempt = { _, _ -> },
            onOpenCreateAccount = {},
            onGoogleSignIn = {}
        )
    }
}
