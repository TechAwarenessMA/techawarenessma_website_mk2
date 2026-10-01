@file:OptIn(ExperimentalLayoutApi::class)

package com.techawarenessma.app.ui.certification

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techawarenessma.app.certification.DELL_OPTIPLEX_GUIDE
import com.techawarenessma.app.certification.DeviceClasses
import com.techawarenessma.app.certification.EscalationLadder
import com.techawarenessma.app.certification.KitTools
import com.techawarenessma.app.certification.LanguageCards
import com.techawarenessma.app.certification.MatMethodSteps
import com.techawarenessma.app.certification.RepairSequence
import com.techawarenessma.app.certification.RunOfShow
import com.techawarenessma.app.certification.RungZone
import com.techawarenessma.app.certification.Tier3Conditions
import com.techawarenessma.app.certification.VideoAssignments
import com.techawarenessma.app.certification.WrittenGuides
import com.techawarenessma.app.links.Links
import com.techawarenessma.app.ui.LocalAppActions
import com.techawarenessma.app.ui.components.ArrowBullet
import com.techawarenessma.app.ui.components.LineIcon
import com.techawarenessma.app.ui.components.OutlineChip
import com.techawarenessma.app.ui.components.RichText
import com.techawarenessma.app.ui.components.SelectableTile
import com.techawarenessma.app.ui.components.TagPill
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType

// ------------------------------------------------------------------ Module 1

@Composable
internal fun Module1Section() {
    Kicker("Module 1 · 2h reading + 2h hands-on")
    CourseTitle("Tools, workbench,\nand safety")
    Body("Every serious repair failure comes down to one of three things: **the wrong tool, a lost screw, or a battery.** This module handles all three.")

    SubHeading("The kit · iFixit Pro Tech Toolkit")
    Body("Tap a tool. Every card tells you what it's for — and when **not** to reach for it.")
    ToolPicker()
    Body("**Buy separately:** 91%+ isopropyl, lint-free swabs, nitrile gloves, a good light, adhesive strips, a multimeter, and a fireproof battery container. **Bits you'll actually meet:** PH000–PH1 · P2/P5 Pentalobe · Y000 tri-point · T2–T8 Torx · JIS (strips if you use a Phillips bit on it).", Modifier.padding(top = 16.dp))

    SubHeading("Screw discipline · The Mat Method")
    Body("Walk the five steps. This is the checkpoint skill for Module 1.")
    MatMethod()
    Body("A screw one millimeter too long, driven into the wrong hole, can punch straight through a logic board. **The mat gets used or the repair doesn't start.** Hand attendees the marker; drawing the outline turns them from a passenger into the person running the process.", Modifier.padding(top = 16.dp))

    SubHeading("ESD · The damage you can't feel")
    Body("You're carrying a static charge you can't feel, and a zap way below anything you'd notice can quietly damage a chip. The damage often shows up later: the device works today and dies in three weeks. In a teaching setting, that's the worst possible outcome.")
    listOf(
        "Wrist strap on, clipped to unpainted metal, before the case opens",
        "Touch grounded metal before handling any board",
        "No wool, fleece, or shuffling around on carpet in socks",
        "Boards go into antistatic bags or onto the mat, never onto fabric",
        "Metal spudger and Jimmy are **not** ESD safe; nylon and ESD tweezers are",
    ).forEach { ArrowBullet(it) }

    Kicker("Module 1.5 · Read this twice. Then again before your first workshop.", Modifier.padding(top = 28.dp))
    SubHeading("Battery safety: no exceptions, ever", Modifier.padding(top = 0.dp))
    Body("Puncture a lithium cell and it can vent superheated gas and catch fire. **This is the one thing in the whole curriculum that can put someone in a hospital.**")
    listOf(
        "**1 · Below 25% charge** before any repair near a battery. iFixit tested this: a punctured cell at ~25% smoked and sparked but never hit thermal runaway. A full cell is a completely different event.",
        "**2 · Plastic tools only near a battery.** Always. No metal spudger, no Jimmy, no screwdriver, no knife.",
        "**3 · Never pry against the face of a cell.** Pull the tabs, use isopropyl, use gentle heat, be patient. A battery that won't come out is information, not a challenge.",
        "**4 · A swollen battery ends the repair.** Bulging cover, lifting screen, a device that won't sit flat. Full procedure below.",
        "**5 · Punctured cell? Everyone backs away.** Forget the repair and the data. No water. Don't breathe the gas. Fire beyond a quick spark means evacuate and pull the alarm.",
        "**6 · Batteries never go in the trash.** Tape the terminals, into the container, off to a real battery recycling drop-off. Lots of libraries already have one. Ask.",
        "**7 · You are the last word.** An attendee may push you to keep going. Say no. This is the one spot in this handbook where you overrule the person who owns the device.",
    ).forEach { InlineRule(it) }
    Callout("The swollen battery procedure, every single time", dark = true, modifier = Modifier.padding(top = 12.dp)) {
        listOf(
            "Stop. Tools down.",
            "Don't press, bend, flex, or pry the device.",
            "Power it off. Don't charge it.",
            "Into the fireproof container.",
            "Tell the attendee straight: it's a fire risk, don't use or charge it, it needs a professional. Hand them the referral list.",
            "Log it in the post-workshop report.",
        ).forEachIndexed { index, step ->
            Row(Modifier.padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.size(26.dp).background(TaaColors.Coral, CircleShape), contentAlignment = Alignment.Center) {
                    Text("${index + 1}", style = TaaType.Label, color = TaaColors.Ink)
                }
                RichText(step, style = TaaType.Body)
            }
        }
    }
    Body("You'll feel like you're letting them down. You're not. You're the person in the room who knew what they were looking at.", Modifier.padding(top = 8.dp))
    Body("Required at every workshop: fireproof container with a lid · nitrile gloves · know where the extinguisher is · a room you can clear fast. **No container, no battery work.**")
}

@Composable
private fun ToolPicker() {
    var selectedId by rememberSaveable { mutableStateOf(KitTools.first().id) }
    val tool = KitTools.first { it.id == selectedId }
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(TaaColors.Sand)
            .padding(18.dp)
            .semantics { liveRegion = LiveRegionMode.Polite },
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(tool.name, style = TaaType.CardTitle, color = TaaColors.Ink, modifier = Modifier.weight(1f, fill = false))
            TagPill(tool.qty, TaaColors.Cream)
        }
        RichText("**For:** ${tool.useFor}", style = TaaType.Small)
        RichText("**Not for:** ${tool.notFor}", style = TaaType.Small)
    }
    Column(Modifier.padding(top = 12.dp).selectableGroup(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        KitTools.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { item ->
                    SelectableTile(
                        selected = item.id == selectedId,
                        onClick = { selectedId = item.id },
                        modifier = Modifier.weight(1f).heightIn(min = 96.dp),
                        contentPadding = PaddingValues(8.dp),
                    ) {
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            LineIcon(item.iconPath, tint = TaaColors.Slate, size = 26.dp)
                            Text(item.name, style = TaaType.Small.copy(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold), textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MatMethod() {
    var step by rememberSaveable { mutableIntStateOf(0) }
    Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        MatMethodSteps.forEachIndexed { index, item ->
            SelectableTile(selected = index == step, onClick = { step = index }, modifier = Modifier.fillMaxWidth()) {
                NumberBadge("${index + 1}", selected = index == step)
                Text(item.title, style = TaaType.Small.copy(fontWeight = FontWeight.SemiBold), modifier = Modifier.weight(1f))
            }
        }
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(TaaColors.Ink)
            .padding(18.dp)
            .semantics { liveRegion = LiveRegionMode.Polite },
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text("STEP ${step + 1} OF 5", style = TaaType.Label.copy(letterSpacing = 1.4.sp), color = TaaColors.Yellow)
        Text(MatMethodSteps[step].detail, style = TaaType.Body, color = TaaColors.Cream)
    }
}

@Composable
private fun NumberBadge(text: String, selected: Boolean) {
    Box(
        Modifier.size(30.dp).background(if (selected) TaaColors.Blue else TaaColors.Sand, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, style = TaaType.Label, color = if (selected) TaaColors.Cream else TaaColors.Ink)
    }
}

// ------------------------------------------------------------------ Module 2

@Composable
internal fun Module2Section() {
    Kicker("Module 2 · 3h reading + 4h hands-on · Method beats memorization")
    CourseTitle("The universal\nrepair sequence")
    Body("Every repair on every device follows the same nine steps. Teach the sequence itself — it's worth more than any single repair, because it transfers to devices we never covered. Tap each step for the real detail.")
    RepairSequenceAccordion()
    Quote("1/3 of \"broken\" devices are actually software: full storage, a failed update, a rogue app, a setting. Always rule out software before you open anything.", Modifier.padding(top = 20.dp))
    Body("The highest satisfaction-per-minute repair in existence: a plug of pocket lint packed into the charging port. Wooden toothpick, gentle scraping, phone off, no metal. It's the perfect first win for a nervous attendee.", Modifier.padding(top = 8.dp))

    Kicker("Module 2.3–2.8 · A few solid repairs + the judgment to stop", Modifier.padding(top = 28.dp))
    SubHeading("What's realistic, by device class", Modifier.padding(top = 0.dp))
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DeviceClasses.forEach { device ->
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(TaaColors.Sand).padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    LineIcon(device.iconPath, tint = TaaColors.Slate, size = 30.dp)
                    Column {
                        Text(device.name, style = TaaType.Title, color = TaaColors.Ink, modifier = Modifier.semantics { heading() })
                        Text(device.tagline, style = TaaType.Small, modifier = Modifier.alpha(0.75f))
                    }
                }
                device.can.forEach { Text("✓ $it", style = TaaType.Small) }
                Text("✗ ${device.cannot}", style = TaaType.Small.copy(fontWeight = FontWeight.SemiBold), color = TaaColors.Red)
            }
        }
    }
    Callout("Stop and refer when", dark = true, modifier = Modifier.padding(top = 12.dp)) {
        RichText(
            "Swollen battery · water damage · soldering required · a serialized part that breaks a feature they rely on · top-tier iFixit difficulty · unbacked-up data at risk · missing tool or part · repair costs close to replacement · your gut says stop. **If you feel out of your depth, you are.** Saying \"this is past what I can safely do here, and here's who can\" is what competence sounds like.",
            style = TaaType.Body,
        )
    }
}

/** One step open at a time, first step open by default — as on the website. */
@Composable
private fun RepairSequenceAccordion() {
    var open by rememberSaveable { mutableIntStateOf(0) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        RepairSequence.forEachIndexed { index, step ->
            val expanded = open == index
            val shape = RoundedCornerShape(16.dp)
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(if (expanded) TaaColors.Sand else TaaColors.Cream)
                    .border(2.dp, if (expanded) TaaColors.Blue else TaaColors.Sand, shape)
                    .clickable(role = Role.Button, onClickLabel = if (expanded) "Collapse" else "Expand") {
                        open = if (expanded) -1 else index
                    }
                    .semantics { stateDescription = if (expanded) "Expanded" else "Collapsed" }
                    .animateContentSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text((index + 1).toString().padStart(2, '0'), style = TaaType.Small.copy(fontWeight = FontWeight.Bold, fontFeatureSettings = "tnum"), color = TaaColors.Blue, modifier = Modifier.widthIn(min = 22.dp))
                    Text(step.title, style = TaaType.Title.copy(fontSize = 16.sp), color = TaaColors.Ink, modifier = Modifier.weight(1f))
                    Text(if (expanded) "−" else "+", style = TaaType.CardTitle, color = TaaColors.Slate)
                }
                if (expanded) {
                    Text(step.detail, style = TaaType.Body, modifier = Modifier.padding(top = 10.dp))
                }
            }
        }
    }
}

// ------------------------------------------------------------------ Module 3

@Composable
internal fun Module3Section() {
    val actions = LocalAppActions.current
    Kicker("Module 3 · About 4 hours of video · Written guides teach sequence. Video teaches hands.")
    CourseTitle("Required viewing")
    Callout("The active viewing protocol", accent = TaaColors.Blue) {
        listOf(
            "1 · Watch once at normal speed, hands empty, phone away.",
            "2 · Watch again with a practice device in front of you, pausing at every step and doing it.",
            "3 · Write down every warning. A warning is the compressed experience of someone who already broke one.",
            "4 · Study how they talk. The good ones narrate their reasoning, not just their actions — exactly the style you'll use at a workshop table.",
            "5 · Watch their recoveries. Someone calmly fixing a mistake on camera is the most valuable footage in the whole video.",
        ).forEach { Text(it, style = TaaType.Body, modifier = Modifier.padding(vertical = 2.dp)) }
    }

    SubHeading("The assignments — real links, verified")
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        VideoAssignments.forEach { video ->
            val url = video.url
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(TaaColors.Sand)
                    .then(
                        if (url != null) {
                            Modifier.clickable(role = Role.Button, onClickLabel = "Watch on YouTube") { actions.openUrl(url) }
                        } else {
                            Modifier
                        },
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(video.channel.uppercase(), style = TaaType.Label, color = TaaColors.Blue)
                if (url != null) {
                    Text("${video.title} ↗", style = TaaType.Title.copy(fontSize = 15.sp), color = TaaColors.Ink)
                } else {
                    Text(video.title, style = TaaType.Title.copy(fontSize = 15.sp), color = TaaColors.Ink)
                    video.note?.let { RichText(it, style = TaaType.Small) }
                }
                TagPill(video.kind, if (url == null) TaaColors.Coral else TaaColors.Cream)
            }
        }
    }
    Body("**Watch several channels; one presenter's habits aren't the whole craft.** JerryRigEverything: watch critically — it's not a model for how to handle an attendee's device. Rossmann: you're not learning to microsolder, you're learning to think like a technician.", Modifier.padding(top = 16.dp))

    SubHeading("Written guides — matched to TAA's actual inventory")
    Body("Primary mechanism: [iFixit's device search ↗](${Links.IFIXIT_GUIDES}) — inventory varies by chapter and changes over time. Worked examples for the lesson itself, from the Granite batch's **Lenovo ThinkPad E14 Gen 4**:")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 12.dp)) {
        WrittenGuides.forEach { (label, url) ->
            OutlineChip(
                "$label ↗",
                modifier = Modifier.clip(CircleShape).clickable(role = Role.Button) { actions.openUrl(url) },
            )
        }
    }
    Callout("Gap — Dell OptiPlex 5090 / 5080") {
        RichText(
            "iFixit has no guides for these exact models. Working reference: [Dell's official OptiPlex 5080 CRU teardown guide ↗]($DELL_OPTIPLEX_GUIDE). And teach the lookup itself: \"iFixit first, manufacturer's service manual if it's not there\" is a real skill, not a workaround to hide.",
            style = TaaType.Body,
        )
    }
    Callout("Before you move on, you should be able to", dark = true) {
        RichText(
            "Describe how much force a pick takes under a glued screen · tell adhesive releasing apart from glass starting to crack · narrate a repair out loud · explain serialization to a non-technical person in under a minute · recover from a mistake without visibly panicking.",
            style = TaaType.Body,
        )
    }
}

// ------------------------------------------------------------------ Module 4

@Composable
internal fun Module4Section() {
    Kicker("Module 4 · 2h reading + 1h practice · The most skilled leads who fail, fail here")
    CourseTitle("Teaching\nolder adults")
    Body("None of this is about capability. Think of it as accommodation, the same way a lecture hall has a microphone.")
    Rule("Vision", "Print handouts at 14pt or bigger. Task lighting at every station. Bump up the device's own text size first — and do it with them so they can do it again at home.")
    Rule("Hearing", "Face the person when you talk. Lower your pitch instead of raising your volume; shouting raises pitch and makes it worse. Never talk from behind someone.")
    Rule("Fine motor", "Tremor and arthritis are common, and people usually don't mention them. Reverse tweezers hold without grip. Stable surface, elbow support, both hands braced. Never rush a hand.")
    Rule("Memory", "New sequences take longer; judgment is fine. One step at a time, written steps to take home, and explain why each step exists. People hold onto reasons way better than sequences.")
    Rule("Anxiety", "Twenty years of being told they're \"bad with computers.\" Some have broken something and been blamed for it. It's the biggest barrier in the room, and it's entirely psychological.")
    Callout("The discussion-based model. TAA does not lecture. Ever.", dark = true, modifier = Modifier.padding(top = 12.dp)) {
        listOf(
            "Open with their problem, not your topic. \"What's the thing about your phone that annoys you most? Go around.\" Everyone talks once in the first five minutes, and now you have the room's real agenda.",
            "Ask, don't tell. \"Why do you think the manufacturer glued this in instead of using clips?\" starts a better right-to-repair conversation than any speech.",
            "Teach toward what they said. If four people said \"storage full,\" that's your lead topic today, whatever your plan was.",
            "Let them answer each other. An explanation from a peer lands harder than one from an expert — and it turns attendees into volunteers.",
            "Close with commitments: \"what are you going to try this week?\"",
        ).forEach { ArrowBullet(it, arrowColor = TaaColors.Yellow) }
    }

    Kicker("Module 4.3–4.5 · Small wording changes decide who comes back", Modifier.padding(top = 28.dp))
    SubHeading("Language rules", Modifier.padding(top = 0.dp))
    Body("Active recall, not reading: for each card, decide what you'd say instead — then reveal.")
    LanguageFlashCards()
    Body("**\"Just\" is the most common condescension in tech teaching.** \"You just go to settings\" tells a struggling person that the thing beating them is trivial. Cut it from your vocabulary at the table.", Modifier.padding(top = 16.dp))

    SubHeading("The hard moments")
    InlineRule("**\"I'm too old for this.\"** Don't argue and don't over-reassure; both feel dismissive. Point at the evidence: \"You've used this phone for three years without anyone showing you how. That's not someone who can't learn. That's someone nobody ever taught.\"")
    InlineRule("**The person who was scammed.** Stop what you're doing. No alarm, and never \"how did you fall for that.\" These scams are built by professionals to be convincing. Help them change the password and turn on two-factor — their hands on the phone, not yours — and give them reportfraud.ftc.gov.")
    InlineRule("**The dominant talker.** \"Good, hold that thought, I want to come back to it. Margaret, what did you find?\" Then call on the quiet people by name. And consider recruiting the talker; that energy is useful in the right seat.")
    InlineRule("**The person who came for something else.** Loneliness, or a family that stopped answering tech questions. That's legitimate. The digital literacy track exists partly for this, and a chapter that only counts repairs misses most of its impact.")
    Callout("What never happens at a TAA workshop", modifier = Modifier.padding(top = 8.dp)) {
        RichText(
            "Nobody gets laughed at · no question gets treated as obvious · no device gets taken without asking (\"May I?\", and hand it back screen facing them) · nobody gets told they're too old · no talking about attendees where they can hear · no accounts, photos, or messages get opened · no passcode gets written down · no device leaves the room.",
            style = TaaType.Body,
        )
    }
}

@Composable
private fun LanguageFlashCards() {
    // Revealed card indexes; a card stays revealed once flipped, as on the site.
    var revealed by rememberSaveable { mutableStateOf(emptyList<Int>()) }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        LanguageCards.forEachIndexed { index, card ->
            val shown = index in revealed
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(TaaColors.Sand)
                    .animateContentSize()
                    .padding(16.dp)
                    .semantics { liveRegion = LiveRegionMode.Polite },
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text("DON'T SAY", style = TaaType.Label, color = TaaColors.Red)
                Text("\"${card.dont}\"", style = TaaType.Title, color = TaaColors.Ink)
                if (shown) {
                    Text("SAY INSTEAD", style = TaaType.Label, color = TaaColors.Blue, modifier = Modifier.padding(top = 6.dp))
                    Text(card.say, style = TaaType.Body)
                } else {
                    Box(
                        Modifier
                            .heightIn(min = 44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(role = Role.Button) { revealed = revealed + index },
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text("Reveal the fix →", style = TaaType.Button.copy(fontSize = 14.sp), color = TaaColors.Ink)
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------ Module 5

@Composable
internal fun Module5Section() {
    Kicker("Module 5 · 2h reading + 1h practice · Keeping your hands off other people's work")
    CourseTitle("The escalation\nladder")
    Body("Someone who watched you replace a battery learned that battery replacement is *possible*. Someone who replaced one themselves — badly, slowly, with two mistakes and one small panic — learned that *they* can replace a battery. Only the second one changes what they do after they go home. Climb the ladder one rung at a time; tap each rung.")
    Ladder()
    Callout("The failure mode to watch for", modifier = Modifier.padding(top = 12.dp)) {
        RichText("**Jumping straight to rung 7 because it's faster.** It is faster. It's also the whole reason a workshop can feel busy and teach nobody anything.", style = TaaType.Body)
    }
    Callout("\"Just fix it for me\"", dark = true) {
        RichText("\"I could do that in five minutes, but then next time you'd be right back here. Give me twenty and you'll be able to do it yourself.\" The line is capability, not willingness. Someone who physically can't hold tweezers gets help, narrated clearly, with written steps to take home.", style = TaaType.Body)
    }

    Kicker("Module 5.3–5.5 · A two-hour session. Adapt it, but keep the shape.", Modifier.padding(top = 28.dp))
    SubHeading("Run-of-show & the practice-device policy", Modifier.padding(top = 0.dp))
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(TaaColors.Sand).padding(vertical = 6.dp)) {
        RunOfShow.forEach { (time, what) ->
            Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    time,
                    style = TaaType.Small.copy(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold),
                    color = TaaColors.Blue,
                    modifier = Modifier.widthIn(min = 44.dp),
                )
                RichText(what, style = TaaType.Small)
            }
        }
    }
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 14.dp)) {
        listOf("2–4 attendees / instructor", "1 device / 1–2 attendees", "Group by device, not skill", "Nervous first-timers → desktops").forEach { OutlineChip(it) }
    }
    Rule("Tier 1 · Practice devices — full hands-on, always", "Donated and scrap hardware the chapter owns. Attendees can take it apart, break it, and do it again, freely. All the invasive teaching happens here. Nobody's data, nobody's property, nobody's grief.")
    Rule("Tier 2 · Attendee devices — non-invasive only", "Software, settings, backups, 2FA setup · battery health readings · external cleaning · port cleaning with a non-metal tool, powered off · diagnosis and a written recommendation.")
    Callout("Tier 3 · Opening an attendee's device — all eight, or it's Tier 2 + referral", dark = true, modifier = Modifier.padding(top = 6.dp)) {
        Tier3Conditions.forEachIndexed { index, condition ->
            Text("${index + 1} · $condition", style = TaaType.Body, modifier = Modifier.padding(vertical = 2.dp))
        }
    }
    Body("**Don't negotiate with yourself on this.** The pressure to say yes will come from a nice person who really wants their phone fixed. That's exactly the situation the rule exists for.", Modifier.padding(top = 8.dp))
    Body("Why this exists, said plainly: a chapter lead is a volunteer who read a handbook. Fifty of those in fifty towns opening strangers' phones on demand gets you bricked devices, lost family photos, and eventually an injury. Practice devices remove nearly all of that risk and almost none of the learning. That's a rare trade, and we take it.")
}

@Composable
private fun Ladder() {
    var selected by rememberSaveable { mutableIntStateOf(0) }
    Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        EscalationLadder.forEachIndexed { index, rung ->
            val background = when (rung.zone) {
                RungZone.Low -> TaaColors.Sand
                RungZone.Escalation -> TaaColors.Yellow
                RungZone.Rare -> TaaColors.Red
            }
            val content = if (rung.zone == RungZone.Rare) TaaColors.Cream else TaaColors.Ink
            SelectableTile(
                selected = index == selected,
                onClick = { selected = index },
                modifier = Modifier.fillMaxWidth(),
                background = background,
                selectedBackground = background,
                idleBorder = background,
                selectedRing = TaaColors.Blue,
            ) {
                Text("R${index + 1}", style = TaaType.Label, color = content, modifier = Modifier.widthIn(min = 24.dp))
                Text(rung.title, style = TaaType.Title.copy(fontSize = 15.sp), color = content, modifier = Modifier.weight(1f))
                Text(rung.zone.label.uppercase(), style = TaaType.Label.copy(fontSize = 9.sp), color = content)
            }
        }
    }
    val rung = EscalationLadder[selected]
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(TaaColors.Ink)
            .padding(18.dp)
            .semantics { liveRegion = LiveRegionMode.Polite },
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text("RUNG ${selected + 1} · ${rung.title.uppercase()}", style = TaaType.Label.copy(letterSpacing = 1.4.sp), color = TaaColors.Yellow)
        Text(rung.detail, style = TaaType.Body, color = TaaColors.Cream)
    }
}
