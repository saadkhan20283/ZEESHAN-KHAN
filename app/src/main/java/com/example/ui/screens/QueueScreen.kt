package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.data.model.JobStatus
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun QueueScreen(
    activeQueue: List<ProjectEntity>,
    onSelectProject: (String) -> Unit,
    onPause: (String) -> Unit,
    onResume: (String) -> Unit,
    onCancel: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Video Generation Queue",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Real-time background worker pipeline",
            color = TextSecondary,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (activeQueue.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No active generation jobs in queue.\nCreate a video or trigger Autopilot to start rendering.",
                    color = TextTertiary,
                    fontSize = 13.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                items(activeQueue) { job ->
                    val statusEnum = try { JobStatus.valueOf(job.status) } catch (e: Exception) { JobStatus.QUEUED }
                    Surface(
                        color = SurfaceDark,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("queue_job_${job.id}")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Job ID: ${job.id.takeLast(8)}",
                                    color = TextTertiary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                StatusBadge(status = statusEnum)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = job.title,
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${job.category} • ${job.durationSeconds}s • ${job.aspectRatio} • Quality: ${job.quality}",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Stage progress bar
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Stage: ${statusEnum.label}",
                                        color = StudioCyan,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "${(statusEnum.progress * 100).toInt()}%",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                LinearProgressIndicator(
                                    progress = { statusEnum.progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = StudioRed,
                                    trackColor = SurfaceElevated
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Controls Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onSelectProject(job.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = StudioGold),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Preview", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                if (statusEnum == JobStatus.QUEUED) {
                                    Button(
                                        onClick = { onResume(job.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = StudioGreen),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Start", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else if (!statusEnum.isTerminal) {
                                    OutlinedButton(
                                        onClick = { onPause(job.id) },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Pause", fontSize = 11.sp)
                                    }
                                }

                                OutlinedButton(
                                    onClick = { onCancel(job.id) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioRedLight),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Cancel", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
