package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY updatedAt DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getProjectById(id: String): ProjectEntity?

    @Query("SELECT * FROM projects WHERE id = :id")
    fun observeProjectById(id: String): Flow<ProjectEntity?>

    @Query("SELECT * FROM projects WHERE status IN ('QUEUED', 'RESEARCHING', 'SCRIPTING', 'GENERATING_VIDEO', 'GENERATING_VOICE', 'EDITING', 'THUMBNAIL', 'UPLOADING', 'PROCESSING') ORDER BY updatedAt DESC")
    fun getActiveQueue(): Flow<List<ProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Query("UPDATE projects SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateProjectStatus(id: String, status: String, updatedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProjectById(id: String)
}

@Dao
interface SceneDao {
    @Query("SELECT * FROM scenes WHERE projectId = :projectId ORDER BY sceneNumber ASC")
    fun getScenesForProject(projectId: String): Flow<List<SceneEntity>>

    @Query("SELECT * FROM scenes WHERE projectId = :projectId ORDER BY sceneNumber ASC")
    suspend fun getScenesList(projectId: String): List<SceneEntity>

    @Query("SELECT * FROM scenes WHERE id = :sceneId")
    suspend fun getSceneById(sceneId: String): SceneEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenes(scenes: List<SceneEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScene(scene: SceneEntity)

    @Update
    suspend fun updateScene(scene: SceneEntity)

    @Query("DELETE FROM scenes WHERE projectId = :projectId")
    suspend fun deleteScenesForProject(projectId: String)
}

@Dao
interface YoutubeAccountDao {
    @Query("SELECT * FROM youtube_accounts ORDER BY connectedAt DESC")
    fun getAllAccounts(): Flow<List<YoutubeAccountEntity>>

    @Query("SELECT * FROM youtube_accounts WHERE isConnected = 1 LIMIT 1")
    fun getPrimaryConnectedAccount(): Flow<YoutubeAccountEntity?>

    @Query("SELECT * FROM youtube_accounts WHERE id = :id")
    suspend fun getAccountById(id: String): YoutubeAccountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: YoutubeAccountEntity)

    @Query("UPDATE youtube_accounts SET isConnected = :connected WHERE id = :id")
    suspend fun setConnected(id: String, connected: Boolean)

    @Query("DELETE FROM youtube_accounts WHERE id = :id")
    suspend fun deleteAccount(id: String)
}

@Dao
interface ScheduledJobDao {
    @Query("SELECT * FROM scheduled_jobs ORDER BY targetTimeMillis ASC")
    fun getAllScheduledJobs(): Flow<List<ScheduledJobEntity>>

    @Query("SELECT * FROM scheduled_jobs WHERE status = 'SCHEDULED' ORDER BY targetTimeMillis ASC")
    fun getUpcomingJobs(): Flow<List<ScheduledJobEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScheduledJob(job: ScheduledJobEntity)

    @Update
    suspend fun updateScheduledJob(job: ScheduledJobEntity)

    @Query("UPDATE scheduled_jobs SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)

    @Query("DELETE FROM scheduled_jobs WHERE id = :id")
    suspend fun deleteScheduledJob(id: String)
}

@Dao
interface TrendDao {
    @Query("SELECT * FROM trend_items ORDER BY detectedTime DESC")
    fun getAllTrends(): Flow<List<TrendItemEntity>>

    @Query("SELECT * FROM trend_items WHERE category = :category ORDER BY detectedTime DESC")
    fun getTrendsByCategory(category: String): Flow<List<TrendItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrends(trends: List<TrendItemEntity>)

    @Query("UPDATE trend_items SET isSaved = :isSaved WHERE id = :id")
    suspend fun setSaved(id: String, isSaved: Boolean)

    @Query("DELETE FROM trend_items WHERE id = :id")
    suspend fun deleteTrend(id: String)
}

@Dao
interface AutomationSettingsDao {
    @Query("SELECT * FROM automation_settings WHERE id = 1")
    fun getSettings(): Flow<AutomationSettingsEntity?>

    @Query("SELECT * FROM automation_settings WHERE id = 1")
    suspend fun getSettingsDirect(): AutomationSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: AutomationSettingsEntity)
}
