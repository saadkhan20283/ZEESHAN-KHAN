package com.example.data.model

enum class JobStatus(val label: String, val progress: Float, val isTerminal: Boolean = false) {
    QUEUED("Queued", 0.05f),
    RESEARCHING("Researching Facts & Topic", 0.15f),
    SCRIPTING("Writing Original Script", 0.30f),
    GENERATING_VIDEO("Generating AI Video Scenes", 0.50f),
    GENERATING_VOICE("Synthesizing Voiceover", 0.65f),
    EDITING("Rendering Video & Captions", 0.78f),
    THUMBNAIL("Generating Thumbnail & SEO", 0.88f),
    READY("Ready for Review", 1.0f),
    SCHEDULED("Scheduled for Publishing", 1.0f),
    UPLOADING("Uploading to YouTube", 0.92f),
    PROCESSING("YouTube Processing", 0.97f),
    PUBLISHED("Published Live", 1.0f, isTerminal = true),
    FAILED("Job Failed", 0f, isTerminal = true)
}

enum class VideoType(val label: String, val defaultAspectRatio: String) {
    SHORT("Short (Vertical 9:16)", "9:16"),
    LONG_FORM("Long-form (Landscape 16:9)", "16:9"),
    CUSTOM("Custom", "1:1")
}

enum class AspectRatio(val label: String, val value: String) {
    RATIO_9_16("9:16 (Shorts/Reels)", "9:16"),
    RATIO_16_9("16:9 (Standard YouTube)", "16:9"),
    RATIO_1_1("1:1 (Square)", "1:1")
}

enum class PublishingPrivacy(val label: String) {
    PRIVATE("Private"),
    UNLISTED("Unlisted"),
    PUBLIC("Public")
}

enum class VoiceGender(val label: String, val modelVoice: String) {
    MALE_DEEP("Male - Deep & Resonant", "Fenrir"),
    MALE_NATURAL("Male - Natural Conversational", "Puck"),
    FEMALE_WARM("Female - Warm & Engaging", "Kore"),
    FEMALE_DOCUMENTARY("Female - Clear Documentary", "Aoede"),
    ENERGETIC("Narrator - Energetic & Fast", "Charon"),
    CALM("Narrator - Calm & Soothing", "Leda"),
    CHILD_FRIENDLY("Narrator - Cheerful & Friendly", "Zephyr")
}

enum class CaptionStyle(val label: String, val description: String) {
    DYNAMIC("Dynamic Word-by-Word", "Active highlighted karaoke bounce for Shorts retention"),
    NORMAL("Classic Subtitles", "Clean lower-third subtitle bar with background outline"),
    HIGHLIGHTED("Yellow Box Highlight", "High contrast black box with golden keywords")
}

enum class MusicCategory(val label: String, val mood: String) {
    CINEMATIC("Cinematic", "Epic orchestral swells, cinematic sub-bass hits"),
    DOCUMENTARY("Documentary", "Reflective piano, ambient strings, intellectual focus"),
    HORROR("Horror & Thriller", "Dissonant pads, metallic scrapes, ticking suspense"),
    COMEDY("Comedy", "Upbeat pizzicato, quirky acoustic guitar, bounce beats"),
    KIDS("Kids", "Playful xylophone, cheerful glockenspiel, bright melodies"),
    CORPORATE("Corporate & Business", "Polished modern synth, optimistic pulses, clean beats"),
    TRAVEL("Travel & Adventure", "Acoustic folk, lively percussion, world instruments"),
    SPORTS("Sports & High Energy", "Heavy distorted bass, trap drums, stadium hype"),
    TECHNOLOGY("Technology & Cyber", "Futuristic synthwave, glitch textures, digital pulses")
}
