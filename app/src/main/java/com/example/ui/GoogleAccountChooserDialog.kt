package com.example.ui

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.UserAccount

@Composable
fun GoogleAccountChooserDialog(
    onDismiss: () -> Unit,
    onAccountSelected: (UserAccount) -> Unit
) {
    var showCustomInput by remember { mutableStateOf(false) }
    var customEmailInput by remember { mutableStateOf("") }
    var isSigningIn by remember { mutableStateOf(false) }
    var signingInAccountName by remember { mutableStateOf("") }

    fun logUserIn(email: String) {
        isSigningIn = true
        signingInAccountName = email
        val name = email.substringBefore("@")
            .replace(".", " ")
            .replace("_", " ")
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
            .ifBlank { "Google User" }

        val account = UserAccount(
            username = email.substringBefore("@").replace(".", "_"),
            password = "google_authenticated",
            email = email,
            displayName = name,
            isGoogleUser = true,
            avatarEmoji = "🌐"
        )
        onAccountSelected(account)
    }

    Dialog(
        onDismissRequest = { if (!isSigningIn) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .clickable { if (!isSigningIn) onDismiss() }
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = false) {}
                    .clip(RoundedCornerShape(12.dp))
                    .testTag("google_account_chooser_window"),
                color = Color.White,
                shadowElevation = 16.dp,
                border = BorderStroke(1.dp, Color(0xFFDADCE0))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 16.dp)
                ) {
                    // Top Bar Header mimicking Google Chrome / Google Accounts Window
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            GoogleFourColorGLogo(size = 20.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Sign in with Google",
                                color = Color(0xFF3C4043),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color(0xFF5F6368),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFFF1F3F4), thickness = 1.dp)

                    if (isSigningIn) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp, horizontal = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF1A73E8),
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Signing in with Google...",
                                color = Color(0xFF202124),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = signingInAccountName,
                                color = Color(0xFF5F6368),
                                fontSize = 13.sp
                            )
                        }
                    } else {
                        // -------------------------------------------------------------
                        // Official "Choose an account" layout matching the screenshot
                        // -------------------------------------------------------------
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 20.dp)
                        ) {
                            Text(
                                text = "Choose an account",
                                color = Color(0xFF202124),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = FontFamily.SansSerif
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "to continue to ",
                                    color = Color(0xFF5F6368),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "delta-case-hc9s2.firebaseapp.com",
                                    color = Color(0xFF1A73E8),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // 1. Account Option: Quick 1-tap sign-in
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable {
                                        logUserIn("google.user@gmail.com")
                                    }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color(0xFF00897B), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "G",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Text(
                                    text = "google.user@gmail.com",
                                    color = Color(0xFF3C4043),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }

                            HorizontalDivider(color = Color(0xFFE8EAED), thickness = 1.dp)

                            // 2. "Use another account" (Allows ANY user to type any account)
                            if (!showCustomInput) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(6.dp))
                                        .clickable { showCustomInput = true }
                                        .padding(vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.AccountCircle,
                                        contentDescription = "Use another account",
                                        tint = Color(0xFF5F6368),
                                        modifier = Modifier.size(28.dp)
                                    )

                                    Spacer(modifier = Modifier.width(20.dp))

                                    Text(
                                        text = "Use another account",
                                        color = Color(0xFF3C4043),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            } else {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFF8F9FA), RoundedCornerShape(8.dp))
                                        .border(1.dp, Color(0xFFDADCE0), RoundedCornerShape(8.dp))
                                        .padding(14.dp)
                                ) {
                                    Text(
                                        text = "Enter your Google email:",
                                        color = Color(0xFF202124),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    BasicTextField(
                                        value = customEmailInput,
                                        onValueChange = { customEmailInput = it },
                                        singleLine = true,
                                        textStyle = TextStyle(
                                            color = Color(0xFF202124),
                                            fontSize = 15.sp
                                        ),
                                        cursorBrush = SolidColor(Color(0xFF1A73E8)),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Email,
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                if (customEmailInput.isNotBlank()) {
                                                    val input = customEmailInput.trim()
                                                    val email = if (input.contains("@")) input else "$input@gmail.com"
                                                    logUserIn(email)
                                                }
                                            }
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.White, RoundedCornerShape(4.dp))
                                            .border(1.dp, Color(0xFF1A73E8), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 12.dp, vertical = 10.dp),
                                        decorationBox = { innerTextField ->
                                            if (customEmailInput.isEmpty()) {
                                                Text(
                                                    text = "e.g. yourname@gmail.com",
                                                    color = Color(0xFF80868B),
                                                    fontSize = 14.sp
                                                )
                                            }
                                            innerTextField()
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Button(
                                            onClick = {
                                                if (customEmailInput.isNotBlank()) {
                                                    val input = customEmailInput.trim()
                                                    val email = if (input.contains("@")) input else "$input@gmail.com"
                                                    logUserIn(email)
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8)),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text("Sign in", color = Color.White, fontWeight = FontWeight.Medium)
                                        }
                                    }
                                }
                            }

                            HorizontalDivider(color = Color(0xFFE8EAED), thickness = 1.dp)

                            Spacer(modifier = Modifier.height(36.dp))

                            // Bottom Footer: Language selector + Help Privacy Terms
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { }
                                ) {
                                    Text(
                                        text = "English (United States)",
                                        color = Color(0xFF5F6368),
                                        fontSize = 12.sp
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Language",
                                        tint = Color(0xFF5F6368),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Help",
                                        color = Color(0xFF5F6368),
                                        fontSize = 12.sp,
                                        modifier = Modifier.clickable { }
                                    )
                                    Text(
                                        text = "Privacy",
                                        color = Color(0xFF5F6368),
                                        fontSize = 12.sp,
                                        modifier = Modifier.clickable { }
                                    )
                                    Text(
                                        text = "Terms",
                                        color = Color(0xFF5F6368),
                                        fontSize = 12.sp,
                                        modifier = Modifier.clickable { }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GoogleFourColorGLogo(size: androidx.compose.ui.unit.Dp = 20.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .background(Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "G",
            color = Color(0xFF4285F4),
            fontSize = (size.value * 0.75).sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.SansSerif
        )
    }
}
