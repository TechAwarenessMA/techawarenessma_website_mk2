package com.techawarenessma.app.certification

// Course material from "Chapter Certification.dc.html", verbatim. Text uses the app's
// small markup: **bold**, *italic*, [label](target).

data class KitTool(val id: String, val name: String, val qty: String, val iconPath: String, val useFor: String, val notFor: String)

/** Module 1 · The kit · iFixit Pro Tech Toolkit */
val KitTools = listOf(
    KitTool("driver", "64-bit driver", "×1 + 64 bits", "M12 3h8v8h-8z M16 11v14 M16 25l-2 4h4z", "Phillips, Torx, Pentalobe, JIS, Tri-point and more. Seat the bit fully, press down, turn steadily.", "Forcing a half-seated bit, or using a Phillips bit on a JIS screw — it strips."),
    KitTool("spudger", "Nylon spudger", "×2", "M14 3h4v5l2 3v10l-4 8-4-8V11l2-3z", "Your go-to around connectors and boards. Pointed end for cables, blunt end for prying.", "Heavy leverage. If it’s flexing hard, you’ve got the wrong tool or a hidden screw."),
    KitTool("opening", "Plastic opening tools", "×3", "M13 3h6v14l-3 12-3-12z", "The default pry. Not sure which tool to grab? It’s usually this one.", "Levering against fragile board edges or the face of a battery cell."),
    KitTool("picks", "Opening picks", "×6", "M16 29C11 23 8 17 8 12a8 8 0 0 1 16 0c0 5-3 11-8 17z", "Cut and hold screen adhesive. They wear out, so buy extras.", "Pushing deeper than a few millimeters — flex cables live under screens."),
    KitTool("tweezers", "ESD tweezers", "×3", "M11 3c1 9 3 17 5 25 M21 3c-1 9-3 17-5 25 M10 3h4 M18 3h4", "Screws, ribbon cables, tight spots. ESD safe.", "Prying — ever. They’re genuinely sharp, and never a prying tool."),
    KitTool("suction", "Suction cup", "×1", "M7 24c0-7 4-12 9-12s9 5 9 12z M16 12V7 M10 7h12", "Lifts glued displays. Warm the screen first; tape over shattered glass.", "Beating adhesive on its own — pair it with heat and picks, don’t yank."),
    KitTool("jimmy", "Metal spudger · Jimmy", "×2", "M20 4h8v5h-8z M28 9c0 10-9 18-22 20 M20 9c0 7-5 12-12 14", "Stubborn casings only.", "NOT ESD safe, and never near a battery. These stay in the Lead’s hands, not an attendee’s."),
    KitTool("strap", "Anti-static wrist strap", "×1", "M16 22a6 6 0 1 1 0-12 6 6 0 0 1 0 12z M16 22v7 M13 29h6", "Clipped to unpainted metal before the case opens. One of the two most underestimated things in the kit.", "Treating it as optional — ESD damage is the damage you can’t feel, and it shows up three weeks later."),
    KitTool("mat", "Magnetic mat", "×1", "M4 8h24v18H4z M4 14h24 M4 20h24 M11 8v18 M19 8v18", "The Mat Method: every screw mapped, in position, labeled.", "Skipping it. The mat gets used or the repair doesn’t start."),
)

data class Step(val title: String, val detail: String)

/** Module 1 · Screw discipline · The Mat Method */
val MatMethodSteps = listOf(
    Step("Draw the device outline on the mat before you open anything", "Before a single screw turns, the outline goes on the mat. Hand the attendee the marker — drawing it turns them from a passenger into the person running the process."),
    Step("Each screw goes on the mat in the position it came from", "Not in a pile, not in a cup. The screw sits on the outline exactly where it lives in the device. The magnet holds it there."),
    Step("Circle and label each group", "\"Back cover\", \"shield\", \"bracket\". Thirty seconds of labeling saves the reassembly — and saves the board from a screw one millimeter too long."),
    Step("On reassembly, work backwards through the mat", "The mat is now a map. Last group off is the first group back on. No leftovers, no guessing."),
    Step("Can’t account for every screw? Don’t close the device.", "The hard rule. A missing screw is either lost (fine, note it) or inside the device (not fine). You find out which one before the case closes."),
)

/** Module 2 · The universal repair sequence */
val RepairSequence = listOf(
    Step("Listen", "Let them describe the problem all the way through without jumping in to guess. “It’s slow,” “it shuts off,” and “the screen went dark” are three different problems."),
    Step("Reproduce", "Make the fault happen in front of you. If you can’t reproduce it, you can’t prove you fixed it."),
    Step("Ask the boring questions", "How old is it? What changed? Was it dropped? Was it wet? And the single best diagnostic question there is: “has anyone worked on it before?”"),
    Step("Rule out software", "Reboot, check storage, battery health, updates, and any app installed the week it started. This step alone solves a third of what walks in the door."),
    Step("Research", "Look up the exact model on iFixit. Read the whole guide first, and read the comments — that’s where you find out step 14 breaks the flex cable."),
    Step("Plan the exit", "What’s the failure mode of this repair, and what do you do if it happens? If there’s data on it the owner cares about and it isn’t backed up, back it up or stop."),
    Step("Disassemble deliberately", "Mat out, screws mapped, photos as you go. Stop at the first weird resistance and figure out why. Force isn’t a technique. It’s a decision to break something."),
    Step("Test before closing", "Power on and test the fault plus the basics: display, touch, sound, charging, buttons. Finding a dead speaker after you’ve re-glued the screen means doing the repair twice."),
    Step("Debrief", "What was wrong, what fixed it, what to watch for. If it was the battery, explain what battery health means and how to slow the decline."),
)

data class DeviceClass(val name: String, val tagline: String, val iconPath: String, val can: List<String>, val cannot: String)

/** Module 2 · What's realistic, by device class */
val DeviceClasses = listOf(
    DeviceClass("Desktops", "Easiest. Start nervous attendees here.", "M6 6h20v14H6z M12 24h8 M16 20v4",
        listOf("RAM and storage installs", "PSU, fans, dust removal", "CMOS battery, OS reinstall", "No-POST basics"),
        "Check donated units for BIOS supervisor locks before building a lesson around them"),
    DeviceClass("Laptops", "The best teaching class", "M7 8h18v11H7z M4 23h24l-3-4H7z",
        listOf("HDD→SSD (the most dramatic upgrade a user will ever feel)", "RAM, battery, keyboard", "Fan clean + thermal paste", "Linux on aging machines"),
        "Board-level work · hinge welding. Always disconnect the internal battery first."),
    DeviceClass("Phones", "Most requested, least forgiving", "M11 4h10v24H11z M14 25h4",
        listOf("Port cleaning, battery swaps on moderate-rated models", "Screens rated moderate or below", "Battery health, backups, scam recognition"),
        "Soldering · water damage · Face ID / Touch ID (serialized, breaks for good)"),
    DeviceClass("Tablets", "Large phones, worse adhesive", "M8 4h16v24H8z M15 25h2",
        listOf("Port cleaning, settings, storage", "Accessibility configuration", "Text size, screen zoom, video calling. Don’t underrate this."),
        "Screen swaps as an attendee activity on most big tablets"),
    DeviceClass("Wearables", "Hardest common class. Be honest.", "M11 9h10v14H11z M13 9l1-5h4l1 5 M13 23l1 5h4l1-5",
        listOf("Bands, charging contacts", "Fall detection, heart rate and accessibility settings — usually the more valuable session anyway"),
        "Watch displays and batteries as attendee hands-on · anything needing water-resistance resealing"),
)

data class VideoAssignment(val channel: String, val title: String, val url: String?, val kind: String, val note: String? = null)

/** Module 3 · The assignments — real links, verified */
val VideoAssignments = listOf(
    VideoAssignment("Phone Repair Guru", "The CHEAPEST Screen Replacement Possible…", "https://www.youtube.com/watch?v=D2VwCU1K55o", "Screen replacement"),
    VideoAssignment("Phone Repair Guru", "Using A 9 Volt Battery To Remove The iPhone 16 Battery", "https://www.youtube.com/watch?v=VNZewnrkDng", "Battery replacement"),
    VideoAssignment("Phone Repair Guru", "Accessing Apple's Secret Diagnostic Tools", "https://www.youtube.com/watch?v=EUlhTPLfrV0", "Diagnostic"),
    VideoAssignment(
        "Phone Repair Guru", "Charging port replacement", url = null, kind = "Gap — don't fill blind",
        note = "**No verified link.** Every search result under his name turned out to belong to other channels. Check his channel directly, or substitute another beginner-friendly channel for this assignment.",
    ),
    VideoAssignment("iFixit", "iFixit's iPhone 15 Teardown", "https://www.youtube.com/watch?v=KIPJfNb8wE0", "Teardown 1 of 2"),
    VideoAssignment("iFixit", "Lenovo Built a Truly Repairable Laptop — ThinkPad T14 Gen 7 Teardown", "https://www.youtube.com/watch?v=qZuKhULGufI", "Teardown 2 of 2"),
    VideoAssignment("Hugh Jeffreys", "Smashed iPhone XS Restoration", "https://www.youtube.com/watch?v=EABmyfX6spE", "Restoration 1 of 2"),
    VideoAssignment("Hugh Jeffreys", "What It Takes To Repair The iPhone 14 Pro Max — Restoration", "https://www.youtube.com/watch?v=iWRKZpvf3Uo", "Restoration 2 of 2"),
    VideoAssignment("Hugh Jeffreys", "iPhone 12 Anti Repair Design — Teardown and Repair Assessment", "https://www.youtube.com/watch?v=FY7DtKMBxBw", "Parts pairing / serialization"),
    VideoAssignment("JerryRigEverything", "iPhone Air Teardown", "https://www.youtube.com/watch?v=7eDcvSfk7BQ", "Teardown 1 of 2"),
    VideoAssignment("JerryRigEverything", "Dear Apple: i am sorry… (iPhone 16 Pro Max Teardown)", "https://www.youtube.com/watch?v=hRfjpEbzvYs", "Teardown 2 of 2"),
    VideoAssignment("Louis Rossmann", "Macbook logic board repair, with Louis Rossmann", "https://www.youtube.com/watch?v=HBf-RYEMe38", "Board-level diagnostic"),
    VideoAssignment("Louis Rossmann", "Right to Repair explained in under 60 seconds (FTC testimony)", "https://www.youtube.com/watch?v=qCFP9P7lIvI", "Industry / policy"),
)

/** Module 3 · Written guides, from the Granite batch's Lenovo ThinkPad E14 Gen 4 */
val WrittenGuides = listOf(
    "RAM" to "https://www.ifixit.com/Guide/Lenovo+ThinkPad+E14+Gen+4+RAM+Replacement/214927",
    "Battery" to "https://www.ifixit.com/Guide/Lenovo+ThinkPad+E14+Gen+4+Battery+Replacement/200826",
    "SSD" to "https://www.ifixit.com/Guide/Lenovo+ThinkPad+E14+Gen+4+SSD+Replacement/200829",
    "Fan & heat sink" to "https://www.ifixit.com/Guide/Lenovo+ThinkPad+E14+Gen+4+Fan+%26+Heat+Sink+Replacement/200825",
    "Keyboard" to "https://www.ifixit.com/Guide/Lenovo+ThinkPad+E14+Gen+4+Keyboard+Replacement/214928",
    "iPhone 14 battery" to "https://www.ifixit.com/Guide/iPhone+14+Battery+Replacement/152966",
)

const val DELL_OPTIPLEX_GUIDE =
    "https://www.dell.com/support/kbdoc/en-us/000131478/optiplex-5080-micro-form-factor-mff-teardown-removal-guide-for-customer-replaceable-unit-crus"

data class LanguageCard(val dont: String, val say: String)

/** Module 4 · Language rules — active-recall flash cards */
val LanguageCards = listOf(
    LanguageCard("It’s easy", "“It takes practice. Everybody fumbles this at first.”"),
    LanguageCard("You just tap there", "“Tap there.” Delete the word “just” entirely."),
    LanguageCard("Let me do it", "“Try it. I’ll talk you through it.”"),
    LanguageCard("No, not that one", "“Close. Try the one right above it.”"),
    LanguageCard("I already explained that", "Explain it again, differently, with no comment."),
    LanguageCard("For someone your age, you’re doing great", "“You’re doing great.”"),
    LanguageCard("Your phone is really old", "“Your phone is a few generations back. Here’s what that means for you.”"),
)

enum class RungZone(val label: String) { Low("Low"), Escalation("Escalation"), Rare("Rare exception") }

data class Rung(val title: String, val zone: RungZone, val detail: String)

/** Module 5 · The escalation ladder */
val EscalationLadder = listOf(
    Rung("Wait", RungZone.Low, "Say nothing. Count to ten. Most fumbling sorts itself out, and the sorting-out is where the learning happens. New volunteers jump in after about two seconds. Train yourself out of it."),
    Rung("Ask a question", RungZone.Low, "“What’s it doing?” · “What do you think is holding it?” · “What did the guide say about that corner?”"),
    Rung("Describe", RungZone.Low, "“The tab is on the left edge, about an inch from the corner.”"),
    Rung("Point", RungZone.Low, "At the spot. Still don’t touch the device."),
    Rung("Demonstrate on a different device", RungZone.Escalation, "Show the motion on your own practice unit, then hand theirs back. This solves most of what’s left, and almost nobody uses it enough."),
    Rung("Guided touch", RungZone.Escalation, "“May I put my hand over yours so you can feel the pressure?” Ask first. Their hand stays on the tool."),
    Rung("You take over", RungZone.Rare, "Only for an active safety risk, or when going further will kill the device and the attendee asked you to. Announce it, narrate it, and hand it back the second it’s past the hard part."),
)

/** Module 5 · Run-of-show for a two-hour session */
val RunOfShow = listOf(
    "T-45" to "Setup: stations, lighting, cords taped, fireproof container out, tools counted in, walker clearance checked",
    "T-15" to "Volunteer huddle. First-workshop volunteers get paired, never solo. The Lead makes the stop call.",
    "0:00" to "Doors. Greeter gets names, sign-in, acknowledgment form, and “what brought you in today?”",
    "0:10" to "**Safety briefing. Every workshop. No exceptions.** Two minutes, and nobody touches a tool before it happens.",
    "0:12" to "Go-around: your name + the thing that annoys you most about your device. Write the list up. That’s your agenda.",
    "0:20" to "Station work. 2–4 attendees per instructor. Escalation ladder in effect. **← Tiers apply here**",
    "1:00" to "Cross-pollinate: one person per station shares what surprised them. Five minutes.",
    "1:40" to "**Hard stop on new disassembly.** Close, test, account for everything. An open device at session end goes home broken.",
    "1:50" to "Close-out: one thing you learned, one thing you’ll try at home. Announce the next date.",
    "2:00" to "Teardown: tools counted out, batteries contained, room left better than you found it. That’s how you keep the venue.",
    "+7d" to "Post-workshop report to TAA.",
)

/** Module 5 · Tier 3 — all eight, or it's Tier 2 + referral */
val Tier3Conditions = listOf(
    "Lead is certified and has done this exact repair on a practice unit",
    "Attendee was told out loud it may come back worse, and said yes anyway",
    "Data backed up, or the risk explicitly accepted",
    "Signed acknowledgment on file",
    "Battery below 25%, or the repair doesn’t go near it",
    "Right part and right tools physically present",
    "iFixit difficulty moderate or below",
    "The attendee does the work; you coach",
)
