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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
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
    var step by remember { mutableStateOf(1) } // 1: Universal Sign in with Google, 2: Consent / Are you sure
    var emailInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isAuthenticating by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = { if (!isAuthenticating) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .clickable { if (!isAuthenticating) onDismiss() }
                .padding(horizontal = 20.dp, vertical = 24.dp),
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

                    if (step == 1) {
                        // -------------------------------------------------------------
                        // SCREEN 1: Universal Sign in with Google (Any user types their own account)
                        // -------------------------------------------------------------
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 28.dp, vertical = 24.dp)
                        ) {
                            Text(
                                text = "Sign in with Google",
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

                            Spacer(modifier = Modifier.height(26.dp))

                            Text(
                                text = "Email or phone",
                                color = if (errorMessage != null) Color(0xFFD93025) else Color(0xFF5F6368),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            BasicTextField(
                                value = emailInput,
                                onValueChange = {
                                    emailInput = it
                                    errorMessage = null
                                },
                                singleLine = true,
                                textStyle = TextStyle(
                                    color = Color(0xFF202124),
                                    fontSize = 16.sp
                                ),
                                cursorBrush = SolidColor(Color(0xFF1A73E8)),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        if (emailInput.isBlank()) {
                                            errorMessage = "Enter an email or phone number"
                                        } else {
                                            step = 2
                                        }
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White, RoundedCornerShape(4.dp))
                                    .border(
                                        width = if (errorMessage != null) 2.dp else 1.dp,
                                        color = if (errorMessage != null) Color(0xFFD93025) else Color(0xFF747775),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                decorationBox = { innerTextField ->
                                    if (emailInput.isEmpty()) {
                                        Text(
                                            text = "e.g. yourname@gmail.com",
                                            color = Color(0xFF80868B),
                                            fontSize = 15.sp
                                        )
                                    }
                                    innerTextField()
                                }
                            )

                            if (errorMessage != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = errorMessage!!,
                                    color = Color(0xFFD93025),
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = "To continue, Google will share your name, email address, language preference, and profile picture with this app.",
                                color = Color(0xFF5F6368),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(30.dp))

                            // Action buttons: Create account & Next
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Cancel",
                                    color = Color(0xFF1A73E8),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.clickable {
                                        onDismiss()
                                    }
                                )

                                Button(
                                    onClick = {
                                        if (emailInput.isBlank()) {
                                            errorMessage = "Enter an email or phone number"
                                        } else {
                                            step = 2
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8)),
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.testTag("google_next_button")
                                ) {
                                    Text("Next", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                                }
                            }

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
                    } else {
                        // -------------------------------------------------------------
                        // SCREEN 2: "Are you sure to authenticate?" / Consent Screen
                        // -------------------------------------------------------------
                        val cleanInput = emailInput.trim()
                        val finalEmail = if (cleanInput.contains("@")) cleanInput else "$cleanInput@gmail.com"
                        val displayName = finalEmail.substringBefore("@")
                            .replace(".", " ")
                            .replace("_", " ")
                            .split(" ")
                            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 28.dp, vertical = 20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { if (!isAuthenticating) step = 1 },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color(0xFF5F6368)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Confirm Authentication",
                                    color = Color(0xFF202124),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF8F9FA), RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .background(Color(0xFF1A73E8), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = displayName.take(1).uppercase(),
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = displayName,
                                        color = Color(0xFF202124),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = finalEmail,
                                        color = Color(0xFF5F6368),
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = Color(0xFFE8F0FE),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "Are you sure to authenticate?",
                                        color = Color(0xFF1967D2),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "delta-case-hc9s2.firebaseapp.com will access your Google name, email address ($finalEmail), and profile picture to sign you in securely.",
                                        color = Color(0xFF3C4043),
                                        fontSize = 13.sp,
                                        lineHeight = 17.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            if (isAuthenticating) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = Color(0xFF1A73E8),
                                        strokeWidth = 2.5.dp
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Signing in with Google...",
                                        color = Color(0xFF202124),
                                        fontSize = 14.sp
                                    )
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = onDismiss,
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text("Cancel", color = Color(0xFF1A73E8), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = {
                                            isAuthenticating = true
                                            val account = UserAccount(
                                                username = finalEmail.substringBefore("@").replace(".", "_"),
                                                password = "google_authenticated",
                                                email = finalEmail,
                                                displayName = displayName,
                                                isGoogleUser = true,
                                                avatarEmoji = "🌐"
                                            )
                                            onAccountSelected(account)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8)),
                                        shape = RoundedCornerShape(4.dp),
                                        modifier = Modifier.testTag("authorize_google_button")
                                    ) {
                                        Text("Authorize", color = Color.White, fontWeight = FontWeight.Medium)
                                    }
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
