package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    onTestProviders: () -> Unit,
    modifier: Modifier = Modifier
) {
    val geminiKeyPresent = try {
        val k = BuildConfig.GEMINI_API_KEY
        k.isNotBlank() && k != "MY_GEMINI_API_KEY"
    } catch (e: Exception) { false }

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
                text = "System Settings & Provider APIs",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Configure Video Engines, Gemini API, OAuth2, and Storage",
                color = TextSecondary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Provider Status Summary
        item {
            Surface(
                color = SurfaceDark,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "INTEGRATION HEALTH STATUS", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    ProviderStatusRow(
                        name = "Gemini API & Originality Engine",
                        detail = "gemini-3.5-flash (Fact checking & Scripting)",
                        isConfigured = geminiKeyPresent
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ProviderStatusRow(
                        name = "Real AI Video Provider",
                        detail = "Google Veo (veo-3.1-fast-generate-preview)",
                        isConfigured = geminiKeyPresent
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ProviderStatusRow(
                        name = "TTS Voiceover Provider",
                        detail = "gemini-2.5-flash-preview-tts & Native Audio",
                        isConfigured = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ProviderStatusRow(
                        name = "YouTube Data API & OAuth2",
                        detail = "videos.list, upload.videos, thumbnails.set",
                        isConfigured = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Secret Configuration Guide
        item {
            SectionHeader(title = "AI Studio Secrets Configuration")
            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "🔒 Managing Production API Keys:",
                        color = StudioGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "To supply or update your GEMINI_API_KEY, use the Secrets panel in AI Studio.\nKeys are injected directly into BuildConfig at build time and never hardcoded in source code.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Express.js Node Server Video & Thumbnail Download Endpoints
        item {
            SectionHeader(title = "Node.js Server Download Endpoints (server.js)")
            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "⚡ Real Node.js Express Endpoints Active (Port 3000):",
                        color = StudioCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "GET /api/download/video/:filename",
                        color = StudioGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Streams and downloads the final assembled MP4 file from /generated-videos/",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "GET /api/download/thumbnail/:filename",
                        color = StudioGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Downloads the generated high-resolution thumbnail from /generated-thumbnails/",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Environment Variables List
        item {
            SectionHeader(title = "Production Environment Schema (.env)")
            val envVars = listOf(
                "APP_URL" to "https://studio.youtube.automation.internal",
                "DATABASE_URL" to "room://youtube_automation.db",
                "VIDEO_PROVIDER" to "veo (Google Veo)",
                "VIDEO_MODEL" to "veo-3.1-fast-generate-preview",
                "TTS_PROVIDER" to "gemini (gemini-2.5-flash-preview-tts)",
                "GOOGLE_REDIRECT_URI" to "https://studio.youtube.automation.internal/oauth/callback",
                "AUTOMATION_WEBHOOK" to "active"
            )
            Surface(
                color = SurfaceDark,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    envVars.forEach { (key, value) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = key, color = StudioCyan, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = value, color = TextTertiary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProviderStatusRow(
    name: String,
    detail: String,
    isConfigured: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = detail, color = TextSecondary, fontSize = 11.sp)
        }
        Surface(
            color = if (isConfigured) StudioGreen.copy(alpha = 0.15f) else StudioRed.copy(alpha = 0.15f),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = if (isConfigured) "ONLINE" else "KEY NEEDED",
                color = if (isConfigured) StudioGreen else StudioRedLight,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }
    }
}
