package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val title: String,
    val topic: String,
    val category: String,
    val videoType: String,
    val durationSeconds: Int,
    val language: String,
    val voice: String,
    val visualStyle: String,
    val aspectRatio: String,
    val quality: String,
    val audience: String,
    val researchPlanJson: String = "",
    val scriptJson: String = "",
    val scenesCount: Int = 0,
    val thumbnailPrompt: String = "",
    val thumbnailUrl: String = "",
    val titleOptionsJson: String = "",
    val selectedTitle: String = "",
    val description: String = "",
    val tagsJson: String = "",
    val status: String,
    val finalVideoUrl: String = "",
    val youtubeAccountId: String? = null,
    val scheduledTime: String = "08:00 PM",
    val privacy: String = "Public",
    val approvalRequired: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "scenes")
data class SceneEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val sceneNumber: Int,
    val durationSeconds: Int,
    val narration: String,
    val visualPrompt: String,
    val cameraMotion: String,
    val subjectMotion: String,
    val environmentMotion: String,
    val lighting: String,
    val soundEffect: String,
    val transition: String,
    val continuityReference: String,
    val videoClipUrl: String? = null,
    val isGenerated: Boolean = false,
    val error: String? = null
)

@Entity(tableName = "youtube_accounts")
data class YoutubeAccountEntity(
    @PrimaryKey val id: String,
    val channelId: String,
    val channelName: String,
    val channelImage: String,
    val subscriberCount: String,
    val videoCount: String,
    val encryptedAccessToken: String,
    val encryptedRefreshToken: String,
    val tokenExpiry: Long,
    val isConnected: Boolean,
    val connectedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "scheduled_jobs")
data class ScheduledJobEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val projectTitle: String,
    val targetTimeMillis: Long,
    val uploadTimeDisplay: String, // e.g. "08:00 PM"
    val timezone: String,
    val channelId: String,
    val channelName: String,
    val privacy: String,
    val approvalRequired: Boolean,
    val status: String,
    val errorMessage: String? = null,
    val retryCount: Int = 0,
    val youtubeVideoId: String? = null,
    val publishedUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "trend_items")
data class TrendItemEntity(
    @PrimaryKey val id: String,
    val topic: String,
    val source: String,
    val country: String,
    val category: String,
    val detectedTime: String,
    val trendSignal: String,
    val relatedKeywordsJson: String,
    val signalScore: String,
    val methodologyExplanation: String,
    val isSaved: Boolean = false
)

@Entity(tableName = "automation_settings")
data class AutomationSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val channelId: String = "",
    val category: String = "Cars & Luxury",
    val language: String = "English",
    val videoType: String = "Short (Vertical 9:16)",
    val durationSeconds: Int = 30,
    val videosPerDay: Int = 1,
    val uploadTime: String = "08:00 PM",
    val timezone: String = "UTC",
    val approvalRequired: Boolean = true,
    val trendSources: String = "YouTube Data API, Google Trends",
    val isActive: Boolean = false,
    val lastRunTime: Long = 0L,
    val nextScheduledRunTime: Long = 0L
)
