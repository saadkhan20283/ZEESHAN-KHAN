package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
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
import com.example.data.local.AutomationSettingsEntity
import com.example.data.model.VideoCategory
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*

@Composable
fun AutopilotScreen(
    settings: AutomationSettingsEntity?,
    onToggleAutopilot: (Boolean) -> Unit,
    onSaveSettings: (category: VideoCategory, videosPerDay: Int, uploadTime: String, requireApproval: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isRunning = settings?.isActive ?: false
    var selectedCategory by remember(settings) {
        mutableStateOf(VideoCategory.fromString(settings?.category ?: "Cars & Luxury"))
    }
    var videosPerDay by remember(settings) { mutableIntStateOf(settings?.videosPerDay ?: 1) }
    var uploadTime by remember(settings) { mutableStateOf(settings?.uploadTime ?: "08:00 PM") }
    var requireApproval by remember(settings) { mutableStateOf(settings?.approvalRequired ?: true) }

    val workflowSteps = listOf(
        "1. Trend Discovery" to "Pulls verified breakout topics from YouTube Data API v3",
        "2. Original Research" to "Conducts factual verification and unique angle deduction",
        "3. Scriptwriting" to "Drafts high-retention narration with 3-second hook",
        "4. Scene Breakdown" to "Structures camera motion, subject kinematics & lighting",
        "5. Veo Video Generation" to "Generates photorealistic moving video scenes via Google Veo",
        "6. Voiceover & Audio" to "Synthesizes ultra-natural narration and licensed score",
        "7. Dynamic Subtitles" to "Burns in karaoke styled captions and syncs SFX",
        "8. Thumbnail & 5 Titles" to "Builds high-CTR graphics and tags",
        "9. Safety Review" to "Stops at READY state for human sign-off if enabled",
        "10. YouTube Publishing" to "Uploads video, thumbnail and publishes at $uploadTime"
    )

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
                text = "Autonomous AI Pilot",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Hands-free YouTube channel growth engine",
                color = TextSecondary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Master Switch Card
        item {
            Surface(
                color = if (isRunning) StudioGreen.copy(alpha = 0.12f) else SurfaceElevated,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, if (isRunning) StudioGreen else SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (isRunning) StudioGreen else Color.Gray)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isRunning) "AUTOPILOT IS ACTIVE" else "AUTOPILOT IS PAUSED",
                                color = if (isRunning) StudioGreen else TextSecondary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isRunning) "Running daily schedule: Producing $videosPerDay video(s)/day at $uploadTime" else "Toggle on to automate daily YouTube generation",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Switch(
                        checked = isRunning,
                        onCheckedChange = { onToggleAutopilot(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = StudioGreen,
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = SurfaceBorder
                        ),
                        modifier = Modifier.testTag("autopilot_master_toggle")
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Settings Section
        item {
            SectionHeader(title = "Autopilot Schedule & Settings")

            // Require Approval Toggle (Default ON!)
            Surface(
                color = SurfaceDark,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Require Approval (Recommended)",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Video halts at 'READY' state for creator review before upload",
                            color = TextTertiary,
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = requireApproval,
                        onCheckedChange = {
                            requireApproval = it
                            onSaveSettings(selectedCategory, videosPerDay, uploadTime, it)
                        },
                        colors = SwitchDefaults.colors(checkedTrackColor = StudioGold),
                        modifier = Modifier.testTag("autopilot_approval_toggle")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Videos Per Day
            Surface(
                color = SurfaceDark,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "VIDEOS PER DAY: $videosPerDay", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(1, 2, 3, 5).forEach { num ->
                            val isSel = videosPerDay == num
                            Surface(
                                color = if (isSel) StudioRed else SurfaceElevated,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        videosPerDay = num
                                        onSaveSettings(selectedCategory, num, uploadTime, requireApproval)
                                    }
                            ) {
                                Text(
                                    text = "$num / day",
                                    color = if (isSel) Color.White else TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Upload Time
            Surface(
                color = SurfaceDark,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "PUBLISHING TIME SLOT (DEFAULT: 08:00 PM)", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("12:00 PM", "04:00 PM", "06:00 PM", "08:00 PM").forEach { slot ->
                            val isSel = uploadTime == slot
                            Surface(
                                color = if (isSel) StudioGold else SurfaceElevated,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        uploadTime = slot
                                        onSaveSettings(selectedCategory, videosPerDay, slot, requireApproval)
                                    }
                            ) {
                                Text(
                                    text = slot,
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

            Spacer(modifier = Modifier.height(12.dp))

            // Category Selection
            Surface(
                color = SurfaceDark,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "AUTONOMOUS NICHE / CATEGORY", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        VideoCategory.entries.take(10).forEach { cat ->
                            val isSel = cat == selectedCategory
                            Surface(
                                color = if (isSel) StudioRed else SurfaceElevated,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.clickable {
                                    selectedCategory = cat
                                    onSaveSettings(cat, videosPerDay, uploadTime, requireApproval)
                                }
                            ) {
                                Text(
                                    text = cat.displayName,
                                    color = if (isSel) Color.White else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Daily Workflow Timeline
        item {
            SectionHeader(title = "Autonomous Daily Pipeline")
        }

        items(workflowSteps.size) { i ->
            val (stepTitle, stepDesc) = workflowSteps[i]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(StudioRed.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "${i + 1}", color = StudioRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = stepTitle, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(text = stepDesc, color = TextSecondary, fontSize = 11.sp)
                }
            }
        }
    }
}
