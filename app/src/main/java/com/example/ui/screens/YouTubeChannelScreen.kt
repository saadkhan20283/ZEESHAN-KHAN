package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.YoutubeAccountEntity
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*

@Composable
fun YouTubeChannelScreen(
    currentAccount: YoutubeAccountEntity?,
    allAccounts: List<YoutubeAccountEntity>,
    onConnectOAuth: () -> Unit,
    onDisconnect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "YouTube Channel & OAuth2",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Official Google OAuth integration for direct video upload and analytics",
                color = TextSecondary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Active Channel Card
        item {
            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(StudioRed.copy(alpha = 0.2f))
                                .border(1.5.dp, StudioRed, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (currentAccount?.channelImage?.isNotBlank() == true) {
                                AsyncImage(
                                    model = currentAccount.channelImage,
                                    contentDescription = "Avatar",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Text("YT", color = StudioRed, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentAccount?.channelName ?: "No Channel Connected",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (currentAccount?.isConnected == true) "${currentAccount.subscriberCount} • ${currentAccount.videoCount}" else "Connect via official Google sign-in",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        Surface(
                            color = if (currentAccount?.isConnected == true) StudioGreen.copy(alpha = 0.15f) else StudioRed.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (currentAccount?.isConnected == true) "CONNECTED" else "NOT LINKED",
                                color = if (currentAccount?.isConnected == true) StudioGreen else StudioRedLight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (currentAccount?.isConnected == true) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = onConnectOAuth,
                                colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Switch Channel", color = TextPrimary, fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = { onDisconnect(currentAccount.id) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioRedLight),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Disconnect", fontSize = 12.sp)
                            }
                        }
                    } else {
                        Button(
                            onClick = onConnectOAuth,
                            colors = ButtonDefaults.buttonColors(containerColor = StudioRed),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("connect_google_oauth_button")
                        ) {
                            Text("Connect YouTube via Google OAuth", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // OAuth Security & Permissions Disclosure
        item {
            SectionHeader(title = "Google Identity & Security Compliance")
            Surface(
                color = SurfaceDark,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "🔒 Security Guarantees & Privacy Architecture:", color = StudioGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "• Google Identity Services: Authentication utilizes official Android Credential Manager.", color = TextSecondary, fontSize = 11.sp)
                    Text(text = "• Zero Client Secrets in APK: Pure PKCE (RFC 7636 / RFC 8252) prevents client-side secret exposure.", color = TextSecondary, fontSize = 11.sp)
                    Text(text = "• We never store or inspect your Google account password.", color = TextSecondary, fontSize = 11.sp)
                    Text(text = "• Tokens are encrypted on device using Android Keystore/AES-256.", color = TextSecondary, fontSize = 11.sp)
                    Text(text = "• Pure YouTube Data API v3 endpoints with granular consent permissions.", color = TextSecondary, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Requested YouTube OAuth Scopes:", color = TextTertiary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = "https://www.googleapis.com/auth/youtube.upload\nhttps://www.googleapis.com/auth/youtube.readonly\nhttps://www.googleapis.com/auth/userinfo.profile\nhttps://www.googleapis.com/auth/userinfo.email", color = StudioCyan, fontSize = 10.sp)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Connected Accounts List (Multiple Channels)
        item {
            SectionHeader(title = "Managed Channels (${allAccounts.size})")
        }

        items(allAccounts) { acc ->
            Surface(
                color = SurfaceDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = acc.channelName, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${acc.subscriberCount} • Channel ID: ${acc.channelId}", color = TextSecondary, fontSize = 11.sp)
                    }
                    if (acc.isConnected) {
                        Text(text = "Active", color = StudioGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
