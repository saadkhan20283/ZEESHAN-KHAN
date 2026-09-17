package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.JobStatus
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AutomationViewModel
import com.example.ui.viewmodel.StudioScreen

class MainActivity : ComponentActivity() {

    private val viewModel: AutomationViewModel by viewModels()

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        intent.data?.let { uri ->
            viewModel.handleOAuthRedirectUri(uri)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        intent?.data?.let { uri ->
            viewModel.handleOAuthRedirectUri(uri)
        }

        setContent {
            MyApplicationTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val projects by viewModel.allProjects.collectAsStateWithLifecycle()
                val activeQueue by viewModel.activeQueue.collectAsStateWithLifecycle()
                val connectedAccount by viewModel.connectedAccount.collectAsStateWithLifecycle()
                val allAccounts by viewModel.allAccounts.collectAsStateWithLifecycle()
                val scheduledJobs by viewModel.scheduledJobs.collectAsStateWithLifecycle()
                val automationSettings by viewModel.automationSettings.collectAsStateWithLifecycle()

                var showMoreMenu by remember { mutableStateOf(false) }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(StudioRed),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("▶", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = stringResource(R.string.app_name),
                                            color = TextPrimary,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = uiState.currentScreen.title,
                                            color = StudioGold,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            },
                            actions = {
                                // Channel Connection Indicator
                                Surface(
                                    color = if (connectedAccount?.isConnected == true) StudioGreen.copy(alpha = 0.15f) else StudioRed.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .clickable { viewModel.navigateTo(StudioScreen.YOUTUBE_CHANNEL) }
                                        .testTag("top_bar_channel_status")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(if (connectedAccount?.isConnected == true) StudioGreen else StudioRed)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (connectedAccount?.isConnected == true) "Channel Connected" else "Connect YT",
                                            color = if (connectedAccount?.isConnected == true) StudioGreen else StudioRedLight,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                // Queue Counter Badge
                                if (activeQueue.isNotEmpty()) {
                                    Surface(
                                        color = StudioCyan.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.clickable { viewModel.navigateTo(StudioScreen.QUEUE) }
                                    ) {
                                        Text(
                                            text = "⚡ ${activeQueue.size}",
                                            color = StudioCyan,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                // Quick Menu
                                IconButton(onClick = { showMoreMenu = !showMoreMenu }) {
                                    Icon(Icons.Default.MoreVert, contentDescription = "Studio Menu", tint = TextPrimary)
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = SurfaceDark
                            )
                        )
                    },
                    bottomBar = {
                        Column(modifier = Modifier.background(SurfaceDark)) {
                            // Secondary Navigation Bar Scrollable Strip
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(
                                    StudioScreen.DASHBOARD to "Dashboard",
                                    StudioScreen.TREND_FINDER to "🔥 Trends",
                                    StudioScreen.CREATE_VIDEO to "+ Create",
                                    StudioScreen.ONE_CLICK to "⚡ One-Click",
                                    StudioScreen.AUTO_PILOT to "🤖 Pilot",
                                    StudioScreen.QUEUE to "Queue (${activeQueue.size})",
                                    StudioScreen.VIDEO_PREVIEW to "▶ Preview",
                                    StudioScreen.YOUTUBE_CHANNEL to "Channel",
                                    StudioScreen.SCHEDULER to "Schedule",
                                    StudioScreen.SEO_STUDIO to "SEO",
                                    StudioScreen.ANALYTICS to "Stats",
                                    StudioScreen.SETTINGS to "APIs"
                                ).forEach { (screen, label) ->
                                    val isSel = uiState.currentScreen == screen
                                    Surface(
                                        color = if (isSel) StudioRed else SurfaceElevated,
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .clickable { viewModel.navigateTo(screen) }
                                            .testTag("nav_tab_${screen.name.lowercase()}")
                                    ) {
                                        Text(
                                            text = label,
                                            color = if (isSel) Color.White else TextSecondary,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(BackgroundDark)
                    ) {
                        // Global Banner Alert Notice
                        uiState.bannerNotice?.let { msg ->
                            Surface(
                                color = SurfaceElevated,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, StudioGold.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                                    .align(Alignment.TopCenter)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = msg,
                                        color = TextPrimary,
                                        fontSize = 12.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { viewModel.dismissBanner() },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = TextSecondary)
                                    }
                                }
                            }
                        }

                        // Main Screen Routing
                        when (uiState.currentScreen) {
                            StudioScreen.DASHBOARD -> DashboardScreen(
                                account = connectedAccount,
                                projects = projects,
                                activeQueue = activeQueue,
                                onNavigate = { screen, projId -> viewModel.navigateTo(screen, projId) },
                                onStartPreset = { preset -> viewModel.startOneClickGeneration(preset) }
                            )

                            StudioScreen.TREND_FINDER -> TrendFinderScreen(
                                trends = uiState.trends,
                                isLoading = uiState.isSearchingTrends,
                                selectedCategory = uiState.trendFilterCategory,
                                selectedCountry = uiState.trendFilterCountry,
                                selectedTimeRange = uiState.trendFilterTimeRange,
                                onFilterChange = { c, cat, t -> viewModel.setTrendFilters(c, cat, t) },
                                onSearch = { viewModel.searchTrends(it) },
                                onUseTopic = { topic, cat ->
                                    viewModel.navigateTo(StudioScreen.CREATE_VIDEO)
                                },
                                onGenerateDirect = { topic, cat ->
                                    viewModel.startOneClickGeneration(customTopic = topic)
                                }
                            )

                            StudioScreen.CREATE_VIDEO -> CreateVideoScreen(
                                initialTopic = uiState.selectedProject?.topic ?: "",
                                initialCategory = uiState.trendFilterCategory,
                                onGenerate = { topic, cat, vType, dur, lang, voice, style, ratio, qual, aud ->
                                    viewModel.createCustomVideo(topic, cat, vType, dur, lang, voice, style, ratio, qual, aud)
                                },
                                onSuggestTrends = { viewModel.navigateTo(StudioScreen.TREND_FINDER) }
                            )

                            StudioScreen.ONE_CLICK -> OneClickScreen(
                                onTriggerOneClick = { preset ->
                                    viewModel.startOneClickGeneration(preset = preset)
                                }
                            )

                            StudioScreen.AUTO_PILOT -> AutopilotScreen(
                                settings = automationSettings,
                                onToggleAutopilot = { viewModel.toggleAutopilot(it) },
                                onSaveSettings = { cat, vpd, slot, reqApp ->
                                    viewModel.updateAutopilotSettings(cat, vpd, slot, reqApp)
                                }
                            )

                            StudioScreen.PROJECTS -> ProjectsScreen(
                                projects = projects,
                                onSelectProject = { id ->
                                    viewModel.navigateTo(StudioScreen.VIDEO_PREVIEW, id)
                                },
                                onDeleteProject = { id ->
                                    viewModel.deleteProject(id)
                                },
                                onDownloadVideo = { id ->
                                    viewModel.downloadProjectVideo(id)
                                },
                                onDownloadThumbnail = { id ->
                                    viewModel.downloadProjectThumbnail(id)
                                }
                            )

                            StudioScreen.QUEUE -> QueueScreen(
                                activeQueue = activeQueue,
                                onSelectProject = { id -> viewModel.navigateTo(StudioScreen.VIDEO_PREVIEW, id) },
                                onPause = { viewModel.pauseJob(it) },
                                onResume = { viewModel.resumeJob(it) },
                                onCancel = { viewModel.cancelJob(it) },
                                onDelete = { viewModel.deleteProject(it) }
                            )

                            StudioScreen.VIDEO_PREVIEW -> VideoPreviewScreen(
                                project = uiState.selectedProject ?: projects.firstOrNull(),
                                scenes = uiState.selectedProjectScenes,
                                onRetryScene = { viewModel.retryScene(it) },
                                onApproveAndSchedule = { viewModel.approveAndSchedule(it) },
                                onUploadNow = { viewModel.publishNow(it) },
                                onEditSeo = { viewModel.navigateTo(StudioScreen.SEO_STUDIO) },
                                onDownloadVideo = { id -> viewModel.downloadProjectVideo(id) },
                                onDownloadThumbnail = { id -> viewModel.downloadProjectThumbnail(id) }
                            )

                            StudioScreen.YOUTUBE_CHANNEL -> YouTubeChannelScreen(
                                currentAccount = connectedAccount,
                                allAccounts = allAccounts,
                                onConnectOAuth = {
                                    viewModel.connectYouTube { authUri ->
                                        try {
                                            val browserIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, authUri)
                                            startActivity(browserIntent)
                                        } catch (e: Exception) {
                                            // Handled
                                        }
                                    }
                                },
                                onDisconnect = { viewModel.disconnectYouTube(it) }
                            )

                            StudioScreen.SCHEDULER -> SchedulerScreen(
                                scheduledJobs = scheduledJobs,
                                onDeleteJob = { viewModel.deleteScheduledJob(it) }
                            )

                            StudioScreen.SEO_STUDIO -> SeoStudioScreen(
                                project = uiState.selectedProject ?: projects.firstOrNull(),
                                onSaveSeo = { t, d, tags ->
                                    val current = uiState.selectedProject ?: projects.firstOrNull() ?: return@SeoStudioScreen
                                    viewModel.updateProjectSeo(current.id, t, d, tags)
                                }
                            )

                            StudioScreen.ANALYTICS -> AnalyticsScreen(
                                analyticsData = uiState.analyticsData,
                                selectedPeriod = uiState.analyticsPeriod,
                                onPeriodChange = { viewModel.refreshAnalytics(it) }
                            )

                            StudioScreen.SETTINGS -> SettingsScreen(
                                onTestProviders = {}
                            )
                        }
                    }
                }
            }
        }
    }
}
