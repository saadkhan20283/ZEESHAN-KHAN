package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import com.example.data.local.TrendItemEntity
import com.example.data.model.VideoCategory
import com.example.ui.theme.*

@Composable
fun TrendFinderScreen(
    trends: List<TrendItemEntity>,
    isLoading: Boolean,
    selectedCategory: VideoCategory,
    selectedCountry: String,
    selectedTimeRange: String,
    onFilterChange: (String?, VideoCategory?, String?) -> Unit,
    onSearch: (String) -> Unit,
    onUseTopic: (String, VideoCategory) -> Unit,
    onGenerateDirect: (String, VideoCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    val countries = listOf("US", "GB", "CA", "IN", "PK", "DE", "JP")
    val timeRanges = listOf("Past 24h", "Past 7d", "Past 30d")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Trend Discovery & Signal Engine",
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Sourced from YouTube Data API v3 & global trend signals",
            color = TextSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                if (it.length > 2) onSearch(it)
            },
            placeholder = { Text("Search trending topics, keywords, niches...", color = TextTertiary, fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = StudioGold) },
            singleLine = true,
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
                .testTag("trend_search_field")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Country & Time Range Filter Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            countries.forEach { c ->
                FilterChip(
                    selected = selectedCountry == c,
                    onClick = { onFilterChange(c, null, null) },
                    label = { Text(c, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StudioRed.copy(alpha = 0.2f),
                        selectedLabelColor = StudioRedLight
                    )
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            timeRanges.forEach { t ->
                FilterChip(
                    selected = selectedTimeRange == t,
                    onClick = { onFilterChange(null, null, t) },
                    label = { Text(t, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StudioGold.copy(alpha = 0.2f),
                        selectedLabelColor = StudioGoldLight
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 22 Categories Horizontal Scroll Selector
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
                        .clickable { onFilterChange(null, cat, null) }
                        .testTag("category_chip_${cat.name.lowercase()}")
                ) {
                    Text(
                        text = cat.displayName,
                        color = if (isSelected) Color.White else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = StudioRed)
            }
        } else if (trends.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No trend data returned for this criteria.\nTrend provider reporting status: Ready.",
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
                items(trends) { item ->
                    val catEnum = VideoCategory.fromString(item.category)
                    Surface(
                        color = SurfaceDark,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("trend_item_${item.id}")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Surface(
                                    color = StudioRed.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = item.source,
                                        color = StudioRedLight,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Text(
                                    text = item.detectedTime,
                                    color = TextTertiary,
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = item.topic,
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Signal: ${item.trendSignal}",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Keywords: ${item.relatedKeywordsJson}",
                                color = StudioCyan.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Methodology disclosure
                            Surface(
                                color = SurfaceElevated,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "📊 ${item.methodologyExplanation}",
                                    color = TextTertiary,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onUseTopic(item.topic, catEnum) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Use Topic", fontSize = 12.sp)
                                }

                                Button(
                                    onClick = { onGenerateDirect(item.topic, catEnum) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = StudioRed),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("⚡ Generate Video", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
