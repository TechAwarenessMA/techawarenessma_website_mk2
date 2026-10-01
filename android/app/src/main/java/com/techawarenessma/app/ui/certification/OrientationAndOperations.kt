@file:OptIn(ExperimentalLayoutApi::class)

package com.techawarenessma.app.ui.certification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techawarenessma.app.certification.CertItems
import com.techawarenessma.app.links.Links
import com.techawarenessma.app.ui.LocalAppActions
import com.techawarenessma.app.ui.components.ArrowBullet
import com.techawarenessma.app.ui.components.BigStat
import com.techawarenessma.app.ui.components.OutlineChip
import com.techawarenessma.app.ui.components.PillButton
import com.techawarenessma.app.ui.components.RichText
import com.techawarenessma.app.ui.components.TagPill
import com.techawarenessma.app.ui.components.ToggleTile
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType

// ------------------------------------------------------------------ Orientation

@Composable
internal fun HowToUseSection() {
    Kicker("Orientation · Plan on 3 to 4 weeks, part-time")
    CourseTitle("This isn't reading material.\nIt's a course")
    Body("Five modules, each with a checkpoint you have to actually do, then a written knowledge check and a certification submission at the end. TAA is small and growing fast — we can't personally train every chapter lead, and we're not going to pretend we can. So we wrote down everything we know and built a way to check that you actually learned it.")
    Body("Steps 4 and 5 are real gates, and yes, we do turn people away at them. Not because we enjoy being difficult. It's the reason a library trusts a stranger in their building under our name.")
    Callout("Don't rush it") {
        RichText("If you try to knock this out in a weekend, you'll fail the certification submission. The hands-on parts need you to take devices apart and put them back together over and over — that takes days, not hours.", style = TaaType.Body)
    }
    SubHeading("Budget your time")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 16.dp)) {
        listOf("Part 1 · 45m", "Module 1 · 4h", "Module 2 · 7h", "Module 3 · 4h", "Module 4 · 3h", "Module 5 · 3h", "Parts 8+9 · 3h").forEach { OutlineChip(it) }
    }
    Body("The path on the course overview is the whole program — Application through Full Charter. It fills in as you go. Start with **Part 1 · What TAA is**, right after this.")
}

@Composable
internal fun WhatTaaIsSection() {
    Kicker("Part 1 · 45 minutes reading")
    CourseTitle("What Tech Awareness\nAssociation is")
    Body("We make technology repairable and understandable for the people the tech industry stopped designing for. We do two things, in public, for free:")
    Callout("01 · Device repair", accent = TaaColors.Blue) {
        RichText("People open up, diagnose, and fix their own phones, laptops, tablets, and watches. Their own hands, real tools.", style = TaaType.Body)
    }
    Callout("02 · Digital literacy", accent = TaaColors.Blue) {
        RichText("People learn to use the devices they already own without fear: settings, storage, backups, passwords, and how to spot a scam.", style = TaaType.Body)
    }
    Body("TAA started with a rejection. Our founder pitched a device-repair club at school and got turned down. Turns out the idea never needed a school — it needed a room, a table, some tools, and people who wanted to learn, and public libraries have all four. Nobody handed TAA a budget or permission. Your chapter gets built the same way.", Modifier.padding(top = 8.dp))
    SubHeading("What we believe")
    Text("Five commitments, not decoration", style = TaaType.Small, modifier = Modifier.alpha(0.7f).padding(bottom = 8.dp))
    Rule("01 · A device you cannot open is a device you do not own.", "Manufacturers spent twenty years making repair harder: glued batteries, weird screws, serialized parts. That's power moving away from ordinary people, and we teach repair as a way of taking some of it back.")
    Rule("02 · The person does the repair. Not us.", "The hardest rule we have. If you leave a workshop having personally fixed twelve devices while twelve people watched, you failed. We're a teaching organization, not a repair counter.")
    Rule("03 · Failure is part of the lesson, and we plan for it.", "Someone will strip a screw. Someone will tear a ribbon cable. Good. That's what learning looks like, and it's why we practice on devices we own instead of people's real phones.")
    Rule("04 · Nobody gets condescended to.", "Our attendees ran businesses, taught school, wired houses, kept books. They're capable adults who were never shown this one particular thing. The second your tone slips into baby talk, you've lost them, and they won't come back.")
    Rule("05 · Broken things are a resource, not garbage.", "Around sixty million tons of e-waste gets generated worldwide every year, and only a fraction gets properly recycled. Every device a chapter keeps alive is one that never becomes trash, and every dead one is teaching material or spare parts.")
    Callout("Read this first", dark = true, modifier = Modifier.padding(top = 8.dp)) {
        RichText("If any of these five bugs you, this isn't the right organization for you. Better to figure that out now, on this page.", style = TaaType.Body)
    }
}

@Composable
internal fun WhatAChapterIsSection() {
    Kicker("Part 1 · Say it plainly. Never inflate it.")
    CourseTitle("Who backs us, and\nwhat a chapter is")
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(TaaColors.Ink).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            BigStat("4", "Towns with operating chapters", TaaColors.Yellow, Modifier.weight(1f))
            BigStat("~110", "Enterprise devices from Granite Gives Back", TaaColors.Cyan, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            BigStat("72+38", "SFF desktops + laptops, Dell and Lenovo", TaaColors.Coral, Modifier.weight(1f))
            BigStat("100", "Tested 8GB DDR4 modules for teaching upgrades", TaaColors.Yellow, Modifier.weight(1f))
        }
    }
    Body("You're not starting from zero. When you walk into a library, you represent an organization running in four towns, with corporate hardware from Granite Telecommunications' Granite Gives Back program and a curriculum that's been tested on real attendees. Our toolkits and required reading come from iFixit, the biggest open repair knowledge platform there is.", Modifier.padding(top = 18.dp))
    SubHeading("A Tech Awareness chapter")
    Text("Every word here is a requirement", style = TaaType.Small, modifier = Modifier.alpha(0.7f).padding(bottom = 8.dp))
    Rule("Recurring", "At least once a month. One workshop is an event, not a chapter.")
    Rule("Free", "Attendees never pay. No fee, no door donation, no materials charge.")
    Rule("Public", "Anyone can walk in. No screening, no membership, no age limit.")
    Rule("Fixed venue", "A library or senior center. Not your house, not a coffee shop, not a garage.")
    Rule("Certified lead", "That's you, after you pass Parts 8 and 9. Not before.")
    Rule("TAA name", "Our materials, our safety rules, honest representation. In exchange: the name, the toolkit, the hardware, the introduction.")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 12.dp)) {
        listOf("✗ Not a repair shop", "✗ Not a business", "✗ Not a personal brand", "✗ Not unsupervised")
            .forEach { OutlineChip(it, borderColor = TaaColors.Red, color = TaaColors.Red) }
    }
    SubHeading("Who runs TAA")
    listOf(
        Triple("RS", "Ronit Sharma", "Executive Director"),
        Triple("TM", "Tanay Mangal", "Programs & Education"),
        Triple("NA", "Nathan Anwin", "Operations & Logistics"),
        Triple("SG", "Suhaas Goluguri", "Outreach & Community"),
        Triple("EJ", "Ekansh Jain", "Finance & Impact"),
        Triple("SG", "Suhrit Ghosh", "Social Media Lead"),
    ).forEach { (initials, name, role) ->
        Row(
            Modifier.fillMaxWidth().padding(vertical = 6.dp).semantics(mergeDescendants = true) {},
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(Modifier.size(44.dp).background(TaaColors.Yellow, CircleShape), contentAlignment = Alignment.Center) {
                Text(initials, style = TaaType.Label, color = TaaColors.Ink)
            }
            Column {
                Text(name, style = TaaType.Title, color = TaaColors.Ink)
                Text(role, style = TaaType.Small, modifier = Modifier.alpha(0.75f))
            }
        }
    }
}

@Composable
internal fun RolesSection() {
    Kicker("Part 1 · Get to 3–5 people before your first workshop")
    CourseTitle("Chapter roles &\ncode of conduct")
    Rule("Chapter Lead", "The person on the hook for everything. Owns the venue relationship, the safety briefing, the inventory, the report, and the call to stop a repair.", kicker = "Certified · Parts 8+9")
    Rule("Co-Lead", "Your backup. Recruit one within ninety days. A one-person chapter is one bad week away from ending, and most dead chapters died exactly that way.", kicker = "Certified · Parts 8+9")
    Rule("Repair Instructor", "Runs one station of 2–4 attendees. Coaches through the escalation ladder: words first, pointing second, hands almost never.", kicker = "Modules 1, 2, 4, 5 + check")
    Rule("Digital Literacy Instructor", "The non-repair half of the mission, and often the most impactful seat in the room. A patient retiree or a librarian usually beats a hotshot technician here.", kicker = "Modules 1 (safety), 4, 5")
    Rule("Intake & Greeter", "The first face people see. Sign-in, acknowledgment form, \"what brought you in today?\", the queue, and the attendance count.", kicker = "Module 4 + safety briefing")
    Rule("Inventory Steward", "Counts tools in and out, tracks practice devices, keeps consumables stocked, handles battery storage and disposal. Chapters lose toolkits. It's embarrassing and expensive.", kicker = "Module 1 in full · meticulous")
    Rule("Library Liaison", "Someone on the inside at the venue who helps with booking, promotion, and the stuff you'd otherwise learn the hard way.", kicker = "Optional · venue staff")
    Callout("Rule of thumb", modifier = Modifier.padding(top = 8.dp)) {
        RichText("One person can hold two roles. Nobody should hold four. Recruit the way you got recruited: through the library, and through people who showed up and liked it.", style = TaaType.Body)
    }
    SubHeading("Code of conduct")
    Text("You agree to all ten as a condition of your charter", style = TaaType.Small, modifier = Modifier.alpha(0.7f).padding(bottom = 8.dp))
    listOf(
        "Never charge, solicit, or accept payment under the TAA name",
        "Never use a workshop to drum up business for anyone",
        "Run the safety briefing before anyone picks up a tool",
        "Follow the practice-device policy, no exceptions",
        "Stop any repair you believe has become unsafe",
        "Never claim partnerships, funding, or status TAA doesn't have",
        "Treat every attendee as a capable adult",
        "Report honestly, including the workshops that flopped",
        "Report injuries, damage, or serious incidents within 24 hours",
        "Never touch an attendee's accounts, photos, messages, or files",
    ).forEachIndexed { index, rule ->
        InlineRule("**${(index + 1).toString().padStart(2, '0')}** · $rule")
    }
    Callout("Point 10, plainly", dark = true, modifier = Modifier.padding(top = 8.dp)) {
        RichText("You'll be handed unlocked phones by people who trust you. Don't ask for a passcode you don't need. Don't take a device out of the room. Don't touch an account. If a repair needs the phone unlocked, the owner unlocks it and keeps their hands on it.", style = TaaType.Body)
    }
}

// ------------------------------------------------------------------ Operations

@Composable
internal fun Part7Section() {
    Kicker("Part 7 · The venue is your most valuable asset, and it's on loan")
    CourseTitle("Running\nyour chapter")
    SubHeading("The first ninety days", Modifier.padding(top = 0.dp))
    Callout("Days 1–30 · Certify and prepare", accent = TaaColors.Blue) {
        listOf(
            "Finish Modules 1–5, the knowledge check, the certification",
            "Confirm the venue: room, recurring date, tables, power, storage",
            "Inventory target: 3 desktops, 3 laptops, 6 phones minimum",
            "Check every donated desktop for a BIOS lock",
            "Buy the fireproof container; find your battery recycling point",
            "Build the referral list; post TAA-template flyers",
        ).forEach { ArrowBullet(it) }
    }
    Callout("Days 31–60 · First workshop + a second person", accent = TaaColors.Blue) {
        listOf(
            "Workshop 1: keep it small on purpose, 8–12 people, never thirty",
            "Report within seven days",
            "Find your Co-Lead: attendees who stayed late, venue volunteers. Start them on the handbook.",
            "Workshop 2",
        ).forEach { ArrowBullet(it) }
    }
    Callout("Days 61–90 · Stabilize", accent = TaaColors.Blue) {
        listOf(
            "Workshop 3, and provisional becomes full charter",
            "Co-Lead certified",
            "Recruit 2 Repair Instructors + 1 Greeter",
            "Get a recurring date on the venue's own calendar. \"Second Saturday every month\" gets attendance. \"Whenever we can book a room\" doesn't.",
        ).forEach { ArrowBullet(it) }
    }
    SubHeading("What the venue needs from you, in priority order")
    InlineRule("**1 · Predictability.** Same date, same room, same time. Can't make it? Tell them a week out, not the morning of.")
    InlineRule("**2 · Not being a problem.** Room cleaner than you found it, no overruns, no trip hazards.")
    InlineRule("**3 · Attendance numbers, in writing, without being asked.** Libraries justify their budgets with attendance. This one habit makes you their favorite program.")
    InlineRule("**4 · No liability surprises, no commerce.** \"Will you break patrons' property?\" The practice-device policy is your answer; say it out loud. And nothing gets sold, ever.")
    SubHeading("Volunteers, devices, reporting")
    Rule("Recruiting volunteers", "Your best source is your own attendees — especially anyone who starts helping the person next to them without being asked. Recruit those people on the spot. Also: retired engineers and technicians, students who need service hours (they need the most Module 4 coaching), the library's volunteer pool, makerspaces, radio clubs. Nobody works a station alone on their first workshop. Nobody handles batteries without Module 1. Check whether your venue requires background checks — it's their process, not yours to waive.")
    Rule("Sourcing practice devices", "Ask your attendees — the single best source; every household has dead phones in a drawer. TAA's donated inventory (ask; logistics vary). Library and town IT retirements. Local repair shops' scrap bins, which often becomes a referral relationship both ways. E-waste events (ask the organizer first). **Wipe every donated device or pull the drive** before it becomes teaching material — skip this and you'll eventually hand someone a laptop with a stranger's tax returns on it.")
    Rule("Reporting", "Post-workshop report within seven days, every workshop. Ten minutes, and it's a charter condition — it builds the dataset that proves the program works. Report the bad ones too: a session where four people showed up and a repair failed is more useful than a session you never wrote up. **Within 24 hours:** any injury, property damage, battery event, data or privacy incident, venue complaint, or accusation against a volunteer.")
    Column(
        Modifier.fillMaxWidth().padding(top = 12.dp).clip(RoundedCornerShape(18.dp)).background(TaaColors.Ink).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("50", style = TaaType.Hero, color = TaaColors.Yellow)
        RichText(
            "What \"fifty chapters\" actually means: a chapter counts once it's run three workshops and filed three reports. Not when someone gets approved, not when a toolkit ships. Thirty inactive chapters is worse than twelve active ones. **Your job isn't to exist. It's to run a workshop every month.** If you're running out of steam, tell us; a chapter that asks for help is one we can help. A chapter that goes quiet is one we lose.",
            style = TaaType.Body,
            color = TaaColors.Cream,
        )
    }
}

@Composable
internal fun Part8Section() {
    val actions = LocalAppActions.current
    Kicker("Part 8 · Operations")
    CourseTitle("The knowledge\ncheck")
    TagPill("Gap — content lives in the full handbook", TaaColors.Red, color = TaaColors.Cream, modifier = Modifier.padding(bottom = 16.dp))
    Body("The knowledge check is **68 written questions covering all five modules**, answered in your own words. This condensed field edition doesn't include them — they live in the full handbook, along with Appendices A–F (safety script, forms, checklists, referral template, glossary).")
    Body("Per the build spec: this section gets built from the complete handbook, not from 68 invented placeholder questions. Request the full handbook from TAA before this module goes live.")
    PillButton(
        "Request the full handbook →",
        onClick = { actions.email(Links.CONTACT_EMAIL, subject = "Full handbook request (Part 8 + appendices)") },
    )
}

@Composable
internal fun Part9Section(readyItems: Set<String>, onToggle: (String, Boolean) -> Unit) {
    val readyCount = CertItems.count { it.id in readyItems }
    Kicker("Part 9 · The gate is real, and passing it means something")
    CourseTitle("Certification\nsubmission")
    RichText(
        "Submit all six items together. Track your readiness here — **$readyCount of 6 ready.**",
        style = TaaType.Body,
        modifier = Modifier.padding(bottom = 14.dp).semantics { liveRegion = LiveRegionMode.Polite },
    )
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CertItems.forEach { item ->
            val ready = item.id in readyItems
            ToggleTile(checked = ready, onCheckedChange = { onToggle(item.id, it) }) {
                Box(
                    Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (ready) TaaColors.Ink else TaaColors.Cream)
                        .border(2.dp, TaaColors.Ink, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    if (ready) Text("✓", style = TaaType.Label, color = TaaColors.Cream)
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(item.title, style = TaaType.Title.copy(fontSize = 15.sp), color = TaaColors.Ink)
                    Text(item.detail, style = TaaType.Small)
                }
            }
        }
    }
    Body("**Also required:** at least six completed iFixit guides across four device classes, tracked on your account, plus the foundation reading — from Tools Best Practices to the swollen-battery wiki.", Modifier.padding(top = 16.dp))
    SubHeading("TAA sends back one of four results")
    Rule("Certified", "Charter issued, provisional status. Schedule workshop 1.")
    Rule("Certified with notes", "Approved, with specific fixes we read back to you before workshop 1.")
    Rule("Resubmit", "Common, and not a rejection. It's usually the mock lesson; the escalation ladder is genuinely hard.")
    Rule("Not approved", "We'll tell you exactly why, including when it's a fit issue instead of a skill issue.")
}
