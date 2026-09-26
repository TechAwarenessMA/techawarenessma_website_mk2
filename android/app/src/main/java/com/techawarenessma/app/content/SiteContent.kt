package com.techawarenessma.app.content

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.techawarenessma.app.R
import com.techawarenessma.app.ui.theme.TaaColors

/*
 * The website's data, ported verbatim from the `data`/`toolData` arrays in each page's
 * script block. Keep the two in sync when either changes.
 */

data class BenchTool(
    val id: String,
    val name: String,
    val tag: String,
    val what: String,
    val why: String,
    val iconPath: String,
)

/** Home page · "Tap a tool. Learn what it does." */
val BenchTools = listOf(
    BenchTool(
        "driver", "Precision driver", "64-bit driver kit",
        "An aluminum screwdriver handle with 64 swappable bits — Phillips, pentalobe, Torx, tri-point.",
        "Phones and laptops use tiny, weird screws on purpose. The right bit opens every one of them without stripping.",
        "M12 3h8v8h-8z M16 11v14 M16 25l-2 4h4z",
    ),
    BenchTool(
        "spudger", "Spudger", "nylon pry stick",
        "A stiff nylon stick with a flat end and a pointed end.",
        "Disconnecting ribbon cables and prying connectors without scratching boards or shorting anything — it’s non-conductive.",
        "M14 3h4v5l2 3v10l-4 8-4-8V11l2-3z",
    ),
    BenchTool(
        "picks", "Opening picks", "thin plastic wedges",
        "Guitar-pick-shaped slivers of plastic, thinner than a credit card.",
        "Sliding through glued seams to separate a screen or back cover, a few millimeters at a time, without cracking glass.",
        "M16 29C11 23 8 17 8 12a8 8 0 0 1 16 0c0 5-3 11-8 17z",
    ),
    BenchTool(
        "tweezers", "Precision tweezers", "blunt + angled",
        "Fine-tipped steel tweezers, straight and angled.",
        "Placing screws the size of sand grains, and pulling connectors your fingers are three sizes too big for.",
        "M11 3c1 9 3 17 5 25 M21 3c-1 9-3 17-5 25 M10 3h4 M18 3h4",
    ),
    BenchTool(
        "suction", "Suction handle", "screen lifter",
        "A rubber suction cup with a grip handle.",
        "Pulling a screen up just enough to slip a pick underneath — the first move of almost every phone repair.",
        "M7 24c0-7 4-12 9-12s9 5 9 12z M16 12V7 M10 7h12",
    ),
    BenchTool(
        "jimmy", "The Jimmy", "flexible blade",
        "A thin, flexible steel blade with a comfortable handle.",
        "Getting under stubborn adhesive and battery glue where picks are too soft and screwdrivers are too pointy.",
        "M20 4h8v5h-8z M28 9c0 10-9 18-22 20 M20 9c0 7-5 12-12 14",
    ),
)

data class ImpactStat(val value: Int, val suffix: String, val label: String, val color: Color)

/** Home page impact numbers. "Workshops running" is the page's `workshopsCount` prop (default 24). */
val ImpactStats = listOf(
    ImpactStat(442, "", "Devices collected", TaaColors.Cyan),
    ImpactStat(1545, "", "lbs of e-waste diverted", TaaColors.Coral),
    ImpactStat(24, "", "Workshops running", TaaColors.Yellow),
    ImpactStat(100, "+", "People taught", TaaColors.Cyan),
    ImpactStat(120, "+", "Devices repaired", TaaColors.Coral),
)

data class Chapter(
    val id: String,
    val name: String,
    val sub: String,
    val live: Boolean,
    val schedule: String,
    val venue: String,
    @DrawableRes val photo: Int?,
    val photoAlt: String?,
    /** The website's library signup links are still `#` placeholders; null hides the button. */
    val signupUrl: String?,
    val blurb: String,
)

val Chapters = listOf(
    Chapter(
        "shrewsbury", "Shrewsbury", "Flagship chapter", live = true,
        schedule = "Tuesdays, 7:30–8:30 PM", venue = "Shrewsbury Public Library",
        photo = R.drawable.photo_marking_screws,
        photoAlt = "A student marking screw positions on an iFixit mat beside an opened phone at the Shrewsbury workshop",
        signupUrl = null,
        blurb = "Where it started. The full 12-week cycle runs here every week — drop in any Tuesday, no signup needed to attend.",
    ),
    Chapter(
        "grafton", "Grafton", "Live chapter", live = true,
        schedule = "Saturdays, 12:00–1:00 PM", venue = "Grafton Public Library",
        photo = R.drawable.photo_camera,
        photoAlt = "A young student examining a disassembled camera over an iFixit mat at the Grafton workshop",
        signupUrl = null,
        blurb = "Our first expansion town. Same curriculum, Saturday schedule — good if weeknights don’t work for you.",
    ),
    Chapter(
        "southgate", "Southgate", "Senior Center", live = true,
        schedule = "Time TBD", venue = "Southgate Senior Center",
        photo = R.drawable.photo_parent_laptop,
        photoAlt = "A one-on-one session over an open laptop at Southgate Senior Center",
        signupUrl = null,
        blurb = "Home of Senior Tech Support — one-on-one sessions, your device and your questions. (Confirming whether this becomes a general workshop chapter too.)",
    ),
    Chapter(
        "westborough", "Westborough", "Forming", live = false,
        schedule = "Time TBD", venue = "Venue TBD", photo = null, photoAlt = null, signupUrl = null,
        blurb = "Forming now. We’re lining up a venue and a chapter leader — if that could be you, we want to hear from you.",
    ),
    Chapter(
        "worcester", "Worcester", "Forming", live = false,
        schedule = "Time TBD", venue = "Venue TBD", photo = null, photoAlt = null, signupUrl = null,
        blurb = "Forming now. The biggest town on our list — and the one that needs the most hands.",
    ),
    Chapter(
        "hopkinton", "Hopkinton", "Forming", live = false,
        schedule = "Time TBD", venue = "Venue TBD", photo = null, photoAlt = null, signupUrl = null,
        blurb = "Forming now. First session happens as soon as a leader steps up.",
    ),
)

fun chapterStatusColor(live: Boolean) = if (live) TaaColors.Cyan else TaaColors.Yellow

data class MemberWork(val title: String, val detail: String)

data class Member(
    val id: String,
    val name: String,
    val role: String,
    val accent: Color,
    @DrawableRes val photo: Int,
    val bio: String,
    val does: String? = null,
    val work: List<MemberWork> = emptyList(),
) {
    val firstName: String get() = name.substringBefore(' ')

    /** Yellow is unreadable as text on cream, so the site swaps in a darker gold for role lines. */
    val roleColor: Color get() = if (accent == TaaColors.Yellow) TaaColors.GoldText else accent
}

/** Members page · "One large team". Order matters: it drives the "Next:" link on profiles. */
val Members = listOf(
    Member(
        "ronit", "Ronit Sharma", "Executive Director", TaaColors.Yellow, R.drawable.member_ronit,
        does = "Strategy, partners, and the inbox — keeps the whole 501(c)(3) moving.",
        bio = "Runs the whole thing — strategy, partners, and the inbox. The reason a school-club rejection turned into a 501(c)(3).",
        work = listOf(
            MemberWork("Partnerships", "First call for libraries, sponsors, and town officials — and the signature on every agreement."),
            MemberWork("Nonprofit paperwork", "Filings, insurance, and everything that keeps the 501(c)(3) status real."),
            MemberWork("Chapter approvals", "Reviews every Start-a-Chapter application and issues the access codes for chapter certification."),
        ),
    ),
    Member(
        "tanay", "Tanay Mangal", "Programs & Education Lead", TaaColors.Blue, R.drawable.member_tanay,
        does = "Owns the 12-week curriculum and what happens at every bench.",
        bio = "Owns the 12-week curriculum and what happens at every bench, every week, in every chapter.",
        work = listOf(
            MemberWork("Curriculum", "Writes and updates the 12-week repair curriculum every chapter runs from."),
            MemberWork("Workshop nights", "Plans each session — which devices, which skills, which benches — before anyone shows up."),
            MemberWork("Certification", "Maintains the 9-module chapter certification content, flashcards and flowcharts included."),
        ),
    ),
    Member(
        "nathan", "Nathan Anwin", "Operations & Logistics Lead", TaaColors.Red, R.drawable.member_nathan,
        does = "Toolkits, devices, venues, drives — gets things where they need to be.",
        bio = "Toolkits, devices, venues, drives — if it had to be somewhere on a Tuesday night, Nathan got it there.",
        work = listOf(
            MemberWork("Toolkits", "Builds and restocks every bench toolkit — drivers, spudgers, spare parts."),
            MemberWork("Collection drives", "Runs the device drives behind the 442 devices collected so far."),
            MemberWork("Venues", "Books and sets up the rooms in Shrewsbury, Grafton, Westborough, and Worcester."),
        ),
    ),
    Member(
        "ekansh", "Ekansh Jain", "Finance & Impact Lead", TaaColors.Blue, R.drawable.member_ekansh,
        does = "Counts the dollars and the pounds of e-waste — honestly.",
        bio = "Counts the dollars and the pounds of e-waste — honestly. Every stat on the homepage goes through him first.",
        work = listOf(
            MemberWork("Budget", "Tracks every donation in and every toolkit dollar out."),
            MemberWork("Impact numbers", "Weighs, counts, and verifies the stats — 1,545 lbs diverted, 120+ repairs — before they go public."),
            MemberWork("Reporting", "Writes the reports partners and sponsors see at the end of each season."),
        ),
    ),
    Member(
        "suhrit", "Suhrit Ghosh", "Outreach & Community Lead", TaaColors.Red, R.drawable.member_suhrit,
        does = "Libraries, senior centers, and the feed — gets every town to show up.",
        bio = "Gets every town to show up — libraries, senior centers, and the feed. If you found us online, that was him.",
        work = listOf(
            MemberWork("Community venues", "Keeps the library and senior-center relationships that host our workshops."),
            MemberWork("New chapters", "Finds and recruits the students who want to start a chapter in their town."),
            MemberWork("Instagram", "Shoots and posts the workshop timelapses and bench photos on the feed."),
        ),
    ),
    Member(
        "howie", "Howie Cao", "Westborough Chapter Lead", TaaColors.Yellow, R.drawable.member_howie,
        bio = "Runs the Westborough chapter — the benches, the people, and the weekly workshop night.",
    ),
    Member(
        "atharv", "Atharv Mishra", "Westborough Programs Lead", TaaColors.Blue, R.drawable.member_atharv,
        bio = "Runs the 12-week curriculum at the Westborough benches.",
    ),
    Member(
        "cullen", "Cullen Bautista", "Westborough Operations & Outreach Lead", TaaColors.Red, R.drawable.member_cullen,
        bio = "Toolkits and turnout — runs both operations and outreach for Westborough.",
    ),
    Member(
        "niti", "Niti Tyagi", "Worcester Chapter Lead", TaaColors.Yellow, R.drawable.member_niti,
        bio = "Runs the Worcester chapter — the benches, the people, and the weekly workshop night.",
    ),
    Member(
        "bhoomi", "Bhoomi Prashanth", "Worcester Programs Lead", TaaColors.Blue, R.drawable.member_bhoomi,
        bio = "Runs the 12-week curriculum at the Worcester benches.",
    ),
    Member(
        "dhriti", "Dhriti Krishnaswamy", "Worcester Outreach Lead", TaaColors.Red, R.drawable.member_dhriti,
        bio = "Gets Worcester to show up — venues, flyers, and new members.",
    ),
    Member(
        "ram", "Ram Kondapalli", "Grafton Chapter Lead", TaaColors.Yellow, R.drawable.member_ram,
        bio = "Runs the Grafton chapter — the benches, the people, and the weekly workshop night.",
    ),
    Member(
        "puneeth", "Puneeth Nuna", "Grafton Programs Lead", TaaColors.Blue, R.drawable.member_puneeth,
        bio = "Runs the 12-week curriculum at the Grafton benches.",
    ),
    Member(
        "kevin", "Kevin Lui", "Grafton Operations Lead", TaaColors.Red, R.drawable.member_kevin,
        bio = "Toolkits, devices, and room setup for the Grafton workshop nights.",
    ),
    Member(
        "param", "Param Tyagi", "Grafton Outreach Lead", TaaColors.Yellow, R.drawable.member_param,
        bio = "Gets Grafton to show up — venues, flyers, and new members.",
    ),
)

fun memberById(id: String?): Member? = Members.firstOrNull { it.id == id }

/** The profile page's "Next:" link wraps from the last member back to the first. */
fun nextMember(current: Member): Member = Members[(Members.indexOf(current) + 1) % Members.size]

/** Programs page · "12 weeks, then it restarts." */
val CurriculumWeeks = listOf(
    "General Awareness",
    "Repair or Replace?",
    "Batteries & Device Lifespans",
    "Phone Care & Preventative Maintenance",
    "Laptop Awareness",
    "Laptop Maintenance & Simple Fixes",
    "Community Repair & Learning Together",
    "Accessibility & Seniors",
    "Online Safety & Scams",
    "Meeting AI — Everyday Tools Explained",
    "AI Red Flags — Deepfakes, Cloned Voices & AI Scams",
    "Capstone Fix-a-Thon — Community Repair Night",
)
