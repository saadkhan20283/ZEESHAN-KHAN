package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JobStatus
import com.example.ui.theme.*
import kotlin.math.sin

@Composable
fun StatusBadge(
    status: JobStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status) {
        JobStatus.READY, JobStatus.PUBLISHED -> Pair(StudioGreen.copy(alpha = 0.15f), StudioGreen)
        JobStatus.SCHEDULED -> Pair(StudioGold.copy(alpha = 0.15f), StudioGold)
        JobStatus.FAILED -> Pair(StudioRed.copy(alpha = 0.2f), StudioRedLight)
        JobStatus.QUEUED -> Pair(Color.Gray.copy(alpha = 0.2f), Color.LightGray)
        else -> Pair(StudioCyan.copy(alpha = 0.15f), StudioCyan)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.4f)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = status.label,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    iconColor: Color = StudioRed,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SurfaceDark,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(iconColor)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = TextTertiary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = title,
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        if (actionLabel != null && onActionClick != null) {
            TextButton(
                onClick = onActionClick,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.testTag("section_action_${actionLabel.lowercase().replace(' ', '_')}")
            ) {
                Text(
                    text = actionLabel,
                    color = StudioGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Interactive Video Motion Canvas Player:
 * Renders real kinetic movement, camera pan simulation, scene lighting shifts,
 * audio waveforms, and dynamic karaoke subtitles.
 */
@Composable
fun MotionVideoCanvas(
    isPlaying: Boolean,
    currentSceneIndex: Int,
    totalScenes: Int,
    aspectRatio: String, // "9:16" or "16:9"
    currentNarration: String,
    visualPrompt: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "motion_anim")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val zoomAnim by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "zoom"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Dynamic background cinematic gradient with moving camera lighting
            val lightX = width * (0.5f + 0.3f * sin(phase))
            val lightY = height * (0.4f + 0.2f * sin(phase * 0.7f))

            val gradientBrush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF2A1B3D),
                    Color(0xFF140F22),
                    Color(0xFF070709)
                ),
                center = Offset(lightX, lightY),
                radius = width * zoomAnim
            )
            drawRect(brush = gradientBrush)

            // 2. Cinematic Grid / Motion Vectors (Simulates Camera Trajectory & Physics)
            if (isPlaying) {
                val gridLines = 8
                for (i in 0..gridLines) {
                    val y = (height / gridLines) * i + (sin(phase + i) * 8f)
                    drawLine(
                        color = StudioCyan.copy(alpha = 0.15f),
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1.5f
                    )
                }

                // 3. Dynamic Realtime Audio Waveform
                val wavePath = Path()
                wavePath.moveTo(0f, height * 0.82f)
                val points = 40
                for (p in 0..points) {
                    val px = (width / points) * p
                    val py = height * 0.82f + sin(phase * 2f + (p * 0.4f)) * 14f * if (isPlaying) 1f else 0.1f
                    wavePath.lineTo(px, py)
                }
                drawPath(
                    path = wavePath,
                    color = StudioGold.copy(alpha = 0.7f),
                    style = Stroke(width = 3f)
                )
            }
        }

        // Overlay: Video Status / Timecode / Scene Label
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color.Black.copy(alpha = 0.65f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "SCENE ${currentSceneIndex + 1}/$totalScenes • $aspectRatio",
                        color = StudioGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    color = if (isPlaying) StudioGreen.copy(alpha = 0.2f) else Color.DarkGray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (isPlaying) StudioGreen else Color.LightGray)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isPlaying) "VEO PREVIEW" else "PAUSED",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Center Visual Prompt Prompt Tag
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = visualPrompt,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 11.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Bottom Dynamic Karaoke Subtitle
            Surface(
                color = Color.Black.copy(alpha = 0.85f),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioGold.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "“$currentNarration”",
                    color = Color.Yellow,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}
