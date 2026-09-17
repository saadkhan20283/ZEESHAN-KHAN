package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ProjectEntity
import com.example.data.local.SceneEntity
import com.example.data.model.JobStatus
import com.example.ui.components.MotionVideoCanvas
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun VideoPreviewScreen(
    project: ProjectEntity?,
    scenes: List<SceneEntity>,
    onRetryScene: (String) -> Unit,
    onApproveAndSchedule: (String) -> Unit,
    onUploadNow: (String) -> Unit,
    onEditSeo: () -> Unit,
    onDownloadVideo: (String) -> Unit = {},
    onDownloadThumbnail: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (project == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundDark),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No project selected. Choose a project from Dashboard or Queue.", color = TextSecondary)
        }
        return
    }

    var isPlaying by remember { mutableStateOf(true) }
    var activeSceneIndex by remember { mutableIntStateOf(0) }
    val statusEnum = try { JobStatus.valueOf(project.status) } catch (e: Exception) { JobStatus.READY }

    // Automatic playback scrubber progression
    LaunchedEffect(isPlaying, scenes.size) {
        while (isPlaying && scenes.isNotEmpty()) {
            delay(3500)
            activeSceneIndex = (activeSceneIndex + 1) % scenes.size
        }
    }

    val currentScene = scenes.getOrNull(activeSceneIndex)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 40.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.title,
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${project.category} • ${project.durationSeconds}s • ${project.aspectRatio}",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
                StatusBadge(status = statusEnum)
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 1. Interactive Motion Canvas Player
        item {
            val isShort = project.aspectRatio == "9:16"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isShort) 420.dp else 220.dp),
                contentAlignment = Alignment.Center
            ) {
                MotionVideoCanvas(
                    isPlaying = isPlaying,
                    currentSceneIndex = activeSceneIndex,
                    totalScenes = scenes.size.coerceAtLeast(1),
                    aspectRatio = project.aspectRatio,
                    currentNarration = currentScene?.narration ?: "Visual scene rendering in progress...",
                    visualPrompt = currentScene?.visualPrompt ?: "Cinematic prompt",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Player Controls Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { isPlaying = !isPlaying },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isPlaying) StudioGold else StudioGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (isPlaying) "⏸ Pause" else "▶ Play Preview",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                OutlinedButton(
                    onClick = onEditSeo,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("✏️ Titles & SEO", fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // 2. Metadata Specs Card
        item {
            Surface(
                color = SurfaceDark,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "RENDER SPECIFICATIONS", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Resolution:", color = TextTertiary, fontSize = 12.sp)
                        Text(text = if (project.aspectRatio == "9:16") "1080x1920 (9:16 Shorts)" else "1920x1080 (16:9)", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Bitrate / Container:", color = TextTertiary, fontSize = 12.sp)
                        Text(text = "18.2 Mbps H.264 / AAC", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Publish Privacy:", color = TextTertiary, fontSize = 12.sp)
                        Text(text = project.privacy, color = StudioGold, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Publish Slot:", color = TextTertiary, fontSize = 12.sp)
                        Text(text = "${project.scheduledTime} (UTC)", color = StudioCyan, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 3. Primary Publishing Actions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onApproveAndSchedule(project.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioGold),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("action_approve_schedule")
                ) {
                    Text("📅 Schedule at ${project.scheduledTime}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Button(
                    onClick = { onUploadNow(project.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("action_upload_now")
                ) {
                    Text("🚀 Upload Now", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Download MP4 and Thumbnail from server.js endpoints
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onDownloadVideo(project.id) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioCyan),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("action_download_mp4")
                ) {
                    Text("📥 Download MP4", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = { onDownloadThumbnail(project.id) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioGold),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioGold.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("action_download_thumb")
                ) {
                    Text("🖼️ Download Thumbnail", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 4. Scene Breakdown with Timestamps
        item {
            SectionHeader(title = "Scene Plan Breakdown (${scenes.size} Scenes)")
        }

        itemsIndexed(scenes) { index, sc ->
            val isCurrent = index == activeSceneIndex
            Surface(
                color = if (isCurrent) SurfaceElevated else SurfaceDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isCurrent) StudioGold else SurfaceBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { activeSceneIndex = index }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Scene #${sc.sceneNumber} (${sc.durationSeconds}s)",
                            color = if (isCurrent) StudioGold else TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (sc.error != null) {
                            TextButton(
                                onClick = { onRetryScene(sc.id) },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("🔄 Retry Scene", color = StudioRedLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Text(text = "✅ Ready", color = StudioGreen, fontSize = 11.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Narration: “${sc.narration}”",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Camera: ${sc.cameraMotion}",
                        color = StudioCyan.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
