package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.local.ProjectEntity
import com.example.data.model.TitleOption
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import org.json.JSONArray

@Composable
fun SeoStudioScreen(
    project: ProjectEntity?,
    onSaveSeo: (selectedTitle: String, description: String, tags: String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (project == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundDark),
            contentAlignment = Alignment.Center
        ) {
            Text("Select a project first to optimize Titles & SEO", color = TextSecondary)
        }
        return
    }

    var selectedTitle by remember(project) { mutableStateOf(project.selectedTitle.ifBlank { project.title }) }
    var description by remember(project) { mutableStateOf(project.description) }
    var tags by remember(project) { mutableStateOf(project.tagsJson) }

    // Parse titles options
    val titleOptions = remember(project.titleOptionsJson) {
        val list = mutableListOf<TitleOption>()
        try {
            if (project.titleOptionsJson.isNotBlank()) {
                val array = JSONArray(project.titleOptionsJson)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        TitleOption(
                            title = obj.getString("title"),
                            type = obj.getString("type"),
                            score = obj.optInt("score", 95)
                        )
                    )
                }
            }
        } catch (e: Exception) {}
        if (list.isEmpty()) {
            listOf(
                TitleOption("The Hidden Paradox of ${project.topic}", "Curiosity", 96),
                TitleOption("${project.topic} Explained in 60 Seconds", "Search-friendly", 92),
                TitleOption("Inside the World's Rarest ${project.topic}", "Documentary", 95),
                TitleOption("How One Decision Revolutionized ${project.topic}", "Storytelling", 94),
                TitleOption("The Dark Truth Behind ${project.topic} ⚡", "Shorts", 98)
            )
        } else list
    }

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
                text = "Thumbnails, Titles & SEO Studio",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Algorithm optimization & YouTube search CTR engine",
                color = TextSecondary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 1. Thumbnail Concept Card
        item {
            SectionHeader(title = "Thumbnail Concept & Visual Identity")
            Surface(
                color = SurfaceDark,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        if (project.thumbnailUrl.isNotBlank()) {
                            AsyncImage(
                                model = project.thumbnailUrl,
                                contentDescription = "Generated Thumbnail",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text("16:9 8K Master Thumbnail", color = StudioGold, fontWeight = FontWeight.Bold)
                        }

                        // Title Text Overlay Preview
                        Surface(
                            color = Color.Black.copy(alpha = 0.75f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = selectedTitle.take(35).uppercase(),
                                color = Color.Yellow,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Prompt: ${project.thumbnailPrompt.ifBlank { "High-contrast YouTube 16:9 thumbnail with dramatic lighting." }}",
                        color = TextTertiary,
                        fontSize = 11.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 2. 5 Distinct Title Options
        item {
            SectionHeader(title = "5 Algorithm-Optimized Titles (Tap to Apply)")
        }

        items(titleOptions.size) { i ->
            val opt = titleOptions[i]
            val isSel = opt.title == selectedTitle
            Surface(
                color = if (isSel) SurfaceElevated else SurfaceDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) StudioGold else SurfaceBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { selectedTitle = opt.title }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(14.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = StudioGold.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = opt.type.uppercase(),
                                    color = StudioGold,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "CTR Score: ${opt.score}/100",
                                color = StudioGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = opt.title,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (isSel) {
                        Surface(
                            color = StudioGold,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "SELECTED",
                                color = Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // 3. Description Editor
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(title = "SEO Description (With Timestamps & CTAs)")
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                minLines = 6,
                maxLines = 10,
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
                    .testTag("seo_description_input")
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 4. Tags
        item {
            SectionHeader(title = "Tags & Hashtags")
            OutlinedTextField(
                value = tags,
                onValueChange = { tags = it },
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
                    .testTag("seo_tags_input")
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 5. Save Button
        item {
            Button(
                onClick = { onSaveSeo(selectedTitle, description, tags) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudioRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_seo_button")
            ) {
                Text("Save SEO & Title Configuration", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
