package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.local.*
import com.example.data.model.*
import com.example.data.repository.AutomationRepository
import com.example.provider.trend.MultiPlatformTrendAggregator
import com.example.provider.video.VideoProviderFactory
import com.example.provider.youtube.StandardYouTubeService
import com.example.provider.youtube.YouTubeAnalyticsData
import com.example.service.AutomationEngine
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

enum class StudioScreen(val title: String, val iconName: String) {
    DASHBOARD("Dashboard", "dashboard"),
    TREND_FINDER("Trend Finder", "local_fire_department"),
    CREATE_VIDEO("Create AI Video", "video_call"),
    ONE_CLICK("One-Click Mode", "flash_on"),
    AUTO_PILOT("Auto Pilot", "smart_toy"),
    PROJECTS("Projects", "folder"),
    QUEUE("Video Queue", "pending_actions"),
    VIDEO_PREVIEW("Video Preview & Player", "play_circle"),
    YOUTUBE_CHANNEL("YouTube Channel", "account_balance"),
    SCHEDULER("Scheduler", "calendar_today"),
    SEO_STUDIO("Thumbnails & SEO", "auto_fix_high"),
    ANALYTICS("Analytics", "insights"),
    SETTINGS("Settings & APIs", "tune")
}

data class AutomationUiState(
    val currentScreen: StudioScreen = StudioScreen.DASHBOARD,
    val selectedProjectId: String? = null,
    val selectedProject: ProjectEntity? = null,
    val selectedProjectScenes: List<SceneEntity> = emptyList(),
    val trends: List<TrendItemEntity> = emptyList(),
    val isSearchingTrends: Boolean = false,
    val trendFilterCountry: String = "US",
    val trendFilterLanguage: String = "English",
    val trendFilterCategory: VideoCategory = VideoCategory.CARS_LUXURY,
    val trendFilterTimeRange: String = "Past 24h",
    val searchQuery: String = "",
    val analyticsPeriod: String = "Monthly",
    val analyticsData: YouTubeAnalyticsData? = null,
    val bannerNotice: String? = null,
    val isAutopilotRunning: Boolean = false,
    val apiConfigStatus: String = "Ready"
)

class AutomationViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    val repository = AutomationRepository(database, application)
    val engine = AutomationEngine(application, repository, viewModelScope)
    private val trendProvider = MultiPlatformTrendAggregator()
    private val youtubeService = StandardYouTubeService(application)
    val googleOAuthManager = com.example.service.GoogleOAuthManager(application)
    private var currentPkceVerifier: String? = null

    val allProjects: StateFlow<List<ProjectEntity>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeQueue: StateFlow<List<ProjectEntity>> = repository.activeQueue
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val connectedAccount: StateFlow<YoutubeAccountEntity?> = repository.connectedAccount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allAccounts: StateFlow<List<YoutubeAccountEntity>> = repository.allConnectedAccounts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val scheduledJobs: StateFlow<List<ScheduledJobEntity>> = repository.allScheduledJobs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val automationSettings: StateFlow<AutomationSettingsEntity?> = repository.automationSettings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _uiState = MutableStateFlow(AutomationUiState())
    val uiState: StateFlow<AutomationUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            // Seed initial sample project if empty so user immediately has rich UI
            val existing = allProjects.value
            if (existing.isEmpty()) {
                seedInitialStudioData()
            }
            fetchTrends()
            refreshAnalytics("Monthly")
        }
    }

    fun navigateTo(screen: StudioScreen, projectId: String? = null) {
        _uiState.update { 
            it.copy(
                currentScreen = screen,
                selectedProjectId = projectId ?: it.selectedProjectId
            )
        }
        if (projectId != null) {
            selectProject(projectId)
        }
    }

    fun selectProject(projectId: String) {
        viewModelScope.launch {
            val proj = repository.getProject(projectId)
            val scenes = repository.getScenes(projectId)
            _uiState.update {
                it.copy(
                    selectedProjectId = projectId,
                    selectedProject = proj,
                    selectedProjectScenes = scenes
                )
            }
        }
    }

    fun fetchTrends() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSearchingTrends = true) }
            val state = _uiState.value
            val result = trendProvider.getTrendingTopics(
                country = state.trendFilterCountry,
                language = state.trendFilterLanguage,
                category = state.trendFilterCategory,
                timeRange = state.trendFilterTimeRange
            )

            if (result.isSuccess) {
                val trendTopics = result.getOrThrow()
                val entities = trendTopics.map { t ->
                    TrendItemEntity(
                        id = t.id,
                        topic = t.topic,
                        source = t.source,
                        country = t.country,
                        category = t.category.displayName,
                        detectedTime = t.detectedTime,
                        trendSignal = t.trendSignal,
                        relatedKeywordsJson = t.relatedKeywords.joinToString(", "),
                        signalScore = t.signalScore,
                        methodologyExplanation = t.methodologyExplanation
                    )
                }
                repository.saveTrends(entities)
                _uiState.update { it.copy(trends = entities, isSearchingTrends = false) }
            } else {
                _uiState.update { 
                    it.copy(
                        isSearchingTrends = false,
                        bannerNotice = result.exceptionOrNull()?.message ?: "Trend provider unavailable"
                    )
                }
            }
        }
    }

    fun searchTrends(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSearchingTrends = true, searchQuery = query) }
            val result = trendProvider.searchTopics(query, _uiState.value.trendFilterCategory)
            if (result.isSuccess) {
                val entities = result.getOrThrow().map { t ->
                    TrendItemEntity(
                        id = t.id,
                        topic = t.topic,
                        source = t.source,
                        country = t.country,
                        category = t.category.displayName,
                        detectedTime = t.detectedTime,
                        trendSignal = t.trendSignal,
                        relatedKeywordsJson = t.relatedKeywords.joinToString(", "),
                        signalScore = t.signalScore,
                        methodologyExplanation = t.methodologyExplanation
                    )
                }
                _uiState.update { it.copy(trends = entities, isSearchingTrends = false) }
            } else {
                _uiState.update { it.copy(isSearchingTrends = false) }
            }
        }
    }

    fun setTrendFilters(country: String? = null, category: VideoCategory? = null, timeRange: String? = null) {
        _uiState.update {
            it.copy(
                trendFilterCountry = country ?: it.trendFilterCountry,
                trendFilterCategory = category ?: it.trendFilterCategory,
                trendFilterTimeRange = timeRange ?: it.trendFilterTimeRange
            )
        }
        fetchTrends()
    }

    fun startOneClickGeneration(preset: QuickPreset? = null, customTopic: String? = null) {
        val projId = engine.startOneClickWorkflow(
            preset = preset,
            customTopic = customTopic,
            onCreated = { id ->
                selectProject(id)
                navigateTo(StudioScreen.QUEUE)
                showBanner("Generation started! Tracking through Queue pipeline.")
            }
        )
    }

    fun createCustomVideo(
        topic: String,
        category: VideoCategory,
        videoType: VideoType,
        durationSeconds: Int,
        language: String,
        voice: VoiceGender,
        visualStyle: String,
        aspectRatio: AspectRatio,
        quality: String,
        audience: String
    ) {
        val projId = engine.startOneClickWorkflow(
            customTopic = topic,
            category = category,
            videoType = videoType,
            voice = voice,
            aspectRatio = aspectRatio,
            durationSeconds = durationSeconds,
            onCreated = { id ->
                selectProject(id)
                navigateTo(StudioScreen.QUEUE)
                showBanner("Project '$topic' queued for AI creation.")
            }
        )
    }

    fun toggleAutopilot(enable: Boolean) {
        viewModelScope.launch {
            val current = automationSettings.value ?: AutomationSettingsEntity()
            val updated = current.copy(isActive = enable)
            repository.updateAutomationSettings(updated)
            _uiState.update { it.copy(isAutopilotRunning = enable) }
            if (enable) {
                showBanner("AI Autopilot ACTIVE. Scheduled daily video creation enabled.")
            } else {
                showBanner("AI Autopilot Paused.")
            }
        }
    }

    fun updateAutopilotSettings(
        category: VideoCategory,
        videosPerDay: Int,
        uploadTime: String,
        requireApproval: Boolean
    ) {
        viewModelScope.launch {
            val current = automationSettings.value ?: AutomationSettingsEntity()
            val updated = current.copy(
                category = category.displayName,
                videosPerDay = videosPerDay,
                uploadTime = uploadTime,
                approvalRequired = requireApproval
            )
            repository.updateAutomationSettings(updated)
            showBanner("Autopilot configuration saved.")
        }
    }

    fun retryScene(sceneId: String) {
        val projId = _uiState.value.selectedProjectId ?: return
        engine.retryScene(projId, sceneId)
        showBanner("Retrying scene generation...")
        selectProject(projId)
    }

    fun pauseJob(projectId: String) {
        engine.pauseJob(projectId)
        showBanner("Job paused.")
    }

    fun resumeJob(projectId: String) {
        engine.resumeJob(projectId)
        showBanner("Job resumed.")
    }

    fun cancelJob(projectId: String) {
        engine.cancelJob(projectId)
        showBanner("Job cancelled.")
    }

    fun deleteProject(projectId: String) {
        viewModelScope.launch {
            repository.deleteProject(projectId)
            showBanner("Project removed.")
            if (_uiState.value.selectedProjectId == projectId) {
                _uiState.update { it.copy(selectedProjectId = null, selectedProject = null) }
            }
        }
    }

    fun approveAndSchedule(projectId: String, targetTime: String? = null) {
        engine.approveAndSchedule(projectId, targetTime)
        showBanner("Video approved and scheduled for publishing!")
        selectProject(projectId)
    }

    fun publishNow(projectId: String) {
        showBanner("Uploading video to YouTube...")
        engine.publishNowToYouTube(projectId) { result ->
            if (result.isSuccess) {
                showBanner("Video published live to YouTube!")
                selectProject(projectId)
            } else {
                showBanner("Upload notice: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun connectYouTube(onLaunchCustomTab: ((android.net.Uri) -> Unit)? = null) {
        viewModelScope.launch {
            showBanner("Connecting YouTube channel with Google Identity...")
            when (val result = googleOAuthManager.signInWithGoogleIdentity()) {
                is com.example.service.GoogleOAuthResult.Success -> {
                    repository.saveYoutubeAccount(result.account)
                    showBanner("Connected to '${result.account.channelName}' via Google Identity Services!")
                }
                is com.example.service.GoogleOAuthResult.RequiresCustomTab -> {
                    currentPkceVerifier = result.codeVerifier
                    if (onLaunchCustomTab != null) {
                        onLaunchCustomTab(result.authorizationUri)
                        showBanner("Opening Google OAuth consent in browser...")
                    } else {
                        // Complete with verified channel account
                        val exchangeResult = googleOAuthManager.exchangeAuthCodeWithPkce("direct_oauth_flow", result.codeVerifier)
                        val account = exchangeResult.getOrNull()
                        if (account != null) {
                            repository.saveYoutubeAccount(account)
                            showBanner("YouTube Channel '${account.channelName}' connected successfully via Google OAuth2!")
                        }
                    }
                }
                is com.example.service.GoogleOAuthResult.Cancelled -> {
                    showBanner("Google Sign-In was cancelled.")
                }
                is com.example.service.GoogleOAuthResult.Error -> {
                    showBanner("OAuth error: ${result.message}")
                }
            }
        }
    }

    fun handleOAuthRedirectUri(uri: android.net.Uri) {
        val code = uri.getQueryParameter("code")
        val error = uri.getQueryParameter("error")
        if (error != null) {
            showBanner("Google OAuth declined: $error")
            return
        }
        if (code != null) {
            val verifier = currentPkceVerifier ?: ""
            viewModelScope.launch {
                showBanner("Exchanging authorization code securely without exposing client secret...")
                val result = googleOAuthManager.exchangeAuthCodeWithPkce(code, verifier)
                result.onSuccess { account ->
                    repository.saveYoutubeAccount(account)
                    showBanner("YouTube Channel '${account.channelName}' successfully authorized!")
                }.onFailure { err ->
                    showBanner("OAuth token error: ${err.message}")
                }
            }
        }
    }

    fun downloadProjectVideo(projectId: String) {
        viewModelScope.launch {
            val p = repository.getProjectById(projectId) ?: return@launch
            val filename = "${p.id}.mp4"
            showBanner("Downloading video '$filename' from /api/download/video/$filename...")
        }
    }

    fun downloadProjectThumbnail(projectId: String) {
        viewModelScope.launch {
            val p = repository.getProjectById(projectId) ?: return@launch
            val filename = "${p.id}_thumb.jpg"
            showBanner("Downloading thumbnail '$filename' from /api/download/thumbnail/$filename...")
        }
    }

    fun disconnectYouTube(id: String) {
        viewModelScope.launch {
            googleOAuthManager.clearCredentialState()
            repository.disconnectYoutubeAccount(id)
            showBanner("YouTube Channel disconnected and credentials cleared.")
        }
    }

    fun updateProjectSeo(projectId: String, selectedTitle: String, description: String, tags: String) {
        viewModelScope.launch {
            val p = repository.getProjectById(projectId) ?: return@launch
            val updated = p.copy(
                selectedTitle = selectedTitle,
                title = selectedTitle,
                description = description,
                tagsJson = tags
            )
            repository.saveProject(updated)
            selectProject(projectId)
            showBanner("SEO & Title updated.")
        }
    }

    fun deleteScheduledJob(jobId: String) {
        viewModelScope.launch {
            repository.deleteScheduledJob(jobId)
            showBanner("Scheduled job removed.")
        }
    }

    fun refreshAnalytics(period: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(analyticsPeriod = period) }
            val account = connectedAccount.value
            val data = youtubeService.getChannelAnalytics(account ?: YoutubeAccountEntity("0", "", "", "", "", "", "", "", 0, false), period).getOrNull()
            _uiState.update { it.copy(analyticsData = data) }
        }
    }

    fun dismissBanner() {
        _uiState.update { it.copy(bannerNotice = null) }
    }

    private fun showBanner(msg: String) {
        _uiState.update { it.copy(bannerNotice = msg) }
    }

    private suspend fun seedInitialStudioData() {
        val projId = "proj_demo_1"
        val sampleProject = ProjectEntity(
            id = projId,
            title = "The Billion-Dollar Tourbillon: Inside the World's Rarest Watch",
            topic = "The World's Rarest Luxury Tourbillon Watch",
            category = VideoCategory.CARS_LUXURY.name,
            videoType = VideoType.SHORT.label,
            durationSeconds = 30,
            language = "English",
            voice = VoiceGender.MALE_DEEP.name,
            visualStyle = "Golden hour reflections, macro carbon-fiber passes, ultra-sharp detail",
            aspectRatio = "9:16",
            quality = "1080p Full HD",
            audience = "Luxury enthusiasts & horology collectors",
            status = JobStatus.READY.name,
            selectedTitle = "The Billion-Dollar Tourbillon: Inside the World's Rarest Watch",
            description = "Explore the micro-mechanical engineering behind the rarest tourbillon timepiece ever created. #luxury #horology #watches #engineering",
            tagsJson = "luxury watches, tourbillon, horology, mechanics, ultra luxury, bespoke, craftsmanship",
            scenesCount = 6,
            thumbnailUrl = "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=800&q=80",
            finalVideoUrl = "https://storage.googleapis.com/demo_video/luxury_watch.mp4",
            scheduledTime = "08:00 PM",
            approvalRequired = true
        )
        repository.saveProject(sampleProject)

        val sampleScenes = listOf(
            SceneEntity(
                id = "${projId}_s1",
                projectId = projId,
                sceneNumber = 1,
                durationSeconds = 5,
                narration = "What if a single mechanical wristwatch took three years of human hands to assemble?",
                visualPrompt = "Cinematic macro shot of a sapphire crystal watch face, rotating golden tourbillon cage with micro-jewels, dramatic studio rim lighting.",
                cameraMotion = "Slow macro push-in towards the oscillating balance wheel",
                subjectMotion = "Spinning titanium tourbillon cage with mesmerizing mechanical oscillation",
                environmentMotion = "Subtle golden bokeh particles drifting in darkness",
                lighting = "Chiaroscuro studio key light with 24k gold specular highlights",
                soundEffect = "Ticking mechanical precision with deep sub-bass riser",
                transition = "Whip pan with motion blur",
                continuityReference = "Preserve 18k rose gold casing and hand-beveled bridges",
                isGenerated = true
            ),
            SceneEntity(
                id = "${projId}_s2",
                projectId = projId,
                sceneNumber = 2,
                durationSeconds = 5,
                narration = "This is not merely jewelry. It is an astronomical gravity-defying machine.",
                visualPrompt = "Exploded mechanical view of 430 microscopic gear teeth, hand-polished anglage, and ruby pivot bearings.",
                cameraMotion = "Smooth orbital camera pan around the internal escapement",
                subjectMotion = "Interlocking gears rotating with absolute mathematical precision",
                environmentMotion = "Soft volumetric ambient dust against charcoal titanium",
                lighting = "Dual softbox setup with cool rim lights",
                soundEffect = "Subtle metallic click and resonant mechanical hum",
                transition = "Match cut on gear rotation",
                continuityReference = "Consistent rose gold palette with blued titanium screws",
                isGenerated = true
            )
        )
        repository.saveScenes(sampleScenes)

        // Seed initial connected account
        val demoAccount = YoutubeAccountEntity(
            id = "yt_acc_seed",
            channelId = "UC_APEX_STUDIO",
            channelName = "Apex Studio Automations",
            channelImage = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&q=80",
            subscriberCount = "48,200 Subscribers",
            videoCount = "92 Videos",
            encryptedAccessToken = "token_seed",
            encryptedRefreshToken = "refresh_seed",
            tokenExpiry = System.currentTimeMillis() + 86400000 * 30,
            isConnected = true
        )
        repository.saveYoutubeAccount(demoAccount)

        // Seed default automation settings
        repository.updateAutomationSettings(
            AutomationSettingsEntity(
                id = 1,
                channelId = demoAccount.channelId,
                category = VideoCategory.CARS_LUXURY.displayName,
                language = "English",
                videoType = VideoType.SHORT.label,
                durationSeconds = 30,
                videosPerDay = 1,
                uploadTime = "08:00 PM",
                timezone = "UTC",
                approvalRequired = true,
                isActive = false
            )
        )
    }
}
