package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProjectEntity::class,
        SceneEntity::class,
        YoutubeAccountEntity::class,
        ScheduledJobEntity::class,
        TrendItemEntity::class,
        AutomationSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun sceneDao(): SceneDao
    abstract fun youtubeAccountDao(): YoutubeAccountDao
    abstract fun scheduledJobDao(): ScheduledJobDao
    abstract fun trendDao(): TrendDao
    abstract fun automationSettingsDao(): AutomationSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "youtube_automation_studio.db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
