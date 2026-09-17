package com.example.data.model

data class SceneData(
    val sceneId: String,
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

data class ContinuityProfile(
    val visualDescription: String,
    val appearance: String,
    val clothing: String,
    val colors: String,
    val cameraStyle: String,
    val environment: String,
    val lighting: String,
    val importantObjectDetails: String
)

data class TitleOption(
    val title: String,
    val type: String, // "Curiosity", "Search-friendly", "Documentary", "Storytelling", "Shorts"
    val score: Int = 95
)

data class ResearchPlan(
    val topic: String,
    val originalAngle: String,
    val verifiedFacts: List<String>,
    val academicInferences: List<String>,
    val speculativeAngles: List<String>,
    val fictionLabels: List<String>,
    val sourcesConsulted: List<String>
)

data class ScriptResult(
    val hook: String,
    val body: String,
    val callToAction: String,
    val fullNarration: String,
    val estimatedWordCount: Int,
    val targetDurationSeconds: Int
)

data class ThumbnailConcept(
    val visualPrompt: String,
    val headlineText: String,
    val emotionalColorTheme: String,
    val compositionStyle: String,
    val generatedImageUrl: String? = null
)

data class TrendTopic(
    val id: String,
    val topic: String,
    val source: String, // "YouTube Data API", "Google Trends", "Reddit", "X", "TikTok", "Instagram"
    val country: String,
    val category: VideoCategory,
    val detectedTime: String,
    val trendSignal: String,
    val relatedKeywords: List<String>,
    val signalScore: String, // e.g. "YouTube Popularity Index: Top 10"
    val methodologyExplanation: String
)

data class QuickPreset(
    val id: String,
    val title: String,
    val description: String,
    val category: VideoCategory,
    val videoType: VideoType,
    val durationSeconds: Int,
    val aspectRatio: AspectRatio,
    val visualStyle: String,
    val voice: VoiceGender,
    val captionStyle: CaptionStyle,
    val musicCategory: MusicCategory
) {
    companion object {
        val ALL_PRESETS = listOf(
            QuickPreset(
                id = "preset_trending_short",
                title = "TRENDING SHORT",
                description = "High-retention 30s vertical video optimized for YouTube Shorts algorithm with dynamic captions",
                category = VideoCategory.SHORTS_REELS_TIKTOK,
                videoType = VideoType.SHORT,
                durationSeconds = 30,
                aspectRatio = AspectRatio.RATIO_9_16,
                visualStyle = "Punchy hyper-realistic visuals, cinematic studio lighting, high framerate motion",
                voice = VoiceGender.ENERGETIC,
                captionStyle = CaptionStyle.DYNAMIC,
                musicCategory = MusicCategory.SPORTS
            ),
            QuickPreset(
                id = "preset_viral_story",
                title = "VIRAL STORY",
                description = "Compelling emotional narrative hook with rising tension and surprising climax",
                category = VideoCategory.STORYTELLING,
                videoType = VideoType.LONG_FORM,
                durationSeconds = 120,
                aspectRatio = AspectRatio.RATIO_16_9,
                visualStyle = "Cinematic 35mm film grain, moody warm tones, emotive character closeups",
                voice = VoiceGender.FEMALE_WARM,
                captionStyle = CaptionStyle.NORMAL,
                musicCategory = MusicCategory.CINEMATIC
            ),
            QuickPreset(
                id = "preset_documentary",
                title = "DOCUMENTARY",
                description = "Rigorous historical or scientific breakdown with archival fidelity and verified facts",
                category = VideoCategory.EDUCATIONAL_DOCS,
                videoType = VideoType.LONG_FORM,
                durationSeconds = 300,
                aspectRatio = AspectRatio.RATIO_16_9,
                visualStyle = "Photorealistic macro zooms, panoramic drone vistas, museum lighting",
                voice = VoiceGender.FEMALE_DOCUMENTARY,
                captionStyle = CaptionStyle.NORMAL,
                musicCategory = MusicCategory.DOCUMENTARY
            ),
            QuickPreset(
                id = "preset_mystery",
                title = "MYSTERY & CRIME",
                description = "Atmospheric enigma with chilling low-key lighting and clue-by-clue narrative reveals",
                category = VideoCategory.MYSTERY_CRIME,
                videoType = VideoType.LONG_FORM,
                durationSeconds = 180,
                aspectRatio = AspectRatio.RATIO_16_9,
                visualStyle = "Noir shadows, wet asphalt reflections, cold blue tint, eerie spotlight",
                voice = VoiceGender.MALE_DEEP,
                captionStyle = CaptionStyle.HIGHLIGHTED,
                musicCategory = MusicCategory.HORROR
            ),
            QuickPreset(
                id = "preset_cinematic_luxury",
                title = "CINEMATIC LUXURY",
                description = "Exquisite hypercars, luxury horology, and architectural masterpieces",
                category = VideoCategory.CARS_LUXURY,
                videoType = VideoType.LONG_FORM,
                durationSeconds = 120,
                aspectRatio = AspectRatio.RATIO_16_9,
                visualStyle = "Golden hour reflections, carbon-fiber macro passes, ultra-crisp reflections",
                voice = VoiceGender.MALE_NATURAL,
                captionStyle = CaptionStyle.DYNAMIC,
                musicCategory = MusicCategory.CORPORATE
            )
        )
    }
}
