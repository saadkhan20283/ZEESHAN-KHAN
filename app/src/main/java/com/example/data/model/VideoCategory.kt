package com.example.data.model

enum class VideoCategory(
    val displayName: String,
    val iconName: String,
    val description: String,
    val tone: String,
    val narrativeStyle: String,
    val defaultCameraMotion: String,
    val visualPacing: String,
    val factCheckingRequirement: String
) {
    CINEMATIC_REALISTIC(
        displayName = "Cinematic / Realistic",
        iconName = "movie",
        description = "High-budget cinematic visuals with shallow depth of field and dramatic framing.",
        tone = "Immersive, atmospheric, artistic",
        narrativeStyle = "Hero journey, visual spectacle, evocative narration",
        defaultCameraMotion = "Slow cinematic dolly-in, anamorphic lens pan, 24fps film motion",
        visualPacing = "Measured, dramatic pauses, high dynamic range",
        factCheckingRequirement = "Artistic fiction or realistic dramatization"
    ),
    STORYTELLING(
        displayName = "Storytelling",
        iconName = "auto_stories",
        description = "Narrative-driven story with compelling emotional hooks and character arcs.",
        tone = "Engaging, heartfelt, suspenseful",
        narrativeStyle = "Three-act structure: hook, rising tension, climax and resolution",
        defaultCameraMotion = "Character-focused tracking shots, medium close-ups",
        visualPacing = "Dynamic, builds tension towards revelations",
        factCheckingRequirement = "Distinguish personal narrative vs documented events"
    ),
    MYSTERY_CRIME(
        displayName = "Mystery & Fictional Crime",
        iconName = "search",
        description = "Chilling unsolved enigmas, investigative deductions, and noir atmospheres.",
        tone = "Eerie, suspenseful, analytical",
        narrativeStyle = "Clue-by-clue revelation, unreliable narrator, plot twists",
        defaultCameraMotion = "Slow creeping push, moody low-angle shadow movements",
        visualPacing = "Slow-burn suspense with sudden reveal cuts",
        factCheckingRequirement = "Strictly label fictional crime as fiction; verify historical crime records"
    ),
    EDUCATIONAL_DOCS(
        displayName = "Educational / Documentary",
        iconName = "menu_book",
        description = "Deep dives into history, philosophy, culture, and nature with rigorous factual integrity.",
        tone = "Authoritative, insightful, curious",
        narrativeStyle = "Thesis statement, archival evidence, expert breakdowns, holistic summary",
        defaultCameraMotion = "Panoramic landscape sweep, macro detail zoom, aerial drone flyovers",
        visualPacing = "Informative, lucid pacing synchronized with narration",
        factCheckingRequirement = "Mandatory fact-checking: distinguish verified facts from academic inferences"
    ),
    CARS_LUXURY(
        displayName = "Cars & Luxury",
        iconName = "directions_car",
        description = "Hypercars, luxury timepieces, superyachts, bespoke architecture, and elite craftsmanship.",
        tone = "Prestigious, sleek, exhilarating",
        narrativeStyle = "Engineering excellence, aesthetic perfection, rarity and heritage",
        defaultCameraMotion = "High-speed rolling tracking shots, reflection sweeps, carbon-fiber macro passes",
        visualPacing = "High energy, rhythmic rev cuts, golden-hour brilliance",
        factCheckingRequirement = "Accurate vehicle specifications and production counts"
    ),
    BUSINESS_FINANCE(
        displayName = "Business / Finance",
        iconName = "trending_up",
        description = "Market trends, corporate battles, macroeconomics, and billionaire case studies.",
        tone = "Strategic, analytical, cautionary",
        narrativeStyle = "Problem, economic stakes, strategic maneuvers, market outcomes",
        defaultCameraMotion = "Clean isometric graphics motion, corporate boardroom pans",
        visualPacing = "Crisp, concise, data-informed transitions",
        factCheckingRequirement = "No financial profit guarantees; cite verified economic data"
    ),
    AI_TECHNOLOGY(
        displayName = "AI / Technology",
        iconName = "memory",
        description = "Cutting-edge artificial intelligence, robotics, quantum computing, and future tech.",
        tone = "Futuristic, visionary, objective",
        narrativeStyle = "Breakthrough revelation, technical architecture, societal implications",
        defaultCameraMotion = "Cybernetic camera rotation, matrix data pass, holographic transitions",
        visualPacing = "Rapid, sleek, high-tech motion graphics integration",
        factCheckingRequirement = "Distinguish proven tech benchmarks from theoretical models"
    ),
    KIDS_ANIMATION(
        displayName = "Kids Animation",
        iconName = "toys",
        description = "Whimsical, colorful animated moral tales, phonics, and friendly learning adventures.",
        tone = "Joyful, gentle, wonder-filled",
        narrativeStyle = "Simple moral dilemma, musical repetition, cheerful triumph",
        defaultCameraMotion = "Bouncy animated camera, colorful 2D/3D smooth movements",
        visualPacing = "Energetic, clear focal points, soft rounded shapes",
        factCheckingRequirement = "Child-safe standards, COPPA compliant, no scary elements"
    ),
    ISLAMIC_EDUCATIONAL(
        displayName = "Islamic Educational",
        iconName = "mosque",
        description = "Historical Islamic golden age, architecture, ethical principles, and scholarly biographies.",
        tone = "Respectful, peaceful, spiritually uplifting",
        narrativeStyle = "Reverent historical documentation, moral wisdom, community reflection",
        defaultCameraMotion = "Majestic architectural tilts, serene nature pans, calligraphic reveals",
        visualPacing = "Contemplative, dignified, harmonious",
        factCheckingRequirement = "Strict fidelity to authenticated historical texts and respected scholars"
    ),
    COMEDY(
        displayName = "Comedy",
        iconName = "sentiment_very_satisfied",
        description = "Satire, funny relatable sketches, witty commentary, and humorous situations.",
        tone = "Humorous, witty, playful",
        narrativeStyle = "Setup, comedic escalation, unexpected punchline",
        defaultCameraMotion = "Comedic crash zoom, exaggerated pan, comedic freeze-frame",
        visualPacing = "Snappy comic timing and reaction cuts",
        factCheckingRequirement = "Parody and satire disclaimer where applicable"
    ),
    GAMING(
        displayName = "Gaming",
        iconName = "sports_esports",
        description = "Epic game lore, walkthroughs, meta strategies, and cinematic boss battles.",
        tone = "Exciting, gamer-authentic, fast-paced",
        narrativeStyle = "Game lore backstory, competitive challenge, clutch victory",
        defaultCameraMotion = "Dynamic free-cam orbit, fast first-person push, shake on impacts",
        visualPacing = "Ultra-fast, synchronized with game action beats",
        factCheckingRequirement = "Accurate game patch notes and community terminology"
    ),
    TRAVEL(
        displayName = "Travel",
        iconName = "flight",
        description = "Hidden travel gems, cultural explorations, breathtaking vistas, and street food.",
        tone = "Adventurous, sensory, inviting",
        narrativeStyle = "Arrival, cultural immersion, local encounters, departing reflections",
        defaultCameraMotion = "Wide aerial drone sweeps, first-person walking gimbal motion",
        visualPacing = "Lush, scenic, warm sunset color palettes",
        factCheckingRequirement = "Accurate geographical details and travel safety tips"
    ),
    NEWS_STYLE(
        displayName = "News-style",
        iconName = "newspaper",
        description = "Timely journalistic coverage, investigative reporting, and news updates.",
        tone = "Objective, urgent, clear",
        narrativeStyle = "Inverted pyramid: headline, key facts, context, stakeholder statements",
        defaultCameraMotion = "Steady studio dolly, broadcast lower-third graphic sweeps",
        visualPacing = "Brisk, newsroom tempo with high information density",
        factCheckingRequirement = "Strict dual-source verification; clear separation of commentary"
    ),
    SHORTS_REELS_TIKTOK(
        displayName = "Shorts / Reels / TikTok",
        iconName = "smartphone",
        description = "3-second instant hook, high-retention vertical short with dynamic word captions.",
        tone = "Hyper-engaging, high retention, viral curiosity",
        narrativeStyle = "Extreme hook within 2 seconds, rapid proof, loop ending",
        defaultCameraMotion = "Continuous push-in, kinetic speed ramps, snap transitions",
        visualPacing = "Cut every 1.5 - 2.5 seconds, continuous motion",
        factCheckingRequirement = "Verify claims to prevent deceptive short-form misinformation"
    ),
    MUSIC_VIDEOS(
        displayName = "Music Videos",
        iconName = "music_note",
        description = "Rhythmic beat-matched visual poetry, expressive choreography, and lighting art.",
        tone = "Rhythmic, emotive, stylized",
        narrativeStyle = "Visual motif tied to lyrical rhythm and chorus crescendos",
        defaultCameraMotion = "Beat-synchronized zoom, orbit, handheld stylized flow",
        visualPacing = "Cut on musical downbeats and transient peaks",
        factCheckingRequirement = "Proper music licensing compliance"
    ),
    ANIMATION_CARTOON(
        displayName = "Animation / Cartoon",
        iconName = "palette",
        description = "Expressive characters, 3D/2D animation styles, expressive exaggeration.",
        tone = "Creative, expressive, vibrant",
        narrativeStyle = "Character-driven conflict, visual slapstick or heartfelt journey",
        defaultCameraMotion = "Dynamic cartoon pans, squashing and stretching camera",
        visualPacing = "Lively, expressive frame-by-frame emotion",
        factCheckingRequirement = "Original character designs and storyboards"
    ),
    EXPLAINER_VIDEOS(
        displayName = "Explainer Videos",
        iconName = "lightbulb",
        description = "Simplifying intricate concepts into visual metaphors, diagrams, and clear analogies.",
        tone = "Clear, encouraging, structured",
        narrativeStyle = "The Problem, The Analogy, Step-by-Step Breakdown, Practical Takeaway",
        defaultCameraMotion = "Smooth panning across diagrammatic planes, focus spotlighting",
        visualPacing = "Step-by-step clarity, zero confusion",
        factCheckingRequirement = "Scientifically and technically vetted explanations"
    ),
    ADS_PROMOTIONAL(
        displayName = "Ads / Promotional",
        iconName = "campaign",
        description = "High-conversion product showcases, emotional branding, and compelling calls-to-action.",
        tone = "Persuasive, premium, energetic",
        narrativeStyle = "Pain point, transformative solution, social proof, irresistible CTA",
        defaultCameraMotion = "Slick product 360 spin, hero lighting glints, slow motion beauty shots",
        visualPacing = "Punched, polished, CTA-driven ending",
        factCheckingRequirement = "Truth in advertising, verified product capabilities"
    ),
    SCIENCE(
        displayName = "Science",
        iconName = "science",
        description = "Astrophysics, molecular biology, climate systems, and deep scientific wonders.",
        tone = "Inquisitive, awe-inspiring, empirical",
        narrativeStyle = "Cosmic or micro mystery, scientific method, experiments, frontier discoveries",
        defaultCameraMotion = "Microscopic zooms, cosmic galactic scale shifts, orbit passes",
        visualPacing = "Expansive, awe-inducing, deep space/laboratory contrast",
        factCheckingRequirement = "Peer-reviewed scientific consensus with frontiers noted"
    ),
    SPORTS(
        displayName = "Sports",
        iconName = "fitness_center",
        description = "Athletic triumphs, historic comebacks, player tactics, and adrenaline-pumping highlights.",
        tone = "Adrenaline-fueled, passionate, triumphant",
        narrativeStyle = "Underdog challenge, grueling preparation, decisive game-winning moment",
        defaultCameraMotion = "Sideline tracking, high-speed slow-mo capture, stadium whip pans",
        visualPacing = "Explosive, heartbeat builds, instant replays",
        factCheckingRequirement = "Official match scores and athletic records"
    ),
    HORROR_SUSPENSE(
        displayName = "Horror / Suspense",
        iconName = "sentiment_dissatisfied",
        description = "Psychological thrills, spine-chilling folklore, dark corridors, and visceral tension.",
        tone = "Dread-filled, ominous, claustrophobic",
        narrativeStyle = "Subtle wrongness, claustrophobic trap, escalation, chilling final twist",
        defaultCameraMotion = "Creeping Dutch angles, flashlight cone tracking, sudden stillness",
        visualPacing = "Suffocatingly quiet pauses broken by sharp psychological climaxes",
        factCheckingRequirement = "Clearly framed as fictional suspense/folklore"
    ),
    LONG_FORM_YOUTUBE(
        displayName = "Long-form YouTube",
        iconName = "smart_display",
        description = "10 to 30 minute comprehensive video essays with chapters, pacing variety, and high watch-time.",
        tone = "Immersive, intellectual, conversational",
        narrativeStyle = "Multi-chapter essay: Hook, Historical roots, Contemporary conflict, Synthesis",
        defaultCameraMotion = "Varied cinematic grammar: steady cams, infographics, b-roll shifts",
        visualPacing = "Chapterized pacing, strategic mid-video re-hooks",
        factCheckingRequirement = "Comprehensive source citations and fact attribution"
    );

    companion object {
        fun fromString(value: String): VideoCategory {
            return entries.firstOrNull { 
                it.name.equals(value, ignoreCase = true) || 
                it.displayName.equals(value, ignoreCase = true) 
            } ?: CINEMATIC_REALISTIC
        }
    }
}
