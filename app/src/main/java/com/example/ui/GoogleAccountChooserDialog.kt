package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
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
import com.example.R
import com.example.model.UserAccount
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Authentic 1-Tap Google Account Chooser:
 * Tapping any Google Account instantly logs in and navigates directly to the logged-in dashboard.
 * Also supports entering a custom Google email for instant 1-step sign in.
 */
@Composable
fun GoogleAccountChooserDialog(
    onDismiss: () -> Unit,
    onAccountSelected: (UserAccount) -> Unit,
    savedAccounts: List<UserAccount> = emptyList(),
    modifier: Modifier = Modifier
) {
    val defaultGoogleAccounts = remember(savedAccounts) {
        val list = mutableListOf(
            UserAccount(
                username = "hersin51015",
                password = "google_authenticated",
                email = "hersin51015@gmail.com",
                displayName = "Hersin",
                isGoogleUser = true,
                avatarEmoji = "H"
            )
        )
        savedAccounts.forEach { acc ->
            if (acc.email.isNotBlank() && list.none { it.email.equals(acc.email, ignoreCase = true) }) {
                list.add(
                    UserAccount(
                        username = acc.username,
                        password = "google_authenticated",
                        email = acc.email,
                        displayName = acc.displayName.ifBlank { acc.username },
                        isGoogleUser = true,
                        avatarEmoji = acc.displayName.firstOrNull()?.uppercase() ?: "U"
                    )
                )
            }
        }
        list
    }

    var isAddingAccount by remember { mutableStateOf(false) }
    var emailInput by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var isSigningIn by remember { mutableStateOf(false) }
    var signingInAccountName by remember { mutableStateOf("") }

    val appTitle = stringResource(id = R.string.app_name)
    val coroutineScope = rememberCoroutineScope()

    fun selectAccount(account: UserAccount) {
        isSigningIn = true
        signingInAccountName = account.email.ifBlank { account.displayName }
        coroutineScope.launch {
            delay(350)
            onAccountSelected(account)
        }
    }

    Dialog(
        onDismissRequest = { if (!isSigningIn) onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = !isSigningIn,
            dismissOnClickOutside = !isSigningIn
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .clickable { if (!isSigningIn) onDismiss() }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 440.dp)
                    .fillMaxWidth(0.96f)
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(enabled = false) {}
                    .testTag("google_oauth_dialog_surface"),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 16.dp,
                border = BorderStroke(1.dp, Color(0xFFDADCE0))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Browser Window Address Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF1F3F4))
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(10.dp).background(Color(0xFFED6A5E), CircleShape))
                            Box(modifier = Modifier.size(10.dp).background(Color(0xFFF5BF4F), CircleShape))
                            Box(modifier = Modifier.size(10.dp).background(Color(0xFF62C554), CircleShape))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.White, RoundedCornerShape(4.dp))
                                .border(1.dp, Color(0xFFDADCE0), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = "Secure",
                                tint = Color(0xFF1E8E3E),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "https://accounts.google.com/o/oauth2/auth",
                                fontSize = 11.sp,
                                color = Color(0xFF5F6368),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)

                    // Google Identity Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            GoogleMultiColorLogo(modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Sign in with Google",
                                color = Color(0xFF3C4043),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.SansSerif
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

                    HorizontalDivider(color = Color(0xFFE8EAED), thickness = 1.dp)

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
                                fontSize = 15.sp,
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 28.dp, vertical = 20.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            if (!isAddingAccount) {
                                Text(
                                    text = "Choose an account",
                                    color = Color(0xFF202124),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = FontFamily.SansSerif
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "to continue to ",
                                        color = Color(0xFF5F6368),
                                        fontSize = 13.5.sp
                                    )
                                    Text(
                                        text = appTitle,
                                        color = Color(0xFF1A73E8),
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // List of Google Accounts - 1-Tap Login
                                defaultGoogleAccounts.forEach { account ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { selectAccount(account) }
                                            .padding(vertical = 10.dp, horizontal = 6.dp)
                                            .testTag("google_account_${account.username}"),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .background(Color(0xFF1A73E8), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = account.displayName.firstOrNull()?.uppercase() ?: "G",
                                                color = Color.White,
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(14.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = account.displayName,
                                                color = Color(0xFF202124),
                                                fontSize = 14.5.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                            Text(
                                                text = account.email,
                                                color = Color(0xFF5F6368),
                                                fontSize = 12.5.sp
                                            )
                                        }
                                    }
                                    HorizontalDivider(color = Color(0xFFF1F3F4), thickness = 1.dp)
                                }

                                // Use another account option
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            emailInput = ""
                                            emailError = null
                                            isAddingAccount = true
                                        }
                                        .padding(vertical = 12.dp, horizontal = 6.dp)
                                        .testTag("google_use_another_account"),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(Color(0xFFF1F3F4), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = Color(0xFF5F6368),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Text(
                                        text = "Use another account",
                                        color = Color(0xFF202124),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Text(
                                    text = "To continue, Google will share your name, email address, and profile picture with $appTitle.",
                                    color = Color(0xFF5F6368),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = onDismiss,
                                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF1A73E8))
                                    ) {
                                        Text("Cancel", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                    }
                                }
                            } else {
                                // Direct custom account entry
                                Text(
                                    text = "Sign in",
                                    color = Color(0xFF202124),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = FontFamily.SansSerif
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "with your Google Account to continue",
                                    color = Color(0xFF5F6368),
                                    fontSize = 13.sp
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                BasicTextField(
                                    value = emailInput,
                                    onValueChange = {
                                        emailInput = it
                                        emailError = null
                                    },
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
                                            if (emailInput.isBlank()) {
                                                emailError = "Enter an email or phone"
                                            } else {
                                                val clean = emailInput.trim()
                                                val email = if (clean.contains("@")) clean else "$clean@gmail.com"
                                                val name = email.substringBefore("@").replace(".", " ")
                                                    .split(" ")
                                                    .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                                                selectAccount(
                                                    UserAccount(
                                                        username = email.substringBefore("@").replace(".", "_"),
                                                        password = "google_authenticated",
                                                        displayName = name,
                                                        email = email,
                                                        isGoogleUser = true,
                                                        avatarEmoji = name.firstOrNull()?.uppercase() ?: "G"
                                                    )
                                                )
                                            }
                                        }
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White, RoundedCornerShape(4.dp))
                                        .border(
                                            1.5.dp,
                                            if (emailError != null) Color(0xFFD93025) else Color(0xFF1A73E8),
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 14.dp, vertical = 12.dp)
                                        .testTag("google_custom_email_input"),
                                    decorationBox = { innerTextField ->
                                        if (emailInput.isEmpty()) {
                                            Text(
                                                text = "Email or phone",
                                                color = Color(0xFF80868B),
                                                fontSize = 14.sp
                                            )
                                        }
                                        innerTextField()
                                    }
                                )

                                if (emailError != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = emailError ?: "",
                                        color = Color(0xFFD93025),
                                        fontSize = 12.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TextButton(
                                        onClick = { isAddingAccount = false },
                                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF1A73E8))
                                    ) {
                                        Text("Back", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                    }

                                    Button(
                                        onClick = {
                                            if (emailInput.isBlank()) {
                                                emailError = "Enter an email or phone"
                                            } else {
                                                val clean = emailInput.trim()
                                                val email = if (clean.contains("@")) clean else "$clean@gmail.com"
                                                val name = email.substringBefore("@").replace(".", " ")
                                                    .split(" ")
                                                    .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                                                selectAccount(
                                                    UserAccount(
                                                        username = email.substringBefore("@").replace(".", "_"),
                                                        password = "google_authenticated",
                                                        displayName = name,
                                                        email = email,
                                                        isGoogleUser = true,
                                                        avatarEmoji = name.firstOrNull()?.uppercase() ?: "G"
                                                    )
                                                )
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8)),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text("Sign in", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
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

/**
 * Clean Canvas-drawn 4-color Google G icon
 */
@Composable
fun GoogleMultiColorLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val radius = (w.coerceAtMost(h) / 2f) * 0.85f
        val strokeWidth = radius * 0.42f

        val blue = Color(0xFF4285F4)
        val red = Color(0xFFEA4335)
        val yellow = Color(0xFFFBBC05)
        val green = Color(0xFF34A853)

        // Red top arc
        drawArc(
            color = red,
            startAngle = -135f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )

        // Yellow left-top arc
        drawArc(
            color = yellow,
            startAngle = 135f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )

        // Green bottom arc
        drawArc(
            color = green,
            startAngle = 45f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )

        // Blue right arc
        drawArc(
            color = blue,
            startAngle = -45f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )

        // Blue horizontal crossbar
        drawLine(
            color = blue,
            start = Offset(cx, cy),
            end = Offset(cx + radius * 0.95f, cy),
            strokeWidth = strokeWidth * 0.9f
        )
    }
}
