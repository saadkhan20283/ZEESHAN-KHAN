package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.ProjectEntity
import com.example.data.local.YoutubeAccountEntity
import com.example.data.model.JobStatus
import com.example.data.model.QuickPreset
import com.example.ui.components.MetricCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.StudioScreen

@Composable
fun DashboardScreen(
    account: YoutubeAccountEntity?,
    projects: List<ProjectEntity>,
    activeQueue: List<ProjectEntity>,
    onNavigate: (StudioScreen, String?) -> Unit,
    onStartPreset: (QuickPreset) -> Unit,
    modifier: Modifier = Modifier
) {
    val pendingCount = projects.count { it.status == JobStatus.QUEUED.name || it.status == JobStatus.READY.name }
    val scheduledCount = projects.count { it.status == JobStatus.SCHEDULED.name }
    val publishedCount = projects.count { it.status == JobStatus.PUBLISHED.name }
    val failedCount = projects.count { it.status == JobStatus.FAILED.name }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // 1. Channel Connection Card
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(StudioScreen.YOUTUBE_CHANNEL, null) }
                    .testTag("dashboard_channel_card")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(StudioRed.copy(alpha = 0.2f))
                            .border(1.dp, StudioRed, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (account?.channelImage?.isNotBlank() == true) {
                            AsyncImage(
                                model = account.channelImage,
                                contentDescription = "Channel Avatar",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = "YT",
                                color = StudioRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = account?.channelName ?: "Connect YouTube Channel",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (account?.isConnected == true) StudioGreen else StudioRed)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (account?.isConnected == true) "${account.subscriberCount} • ${account.videoCount}" else "Official OAuth2 connection required for publishing",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Surface(
                        color = StudioRed.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (account?.isConnected == true) "Active" else "Connect",
                            color = StudioRedLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // 2. Primary KPI Grid
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Pending Review",
                    value = "$pendingCount",
                    subtitle = "Awaiting approval",
                    iconColor = StudioGold,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Scheduled",
                    value = "$scheduledCount",
                    subtitle = "At 08:00 PM slot",
                    iconColor = StudioCyan,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Published Live",
                    value = "$publishedCount",
                    subtitle = "On YouTube",
                    iconColor = StudioGreen,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Failed / Alert",
                    value = "$failedCount",
                    subtitle = "Needs review",
                    iconColor = StudioRed,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 3. Quick Action Buttons
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(title = "Quick Actions")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onNavigate(StudioScreen.CREATE_VIDEO, null) },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_create_video")
                ) {
                    Text("+ Create", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Button(
                    onClick = { onNavigate(StudioScreen.TREND_FINDER, null) },
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevated),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_find_trends")
                ) {
                    Text("🔥 Trends", color = StudioGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Button(
                    onClick = { onNavigate(StudioScreen.AUTO_PILOT, null) },
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevated),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_autopilot")
                ) {
                    Text("🤖 Auto Pilot", color = StudioCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        // 4. Quick Content Mode Presets
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "One-Click Content Presets",
                actionLabel = "Instant Mode",
                onActionClick = { onNavigate(StudioScreen.ONE_CLICK, null) }
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(QuickPreset.ALL_PRESETS) { preset ->
                    Surface(
                        color = SurfaceDark,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                        modifier = Modifier
                            .width(220.dp)
                            .clickable { onStartPreset(preset) }
                            .testTag("preset_${preset.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Surface(
                                color = StudioGold.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = preset.aspectRatio.label,
                                    color = StudioGoldLight,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = preset.title,
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = preset.description,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { onStartPreset(preset) },
                                colors = ButtonDefaults.buttonColors(containerColor = StudioRed.copy(alpha = 0.9f)),
                                contentPadding = PaddingValues(vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("⚡ Generate & Queue", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 5. Active Queue Section
        if (activeQueue.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(
                    title = "Live AI Generation Queue",
                    actionLabel = "View All (${activeQueue.size})",
                    onActionClick = { onNavigate(StudioScreen.QUEUE, null) }
                )
            }
            items(activeQueue.take(3)) { job ->
                val statusEnum = JobStatus.valueOf(job.status)
                Surface(
                    color = SurfaceDark,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onNavigate(StudioScreen.VIDEO_PREVIEW, job.id) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = job.title,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { statusEnum.progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = StudioRed,
                                trackColor = SurfaceBorder
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        StatusBadge(status = statusEnum)
                    }
                }
            }
        }

        // 6. Recent Projects List
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "Recent Content Projects",
                actionLabel = "See All",
                onActionClick = { onNavigate(StudioScreen.PROJECTS, null) }
            )
        }

        if (projects.isEmpty()) {
            item {
                Text(
                    text = "No projects yet. Tap '+ Create Video' to start your first YouTube video.",
                    color = TextTertiary,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        } else {
            items(projects.take(5)) { proj ->
                val statusEnum = try { JobStatus.valueOf(proj.status) } catch (e: Exception) { JobStatus.QUEUED }
                Surface(
                    color = SurfaceDark,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onNavigate(StudioScreen.VIDEO_PREVIEW, proj.id) }
                        .testTag("project_item_${proj.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        // Thumbnail Placeholder / Image
                        Box(
                            modifier = Modifier
                                .size(width = 72.dp, height = 48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            if (proj.thumbnailUrl.isNotBlank()) {
                                AsyncImage(
                                    model = proj.thumbnailUrl,
                                    contentDescription = "Thumbnail",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Text("16:9", color = TextTertiary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = proj.title,
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${proj.category} • ${proj.durationSeconds}s • ${proj.aspectRatio}",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                        StatusBadge(status = statusEnum)
                    }
                }
            }
        }
    }
}
