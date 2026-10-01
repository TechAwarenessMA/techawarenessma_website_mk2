package com.techawarenessma.app.certification

/*
 * Rules and state for the Chapter Lead Certification platform, ported from
 * "Chapter Certification.dc.html". Pure Kotlin so it is covered by JVM unit tests.
 */

enum class SectionGroup(val title: String) {
    Orientation("Orientation"),
    Training("Training modules"),
    Operations("Operations"),
}

enum class CertSection(val id: String, val label: String, val group: SectionGroup) {
    HowToUse("how", "How to use this", SectionGroup.Orientation),
    WhatTaaIs("what", "What TAA is", SectionGroup.Orientation),
    WhatAChapterIs("chapter", "Backing & what a chapter is", SectionGroup.Orientation),
    Roles("roles", "Roles & code of conduct", SectionGroup.Orientation),
    M1("m1", "M1 · Tools & safety", SectionGroup.Training),
    M2("m2", "M2 · Repair fundamentals", SectionGroup.Training),
    M3("m3", "M3 · Required viewing", SectionGroup.Training),
    M4("m4", "M4 · Teaching older adults", SectionGroup.Training),
    M5("m5", "M5 · Hands-on & escalation", SectionGroup.Training),
    P7("p7", "P7 · Running your chapter", SectionGroup.Operations),
    P8("p8", "P8 · Knowledge check", SectionGroup.Operations),
    P9("p9", "P9 · Certification", SectionGroup.Operations),
    ;

    val isModule: Boolean get() = group == SectionGroup.Training

    /** "M1" for the checkpoint button label. */
    val shortName: String get() = id.uppercase()

    val next: CertSection? get() = entries.getOrNull(ordinal + 1)
    val previous: CertSection? get() = entries.getOrNull(ordinal - 1)

    companion object {
        fun byId(id: String?): CertSection? = entries.firstOrNull { it.id == id }
        val modules: List<CertSection> get() = entries.filter { it.isModule }
    }
}

data class CertItem(val id: String, val title: String, val detail: String)

/** Part 9 · the six things submitted together. */
val CertItems = listOf(
    CertItem("kc", "01 · Knowledge check", "All 68 questions of Part 8, in writing, in your own words. (Part 8 content pending the full handbook — see the gap note in Part 8.)"),
    CertItem("repair", "02 · Repair video, 5–8 minutes", "One complete repair on a practice device, filmed straight through, hands visible. Mat in use, wrist strap clipped, fireproof container in frame for battery work, device powering on at the end. If something goes wrong, keep filming — a mistake handled calmly is better evidence than a flawless run."),
    CertItem("lesson", "03 · Mock lesson video, 10 minutes", "Teach one genuine beginner. We count how many times you touched their device, and whether “just,” “simply,” or “easy” showed up. A technically perfect lesson where you did the work for them fails. That’s the point."),
    CertItem("evidence", "04 · Checkpoint evidence", "The photos and short write-ups from all five module checkpoints."),
    CertItem("referral", "05 · Your referral list", "Completed for your town."),
    CertItem("plan", "06 · Chapter plan, one page", "Venue and contact, recurring date, inventory, container and disposal point, your Co-Lead prospect, and your first three workshop topics."),
)

data class CertificationState(
    val authed: Boolean = false,
    val lastSection: CertSection = CertSection.HowToUse,
    val doneModules: Set<String> = emptySet(),
    val certItems: Set<String> = emptySet(),
) {
    val doneCount: Int get() = CertSection.modules.count { it.id in doneModules }
    val certCount: Int get() = CertItems.count { it.id in certItems }
    fun isDone(section: CertSection) = section.id in doneModules
}

/** Same rule as the website's prototype gate: `TAA-` plus at least three letters or digits. */
fun isValidAccessCode(code: String): Boolean =
    Regex("^TAA-[A-Za-z0-9]{3,}$", RegexOption.IGNORE_CASE).matches(code.trim())

const val ACCESS_CODE_ERROR =
    "That doesn’t look like a TAA access code (TAA-XXXX). Check the email that came with your approval."

enum class StageStatus { Done, Current, Locked, Upcoming }

data class Stage(val number: Int, val label: String, val status: StageStatus, val mark: String)

/**
 * The path stepper across the top of the platform: Application through First workshop,
 * then the fixed "9 · Full charter" step.
 */
fun pathStages(state: CertificationState): List<Stage> {
    val modulesDone = state.doneCount == CertSection.modules.size
    val certDone = state.certCount == CertItems.size
    val defs = listOf(
        Triple("Application", StageStatus.Done, null),
        Triple("Handbook issued", StageStatus.Done, null),
        Triple("Modules 1–5", if (modulesDone) StageStatus.Done else StageStatus.Current, "${state.doneCount}/5"),
        Triple("Knowledge check", StageStatus.Locked, null),
        Triple("Certification", if (certDone) StageStatus.Done else StageStatus.Upcoming, "${state.certCount}/6"),
        Triple("TAA review", StageStatus.Upcoming, null),
        Triple("Chartered provisional", StageStatus.Upcoming, null),
        Triple("First workshop", StageStatus.Upcoming, null),
        Triple("Full charter · 3 workshops", StageStatus.Upcoming, null),
    )
    return defs.mapIndexed { index, (label, status, extra) ->
        val mark = when {
            status == StageStatus.Done -> "✓"
            status == StageStatus.Current && extra != null -> extra
            else -> (index + 1).toString()
        }
        Stage(number = index + 1, label = label, status = status, mark = mark)
    }
}
