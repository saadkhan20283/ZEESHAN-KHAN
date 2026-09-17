package com.example.provider.video

import com.example.BuildConfig
import com.example.data.model.SceneData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

sealed class VideoJobState {
    data class Processing(val progress: Float, val statusMessage: String) : VideoJobState()
    data class Success(val videoUrl: String, val durationSeconds: Int) : VideoJobState()
    data class Error(val message: String) : VideoJobState()
}

data class VideoProviderConfig(
    val providerName: String = "veo", // "veo" or "openai"
    val apiKey: String = "",
    val model: String = "veo-3.1-fast-generate-preview",
    val baseUrl: String = "https://generativelanguage.googleapis.com"
)

interface VideoProvider {
    val isConfigured: Boolean
    val providerName: String
    suspend fun createVideo(scene: SceneData, aspectRatio: String): Result<String> // returns remote operation/job id
    suspend fun getVideoStatus(jobId: String): Result<VideoJobState>
    suspend fun downloadVideo(videoUrl: String, targetFile: File): Result<File>
    suspend fun cancelVideo(jobId: String): Result<Boolean>
}

/**
 * Official Google Veo Implementation:
 * Uses models/veo-3.1-fast-generate-preview or models/veo-3.1-generate-preview
 */
class VeoVideoProvider(
    private val config: VideoProviderConfig
) : VideoProvider {

    override val providerName: String = "Google Veo (${config.model})"

    override val isConfigured: Boolean
        get() = config.apiKey.isNotBlank() && config.apiKey != "MY_GEMINI_API_KEY"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    override suspend fun createVideo(scene: SceneData, aspectRatio: String): Result<String> = withContext(Dispatchers.IO) {
        if (!isConfigured) {
            return@withContext Result.failure(IllegalStateException("Video generation provider is not configured."))
        }

        val url = "${config.baseUrl}/v1beta/models/${config.model}:generateVideos?key=${config.apiKey}"
        val promptText = "${scene.visualPrompt}. Camera: ${scene.cameraMotion}. Motion: ${scene.subjectMotion}. Environment: ${scene.environmentMotion}. Continuous authentic movement, dynamic lighting."

        val jsonBody = JSONObject().apply {
            put("prompt", promptText)
            put("config", JSONObject().apply {
                put("numberOfVideos", 1)
                put("resolution", "1080p")
                put("aspectRatio", aspectRatio)
            })
        }

        val request = Request.Builder()
            .url(url)
            .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        try {
            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Veo API Error (${response.code}): $body"))
            }

            val json = JSONObject(body)
            val operationName = json.optString("name")
            if (operationName.isNotBlank()) {
                Result.success(operationName)
            } else {
                Result.failure(Exception("Invalid operation response: $body"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getVideoStatus(jobId: String): Result<VideoJobState> = withContext(Dispatchers.IO) {
        if (!isConfigured) {
            return@withContext Result.failure(IllegalStateException("Video generation provider is not configured."))
        }

        val url = "${config.baseUrl}/v1beta/$jobId?key=${config.apiKey}"
        val request = Request.Builder().url(url).get().build()

        try {
            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Veo Status Error (${response.code}): $body"))
            }

            val json = JSONObject(body)
            val done = json.optBoolean("done", false)
            if (!done) {
                val metadata = json.optJSONObject("metadata")
                val state = metadata?.optString("state") ?: "PROCESSING"
                return@withContext Result.success(VideoJobState.Processing(0.5f, "Veo rendering scene: $state"))
            }

            val error = json.optJSONObject("error")
            if (error != null) {
                return@withContext Result.success(VideoJobState.Error(error.optString("message", "Veo generation failed")))
            }

            val responseObj = json.optJSONObject("response")
            val generatedVideos = responseObj?.optJSONArray("generatedVideos")
            val firstVideo = generatedVideos?.optJSONObject(0)
            val videoUri = firstVideo?.optJSONObject("video")?.optString("uri")

            if (!videoUri.isNullOrBlank()) {
                Result.success(VideoJobState.Success(videoUri, 6))
            } else {
                Result.success(VideoJobState.Error("No video URI in response"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun downloadVideo(videoUrl: String, targetFile: File): Result<File> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(videoUrl).build()
        try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Download failed: ${response.code}"))
            }
            response.body?.byteStream()?.use { input ->
                targetFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            Result.success(targetFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cancelVideo(jobId: String): Result<Boolean> = withContext(Dispatchers.IO) {
        // Veo cancellation
        Result.success(true)
    }
}

/**
 * Official OpenAI Video Provider abstraction
 */
class OpenAIVideoProvider(
    private val config: VideoProviderConfig
) : VideoProvider {

    override val providerName: String = "OpenAI Video (${config.model})"

    override val isConfigured: Boolean
        get() = config.apiKey.isNotBlank()

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    override suspend fun createVideo(scene: SceneData, aspectRatio: String): Result<String> = withContext(Dispatchers.IO) {
        if (!isConfigured) {
            return@withContext Result.failure(IllegalStateException("Video generation provider is not configured."))
        }

        val url = "${config.baseUrl}/v1/videos"
        val jsonBody = JSONObject().apply {
            put("model", config.model)
            put("prompt", "${scene.visualPrompt}. Motion: ${scene.cameraMotion}, ${scene.subjectMotion}")
            put("size", if (aspectRatio == "9:16") "1080x1920" else "1920x1080")
            put("seconds", scene.durationSeconds)
        }

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer ${config.apiKey}")
            .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        try {
            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("OpenAI Video API Error: $body"))
            }
            val json = JSONObject(body)
            Result.success(json.getString("id"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getVideoStatus(jobId: String): Result<VideoJobState> = withContext(Dispatchers.IO) {
        if (!isConfigured) {
            return@withContext Result.failure(IllegalStateException("Video generation provider is not configured."))
        }
        val url = "${config.baseUrl}/v1/videos/$jobId"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer ${config.apiKey}")
            .get()
            .build()

        try {
            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("OpenAI Status Error: $body"))
            }
            val json = JSONObject(body)
            val status = json.optString("status")
            when (status) {
                "completed" -> {
                    val urlResult = json.optString("url")
                    Result.success(VideoJobState.Success(urlResult, 5))
                }
                "failed" -> Result.success(VideoJobState.Error(json.optString("error", "Video failed")))
                else -> Result.success(VideoJobState.Processing(0.5f, "Status: $status"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun downloadVideo(videoUrl: String, targetFile: File): Result<File> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(videoUrl).build()
        try {
            val response = client.newCall(request).execute()
            response.body?.byteStream()?.use { input ->
                targetFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            Result.success(targetFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cancelVideo(jobId: String): Result<Boolean> = withContext(Dispatchers.IO) {
        Result.success(true)
    }
}

object VideoProviderFactory {
    fun create(
        providerType: String = "veo",
        apiKeyOverride: String? = null,
        modelOverride: String? = null
    ): VideoProvider {
        val geminiKey = try {
            val k = BuildConfig.GEMINI_API_KEY
            if (k.isNotBlank() && k != "MY_GEMINI_API_KEY") k else ""
        } catch (e: Exception) { "" }

        val activeKey = apiKeyOverride ?: geminiKey

        return when (providerType.lowercase()) {
            "openai", "sora" -> OpenAIVideoProvider(
                VideoProviderConfig(
                    providerName = "openai",
                    apiKey = apiKeyOverride ?: "",
                    model = modelOverride ?: "sora",
                    baseUrl = "https://api.openai.com"
                )
            )
            else -> VeoVideoProvider(
                VideoProviderConfig(
                    providerName = "veo",
                    apiKey = activeKey,
                    model = modelOverride ?: "veo-3.1-fast-generate-preview",
                    baseUrl = "https://generativelanguage.googleapis.com"
                )
            )
        }
    }
}
