package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
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
import com.example.data.model.*
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*

@Composable
fun CreateVideoScreen(
    initialTopic: String = "",
    initialCategory: VideoCategory = VideoCategory.CARS_LUXURY,
    onGenerate: (
        topic: String,
        category: VideoCategory,
        videoType: VideoType,
        duration: Int,
        language: String,
        voice: VoiceGender,
        visualStyle: String,
        aspectRatio: AspectRatio,
        quality: String,
        audience: String
    ) -> Unit,
    onSuggestTrends: () -> Unit,
    modifier: Modifier = Modifier
) {
    var topic by remember { mutableStateOf(initialTopic) }
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var videoType by remember { mutableStateOf(VideoType.SHORT) }
    var durationSeconds by remember { mutableStateOf(30) }
    var language by remember { mutableStateOf("English") }
    var voice by remember { mutableStateOf(VoiceGender.MALE_DEEP) }
    var visualStyle by remember { mutableStateOf("Cinematic photorealistic 8k, dramatic lighting, rich textures") }
    var aspectRatio by remember { mutableStateOf(AspectRatio.RATIO_9_16) }
    var quality by remember { mutableStateOf("1080p Full HD") }
    var audience by remember { mutableStateOf("Broad curious viewers with high retention interest") }

    val languages = listOf("English", "Hindi", "Urdu", "Roman Hindi", "Roman Urdu", "Arabic", "Spanish", "French")
    val visualStyles = listOf(
        "Cinematic photorealistic 8k, dramatic studio lighting",
        "35mm film grain, atmospheric warm vintage tones",
        "Cyberpunk futuristic neon, glowing reflections",
        "Clean minimal corporate infographics & 3D renders",
        "Whimsical colorful 3D Pixar-style animation",
        "Dark noir shadows, high contrast chiaroscuro"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 40.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Create AI Video",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Configure full autonomous pipeline from research to YouTube video",
                color = TextSecondary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 1. Topic Section
        item {
            Text(text = "VIDEO TOPIC", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = topic,
                onValueChange = { topic = it },
                placeholder = { Text("e.g. The Untold Engineering of Rare Tourbillon Watches", color = TextTertiary) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark,
                    focusedBorderColor = StudioRed,
                    unfocusedBorderColor = SurfaceBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("create_topic_input")
            )
            Spacer(modifier = Modifier.height(6.dp))
            TextButton(
                onClick = onSuggestTrends,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("🔥 Pick from Trending Topics", color = StudioGold, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 2. Video Category (22 Categories)
        item {
            Text(text = "CONTENT CATEGORY (22 SPECIALIZED FORMATS)", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VideoCategory.entries.forEach { cat ->
                    val isSelected = cat == selectedCategory
                    Surface(
                        color = if (isSelected) StudioRed else SurfaceElevated,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) StudioRed else SurfaceBorder),
                        modifier = Modifier
                            .clickable {
                                selectedCategory = cat
                                if (cat == VideoCategory.SHORTS_REELS_TIKTOK) {
                                    videoType = VideoType.SHORT
                                    aspectRatio = AspectRatio.RATIO_9_16
                                    durationSeconds = 30
                                }
                            }
                            .testTag("create_category_${cat.name.lowercase()}")
                    ) {
                        Text(
                            text = cat.displayName,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                color = SurfaceDark,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = "Style: ${selectedCategory.tone}", color = StudioGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Camera Motion: ${selectedCategory.defaultCameraMotion}", color = TextSecondary, fontSize = 11.sp)
                    Text(text = "Fact Standards: ${selectedCategory.factCheckingRequirement}", color = TextTertiary, fontSize = 10.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 3. Format & Duration
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "FORMAT", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(VideoType.SHORT, VideoType.LONG_FORM).forEach { t ->
                            val isSel = videoType == t
                            Surface(
                                color = if (isSel) StudioRed else SurfaceElevated,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        videoType = t
                                        aspectRatio = if (t == VideoType.SHORT) AspectRatio.RATIO_9_16 else AspectRatio.RATIO_16_9
                                        durationSeconds = if (t == VideoType.SHORT) 30 else 120
                                    }
                            ) {
                                Text(
                                    text = if (t == VideoType.SHORT) "Short 9:16" else "Long 16:9",
                                    color = if (isSel) Color.White else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                            }
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "DURATION", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    val durations = if (videoType == VideoType.SHORT) listOf(15, 30, 60) else listOf(60, 120, 300)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        durations.forEach { d ->
                            val isSel = durationSeconds == d
                            Surface(
                                color = if (isSel) StudioGold else SurfaceElevated,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { durationSeconds = d }
                            ) {
                                Text(
                                    text = "${d}s",
                                    color = if (isSel) Color.Black else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 4. Language & Voice Selector
        item {
            Text(text = "VOICE & LANGUAGE", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // Language Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                languages.forEach { l ->
                    val isSel = language == l
                    FilterChip(
                        selected = isSel,
                        onClick = { language = l },
                        label = { Text(l, fontSize = 11.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Voice Profiles
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VoiceGender.entries.forEach { v ->
                    val isSel = voice == v
                    Surface(
                        color = if (isSel) StudioCyan.copy(alpha = 0.2f) else SurfaceElevated,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) StudioCyan else SurfaceBorder),
                        modifier = Modifier.clickable { voice = v }
                    ) {
                        Text(
                            text = v.label,
                            color = if (isSel) StudioCyan else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 5. Visual Style Preset Selector
        item {
            Text(text = "VISUAL GENERATION STYLE", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            visualStyles.forEach { style ->
                val isSel = visualStyle == style
                Surface(
                    color = if (isSel) SurfaceElevated else SurfaceDark,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) StudioGold else SurfaceBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clickable { visualStyle = style }
                ) {
                    Text(
                        text = style,
                        color = if (isSel) TextPrimary else TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // 6. Prominent Generate Button
        item {
            Button(
                onClick = {
                    val finalTopic = if (topic.isNotBlank()) topic else "The Untold Secrets of ${selectedCategory.displayName}"
                    onGenerate(
                        finalTopic,
                        selectedCategory,
                        videoType,
                        durationSeconds,
                        language,
                        voice,
                        visualStyle,
                        aspectRatio,
                        quality,
                        audience
                    )
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudioRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("generate_video_submit_button")
            ) {
                Text(
                    text = "🚀 GENERATE VIDEO",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
