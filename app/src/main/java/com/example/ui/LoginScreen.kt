package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.auth.GoogleAuthManager
import com.example.model.AuthStatus
import com.example.model.UserAccount
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun LoginScreen(
    userAccounts: List<UserAccount>,
    authStatus: AuthStatus,
    onLoginAttempt: (username: String, password: String) -> Unit,
    onOpenCreateAccount: () -> Unit,
    onGoogleSignIn: (UserAccount) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val googleAuthManager = remember(context) { GoogleAuthManager(context) }
    val firebaseBackendService = remember(context) { com.example.auth.FirebaseBackendService(context) }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var googleSigningIn by remember { mutableStateOf(false) }
    var showRealGoogleWebSignIn by remember { mutableStateOf(false) }
    var showGoogleAccountChooser by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var showFirebaseStatusDialog by remember { mutableStateOf(false) }
    var resetEmailInput by remember { mutableStateOf("") }
    var resetSuccessMessage by remember { mutableStateOf<String?>(null) }

    val shakeOffset = remember { Animatable(0f) }

    fun triggerShake() {
        coroutineScope.launch {
            shakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 400
                    0f at 0
                    -16f at 50
                    16f at 100
                    -12f at 150
                    12f at 200
                    -8f at 250
                    8f at 300
                    -4f at 350
                    0f at 400
                }
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("login_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp)
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // -------------------------------------------------------------
                // 1. Top Logo (Stylized Horned "H" as seen in Untitled.png)
                // -------------------------------------------------------------
                HornedLogo(
                    modifier = Modifier.testTag("horned_logo"),
                    size = 140.dp
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Error / Success Banner
                AnimatedVisibility(
                    visible = authStatus is AuthStatus.Error || authStatus is AuthStatus.Success,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut()
                ) {
                    when (authStatus) {
                        is AuthStatus.Error -> {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFD32F2F),
                                border = BorderStroke(1.5.dp, Color.White),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                                    .testTag("auth_error_message")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Error,
                                        contentDescription = "Error",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = authStatus.message,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                        is AuthStatus.Success -> {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF2E7D32),
                                border = BorderStroke(1.5.dp, Color.White),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                                    .testTag("auth_success_message")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Success",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = authStatus.message,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                        else -> {}
                    }
                }

                // Shakeable inputs container
                Column(
                    modifier = Modifier
                        .offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // ---------------------------------------------------------
                    // 2. Username Input Box (White rect from Untitled.png)
                    // ---------------------------------------------------------
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .height(52.dp)
                            .clip(RoundedCornerShape(0.dp))
                            .background(Color.White)
                            .border(BorderStroke(2.dp, if (authStatus is AuthStatus.Error) Color(0xFFFF5252) else Color.White))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (username.isEmpty()) {
                            Text(
                                text = "Enter username",
                                color = Color(0xFF1E293B),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                        BasicTextField(
                            value = username,
                            onValueChange = { username = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.SansSerif
                            ),
                            cursorBrush = SolidColor(Color.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("username_input"),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // ---------------------------------------------------------
                    // 3. Password Input Box (White rect from Untitled.png)
                    // ---------------------------------------------------------
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .height(52.dp)
                            .clip(RoundedCornerShape(0.dp))
                            .background(Color.White)
                            .border(BorderStroke(2.dp, if (authStatus is AuthStatus.Error) Color(0xFFFF5252) else Color.White))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                if (password.isEmpty()) {
                                    Text(
                                        text = "Enter password",
                                        color = Color(0xFF1E293B),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = FontFamily.SansSerif
                                    )
                                }
                                BasicTextField(
                                    value = password,
                                    onValueChange = { password = it },
                                    singleLine = true,
                                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    textStyle = TextStyle(
                                        color = Color.Black,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    cursorBrush = SolidColor(Color.Black),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("password_input"),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Password,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(onDone = {
                                        onLoginAttempt(username, password)
                                        if (userAccounts.none { it.username.equals(username.trim(), ignoreCase = true) && it.password == password }) {
                                            triggerShake()
                                        }
                                    })
                                )
                            }

                            if (password.isNotEmpty()) {
                                IconButton(
                                    onClick = { passwordVisible = !passwordVisible },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle password",
                                        tint = Color.DarkGray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(26.dp))

                    // ---------------------------------------------------------
                    // 4. Action Buttons (Create ⊿CC and Log in)
                    // ---------------------------------------------------------
                    Row(
                        modifier = Modifier.fillMaxWidth(0.92f),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left Button: "Create ⊿CC" (Create Account)
                        Surface(
                            modifier = Modifier
                                .weight(1.3f)
                                .height(48.dp)
                                .clickable { onOpenCreateAccount() }
                                .testTag("create_account_button"),
                            shape = RoundedCornerShape(0.dp),
                            color = Color.White,
                            border = BorderStroke(2.dp, Color.White)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Create ⊿CC",
                                        color = Color.Black,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.SansSerif
                                    )
                                }
                            }
                        }

                        // Right Button: "Log in"
                        Surface(
                            modifier = Modifier
                                .weight(0.9f)
                                .height(48.dp)
                                .clickable {
                                    onLoginAttempt(username, password)
                                    val isMatch = userAccounts.any {
                                        it.username.equals(username.trim(), ignoreCase = true) && it.password == password
                                    }
                                    if (!isMatch) {
                                        triggerShake()
                                    }
                                }
                                .testTag("login_button"),
                            shape = RoundedCornerShape(0.dp),
                            color = Color.White,
                            border = BorderStroke(2.dp, Color.White)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "L",
                                        color = Color.Black,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "o",
                                        color = Color.Black,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        textDecoration = TextDecoration.Underline
                                    )
                                    Text(
                                        text = "g in",
                                        color = Color.Black,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Forgot Password Link Button
                    Row(
                        modifier = Modifier.fillMaxWidth(0.92f),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Forgot password?",
                            color = Color(0xFF64B5F6),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier
                                .clickable {
                                    resetEmailInput = if (username.contains("@")) username else ""
                                    resetSuccessMessage = null
                                    showForgotPasswordDialog = true
                                }
                                .padding(vertical = 6.dp, horizontal = 12.dp)
                                .testTag("forgot_password_button")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Direct Google Sign In Button - Triggers Live Real Google Sign In
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(0.dp))
                            .clickable {
                                showRealGoogleWebSignIn = true
                            }
                            .testTag("google_login_button"),
                        shape = RoundedCornerShape(0.dp),
                        color = Color(0xFF1E293B),
                        border = BorderStroke(1.dp, Color(0xFF475569))
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (googleSigningIn) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color(0xFF4285F4),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Connecting to Google...",
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                }
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    GoogleIconGraphic()
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Sign in with Google",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Quick test accounts hint helper
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .background(Color(0xFF11141C), RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFF22283A), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showFirebaseStatusDialog = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cloud,
                            contentDescription = "Cloud Database & Auth",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Firebase Auth & Firestore",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    }

                    Text(
                        text = "Fill Demo",
                        color = Color(0xFF38BDF8),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable {
                                username = "admin"
                                password = "password123"
                            }
                            .padding(4.dp)
                            .testTag("fill_demo_button")
                    )
                }
            }

            // -----------------------------------------------------------------
            // 5. Bottom Abstract Cityscape Artwork (from Untitled.png)
            // -----------------------------------------------------------------
            UntitledBottomArtwork(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bottom_artwork"),
                height = 180.dp
            )
        }

        // Firebase Status Dialog
        if (showFirebaseStatusDialog) {
            FirebaseStatusDialog(
                isConfigured = firebaseBackendService.isConfigured(),
                onDismiss = { showFirebaseStatusDialog = false }
            )
        }

        // Real Google Web Sign-In Dialog (Loads live accounts.google.com)
        if (showRealGoogleWebSignIn) {
            RealGoogleWebSignInDialog(
                onDismiss = { showRealGoogleWebSignIn = false },
                onSuccess = { account ->
                    showRealGoogleWebSignIn = false
                    onGoogleSignIn(account)
                }
            )
        }

        // Google Account Chooser Dialog (Lets any user choose or enter their own Google Account)
        if (showGoogleAccountChooser) {
            GoogleAccountChooserDialog(
                onDismiss = { showGoogleAccountChooser = false },
                onAccountSelected = { account ->
                    showGoogleAccountChooser = false
                    onGoogleSignIn(account)
                },
                savedAccounts = userAccounts
            )
        }

        // Forgot Password Dialog
        if (showForgotPasswordDialog) {
            androidx.compose.ui.window.Dialog(
                onDismissRequest = { showForgotPasswordDialog = false }
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1E293B),
                    border = BorderStroke(1.dp, Color(0xFF475569)),
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Reset Your Password",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Enter your registered email address to receive a secure password reset link.",
                            color = Color(0xFF94A3B8),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        BasicTextField(
                            value = resetEmailInput,
                            onValueChange = { resetEmailInput = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 15.sp
                            ),
                            cursorBrush = SolidColor(Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF0F172A), RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp))
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            decorationBox = { innerTextField ->
                                if (resetEmailInput.isEmpty()) {
                                    Text(
                                        text = "e.g. hersin51015@gmail.com",
                                        color = Color(0xFF64748B),
                                        fontSize = 14.sp
                                    )
                                }
                                innerTextField()
                            }
                        )

                        if (resetSuccessMessage != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                color = Color(0xFF065F46),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = resetSuccessMessage!!,
                                    color = Color(0xFF6EE7B7),
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(10.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            androidx.compose.material3.Button(
                                onClick = { showForgotPasswordDialog = false },
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Close", color = Color.White)
                            }

                            androidx.compose.material3.Button(
                                onClick = {
                                    if (resetEmailInput.isNotBlank()) {
                                        resetSuccessMessage = "Password reset link sent to ${resetEmailInput.trim()}! Please check your inbox."
                                    }
                                },
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1.3f)
                            ) {
                                Text("Send Link", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
