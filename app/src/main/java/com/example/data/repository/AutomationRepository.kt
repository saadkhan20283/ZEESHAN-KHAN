package com.example.data.repository

import android.content.Context
import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class AutomationRepository(
    private val database: AppDatabase,
    private val context: Context
) {
    private val projectDao = database.projectDao()
    private val sceneDao = database.sceneDao()
    private val youtubeAccountDao = database.youtubeAccountDao()
    private val scheduledJobDao = database.scheduledJobDao()
    private val trendDao = database.trendDao()
    private val automationSettingsDao = database.automationSettingsDao()

    val allProjects: Flow<List<ProjectEntity>> = projectDao.getAllProjects()
    val activeQueue: Flow<List<ProjectEntity>> = projectDao.getActiveQueue()
    val connectedAccount: Flow<YoutubeAccountEntity?> = youtubeAccountDao.getPrimaryConnectedAccount()
    val allConnectedAccounts: Flow<List<YoutubeAccountEntity>> = youtubeAccountDao.getAllAccounts()
    val allScheduledJobs: Flow<List<ScheduledJobEntity>> = scheduledJobDao.getAllScheduledJobs()
    val upcomingJobs: Flow<List<ScheduledJobEntity>> = scheduledJobDao.getUpcomingJobs()
    val automationSettings: Flow<AutomationSettingsEntity?> = automationSettingsDao.getSettings()

    fun observeProject(id: String): Flow<ProjectEntity?> = projectDao.observeProjectById(id)
    fun observeScenes(projectId: String): Flow<List<SceneEntity>> = sceneDao.getScenesForProject(projectId)

    suspend fun getProject(id: String): ProjectEntity? = projectDao.getProjectById(id)
    suspend fun getProjectById(id: String): ProjectEntity? = projectDao.getProjectById(id)
    suspend fun getScenes(projectId: String): List<SceneEntity> = sceneDao.getScenesList(projectId)
    suspend fun getAutomationSettingsDirect(): AutomationSettingsEntity {
        return automationSettingsDao.getSettingsDirect() ?: AutomationSettingsEntity()
    }

    suspend fun saveProject(project: ProjectEntity) {
        projectDao.insertProject(project)
    }

    suspend fun updateProjectStatus(id: String, status: JobStatus) {
        projectDao.updateProjectStatus(id, status.name)
    }

    suspend fun saveScenes(scenes: List<SceneEntity>) {
        sceneDao.insertScenes(scenes)
    }

    suspend fun updateScene(scene: SceneEntity) {
        sceneDao.updateScene(scene)
    }

    suspend fun deleteProject(id: String) {
        sceneDao.deleteScenesForProject(id)
        projectDao.deleteProjectById(id)
    }

    suspend fun saveYoutubeAccount(account: YoutubeAccountEntity) {
        youtubeAccountDao.insertAccount(account)
    }

    suspend fun disconnectYoutubeAccount(id: String) {
        youtubeAccountDao.setConnected(id, false)
    }

    suspend fun deleteYoutubeAccount(id: String) {
        youtubeAccountDao.deleteAccount(id)
    }

    suspend fun saveScheduledJob(job: ScheduledJobEntity) {
        scheduledJobDao.insertScheduledJob(job)
    }

    suspend fun updateScheduledJobStatus(id: String, status: String) {
        scheduledJobDao.updateStatus(id, status)
    }

    suspend fun deleteScheduledJob(id: String) {
        scheduledJobDao.deleteScheduledJob(id)
    }

    suspend fun updateAutomationSettings(settings: AutomationSettingsEntity) {
        automationSettingsDao.insertSettings(settings)
    }

    suspend fun saveTrends(trends: List<TrendItemEntity>) {
        trendDao.insertTrends(trends)
    }

    suspend fun setTrendSaved(id: String, isSaved: Boolean) {
        trendDao.setSaved(id, isSaved)
    }

    fun getAllTrends(): Flow<List<TrendItemEntity>> = trendDao.getAllTrends()
    fun getTrendsByCategory(category: String): Flow<List<TrendItemEntity>> = trendDao.getTrendsByCategory(category)
}
