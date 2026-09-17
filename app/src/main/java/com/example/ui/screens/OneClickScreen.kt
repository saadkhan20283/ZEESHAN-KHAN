package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuickPreset
import com.example.ui.theme.*

@Composable
fun OneClickScreen(
    onTriggerOneClick: (QuickPreset) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPreset by remember { mutableStateOf(QuickPreset.ALL_PRESETS.first()) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = StudioGold.copy(alpha = 0.15f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "AUTONOMOUS ONE-CLICK PIPELINE",
                    color = StudioGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "One Tap. Full YouTube Video.",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Autonomous 10-step content engine: Top Trend Discovery → Original Research → Script → Veo Video → Voiceover → Music → Captions → Thumbnail → Title → Scheduled at 08:00 PM.",
                color = TextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Giant Prominent Action Button
        item {
            Button(
                onClick = { onTriggerOneClick(selectedPreset) },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudioRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .testTag("one_click_create_and_schedule_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🚀 CREATE & SCHEDULE",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Preset Selector
        item {
            Text(
                text = "SELECT CONTENT PRESET FOR INSTANT ENGINE",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(QuickPreset.ALL_PRESETS.size) { index ->
            val preset = QuickPreset.ALL_PRESETS[index]
            val isSelected = preset.id == selectedPreset.id
            Surface(
                color = if (isSelected) SurfaceElevated else SurfaceDark,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) StudioRed else SurfaceBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { selectedPreset = preset }
                    .testTag("one_click_preset_${preset.id}")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .border(2.dp, if (isSelected) StudioRed else Color.Gray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(StudioRed)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = preset.title,
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = StudioGold.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = preset.aspectRatio.value,
                                    color = StudioGold,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = preset.description,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
