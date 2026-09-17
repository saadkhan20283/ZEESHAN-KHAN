package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.ProjectEntity
import com.example.data.model.JobStatus
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun ProjectsScreen(
    projects: List<ProjectEntity>,
    onSelectProject: (String) -> Unit,
    onDeleteProject: (String) -> Unit,
    onDownloadVideo: (String) -> Unit = {},
    onDownloadThumbnail: (String) -> Unit = {},
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
                text = "Content Projects Library",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "All generated, scheduled, and published YouTube videos",
                color = TextSecondary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (projects.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No projects yet. Create your first video using the '+ Create' action.",
                        color = TextTertiary,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            items(projects) { proj ->
                val statusEnum = try { JobStatus.valueOf(proj.status) } catch (e: Exception) { JobStatus.QUEUED }
                Surface(
                    color = SurfaceDark,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onSelectProject(proj.id) }
                        .testTag("project_row_${proj.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 80.dp, height = 54.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            if (proj.thumbnailUrl.isNotBlank()) {
                                AsyncImage(
                                    model = proj.thumbnailUrl,
                                    contentDescription = "Thumb",
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
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                StatusBadge(status = statusEnum)
                                TextButton(
                                    onClick = { onDownloadVideo(proj.id) },
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                                ) {
                                    Text("📥 MP4", color = StudioCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                TextButton(
                                    onClick = { onDownloadThumbnail(proj.id) },
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                                ) {
                                    Text("🖼️ Thumb", color = StudioGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        TextButton(
                            onClick = { onDeleteProject(proj.id) },
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            Text("Delete", color = StudioRedLight, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
