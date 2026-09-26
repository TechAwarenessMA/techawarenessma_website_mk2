package com.techawarenessma.app.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techawarenessma.app.R
import com.techawarenessma.app.content.CurriculumWeeks
import com.techawarenessma.app.links.Links
import com.techawarenessma.app.navigation.Routes
import com.techawarenessma.app.ui.LocalAppActions
import com.techawarenessma.app.ui.components.ArrowLink
import com.techawarenessma.app.ui.components.Band
import com.techawarenessma.app.ui.components.BigStat
import com.techawarenessma.app.ui.components.Eyebrow
import com.techawarenessma.app.ui.components.FigureCaption
import com.techawarenessma.app.ui.components.Headline
import com.techawarenessma.app.ui.components.LoopingVideo
import com.techawarenessma.app.ui.components.NumberedLine
import com.techawarenessma.app.ui.components.Paragraph
import com.techawarenessma.app.ui.components.PhoneFrame
import com.techawarenessma.app.ui.components.Photo
import com.techawarenessma.app.ui.components.PillButton
import com.techawarenessma.app.ui.components.PillStyle
import com.techawarenessma.app.ui.components.RichText
import com.techawarenessma.app.ui.components.SandCard
import com.techawarenessma.app.ui.components.SplitHeadline
import com.techawarenessma.app.ui.components.TagPill
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType

/** Anchor ids match the website's section ids (`Programs.dc.html#seniors`). */
private val ProgramSections = listOf(
    "header", "workshops", "curriculum", "equipment", "seniors",
    "ewaste", "laptops", "shrewsbury-schools", "timelapse", "cta",
)

@Composable
fun ProgramsScreen(initialSection: String?) {
    val listState = rememberLazyListState()
    // Only jump once; after that the user's own scroll position wins (also across rotation).
    var jumped by rememberSaveable(initialSection) { mutableStateOf(false) }
    LaunchedEffect(initialSection) {
        val index = ProgramSections.indexOf(initialSection)
        if (!jumped && index > 0) listState.scrollToItem(index)
        jumped = true
    }
    LazyColumn(Modifier.fillMaxSize(), state = listState) {
        item(key = "header") { ProgramsHeader() }
        item(key = "workshops") { WorkshopsProgram() }
        item(key = "curriculum") { Curriculum() }
        item(key = "equipment") { Equipment() }
        item(key = "seniors") { SeniorsProgram() }
        item(key = "ewaste") { EWasteProgram() }
        item(key = "laptops") { LaptopProgram() }
        item(key = "shrewsbury-schools") { SchoolsProgram() }
        item(key = "timelapse") { ProgramsTimelapse() }
        item(key = "cta") { ProgramsCta() }
    }
}

@Composable
private fun ProgramsHeader() {
    Band(top = 20.dp, bottom = 32.dp) {
        Eyebrow("Programs & Workshops")
        SplitHeadline("Teach,", "don't fix-for.", accent = TaaColors.Red)
        Paragraph(
            "We don't take your phone behind a counter. You open it, you fix it, we sit next to you. Five programs, one rule.",
            style = TaaType.BodyLarge,
            modifier = Modifier.padding(top = 20.dp),
        )
    }
}

/** One numbered program: figure, pill, title, copy, and an optional link. */
@Composable
private fun Program(
    number: String,
    numberColor: Color,
    title: String,
    @DrawableRes photo: Int,
    photoAlt: String,
    caption: String?,
    content: @Composable () -> Unit,
) {
    Band(top = 28.dp, bottom = 28.dp) {
        Photo(photo, photoAlt, Modifier.fillMaxWidth().height(260.dp), shape = RoundedCornerShape(24.dp))
        if (caption != null) FigureCaption(caption)
        Spacer(Modifier.height(20.dp))
        TagPill(number, numberColor)
        Text(
            title.uppercase(),
            style = TaaType.Subsection,
            color = TaaColors.Ink,
            modifier = Modifier.padding(top = 14.dp, bottom = 14.dp).semantics { heading() },
        )
        content()
    }
}

@Composable
private fun WorkshopsProgram() {
    val actions = LocalAppActions.current
    Program("01", TaaColors.Yellow, "Weekly Repair Workshops", R.drawable.photo_tweezers, "A student lifting a component out of an open phone with tweezers", "Fig. 01 — Week 4, phone care") {
        Paragraph("A 12-week tech literacy curriculum that restarts every cycle, per chapter. Drop-in friendly — no signup required to attend, and you can join at any week. Each chapter's library runs its own signup form if you want a seat held.")
        Paragraph("You'll open real hardware from week one. By the end you can judge a repair-or-replace call, swap a battery, and spot an online scam from across the room.")
        ArrowLink("Find your chapter's schedule →", onClick = { actions.navigate(Routes.CHAPTERS) })
    }
}

@Composable
private fun Curriculum() {
    Band(background = TaaColors.Ink, contentColor = TaaColors.Cream, top = 48.dp, bottom = 40.dp) {
        Eyebrow("The curriculum", color = TaaColors.Yellow)
        Headline("12 weeks, then it restarts", color = TaaColors.Cream, punctuationColor = TaaColors.Yellow, modifier = Modifier.padding(bottom = 14.dp))
        Paragraph("Miss a week? Catch it next cycle. Every chapter runs the same loop.", modifier = Modifier.padding(bottom = 8.dp))
        CurriculumWeeks.forEachIndexed { index, week ->
            NumberedLine(
                number = (index + 1).toString().padStart(2, '0'),
                text = week,
                numberColor = TaaColors.Yellow,
                divider = TaaColors.Cream.copy(alpha = 0.14f),
            )
        }
    }
}

@Composable
private fun Equipment() {
    Band(top = 40.dp, bottom = 16.dp) {
        SandCard(spacing = 20.dp) {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                BigStat("30", "iFixit Pro Tech Toolkits", TaaColors.Gold, Modifier.weight(1f))
                BigStat("30", "Repair mats", TaaColors.Blue, Modifier.weight(1f))
                BigStat("110", "Devices from Granite", TaaColors.Red, Modifier.weight(1f))
            }
            Text(
                "Every seat gets real tools and a real machine. The Granite devices get repaired, taught on, and — when they're fixed — refurbished and given away.",
                style = TaaType.Body,
            )
        }
    }
}

@Composable
private fun SeniorsProgram() {
    val actions = LocalAppActions.current
    Program("02", TaaColors.Cyan, "Senior Tech Support", R.drawable.photo_senior_session, "One-on-one Senior Tech Support session", "Fig. 02 — One-on-one session") {
        Paragraph("One-on-one, currently at Southgate Senior Center. Bring the device that's been bugging you and the questions you've been embarrassed to ask. There are no dumb questions here — we've heard them all, and we asked most of them ourselves first.")
        Paragraph("Prefer to learn at home, at your own pace? We built [Susu](${Links.SUSU}) for exactly that — free, jargon-free, judgment-free.")
        ArrowLink("Southgate details →", onClick = { actions.navigate(Routes.CHAPTERS) })
    }
}

@Composable
private fun EWasteProgram() {
    Program("03", TaaColors.Coral, "E-Waste Drives", R.drawable.photo_ewaste, "Boxes of donated electronics at an outdoor e-waste drive", "Fig. 03 — Triage bench, refurb in progress") {
        Paragraph("Periodic collection days. Every device that comes in gets triaged into one of four tiers — nothing gets landfilled by default:")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                "T1" to "**Resell** — works as-is; proceeds fund the program",
                "T2" to "**Refurbish** — fixable; becomes teaching hardware or a giveaway",
                "T3" to "**Parts** — dead, but its screen and battery live on",
                "T4" to "**Recycle** — responsibly, as the last resort",
            ).forEach { (tier, text) ->
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(TaaColors.Sand).padding(horizontal = 18.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Text(tier, style = TaaType.Small.copy(fontWeight = FontWeight.Bold), color = TaaColors.Red, modifier = Modifier.widthIn(min = 24.dp).alignByBaseline())
                    RichText(text, style = TaaType.Body, modifier = Modifier.alignByBaseline())
                }
            }
        }
    }
}

@Composable
private fun LaptopProgram() {
    val actions = LocalAppActions.current
    Program("04", TaaColors.Gold, "Worcester Laptop Donation", R.drawable.photo_laptop_donation, "The open case of a donated desktop tower, showing its power supply, drives, and wiring", caption = null) {
        Paragraph("Refurbished laptops from our e-waste drives, given free to Worcester residents who need one and don't have one. No strings — just an application and a quick eligibility check.")
        Paragraph("Every machine we hand out has already passed through Tier 2 refurbishment: wiped, tested, and set up to go. Apply and we'll follow up about pickup.")
        ArrowLink("Apply for a laptop →", onClick = { actions.navigate(Routes.CONTACT) })
    }
}

@Composable
private fun SchoolsProgram() {
    val actions = LocalAppActions.current
    Program("05", TaaColors.Blue, "Shrewsbury School District Program", R.drawable.photo_workshop_table, "Kids working through toolkits and mats at a workshop table", caption = null) {
        Paragraph("In-school repair sessions at Sherwood Middle School and Oak Middle School, taught by our Shrewsbury chapter. Same rule as everywhere else: students open the device themselves.")
        Paragraph("Built for classroom schedules — shorter sessions, tied to school terms, run in partnership with district staff.")
        ArrowLink("Find your chapter's schedule →", onClick = { actions.navigate(Routes.CHAPTERS) })
    }
}

@Composable
private fun ProgramsTimelapse() {
    Band(background = TaaColors.Sand, top = 48.dp, bottom = 48.dp) {
        Eyebrow("Fig. 04 — From the bench")
        Headline("A full teardown, sped up", modifier = Modifier.padding(bottom = 16.dp))
        Paragraph("What twelve weeks builds toward: opening a device without fear. Straight from a real bench, sped up.")
        Spacer(Modifier.height(20.dp))
        PhoneFrame(Modifier.align(Alignment.CenterHorizontally)) {
            LoopingVideo(R.raw.timelapse_programs, contentDescription = "Timelapse of a workshop repair session", modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun ProgramsCta() {
    val actions = LocalAppActions.current
    Band(background = TaaColors.Espresso, contentColor = TaaColors.Cream, top = 48.dp, bottom = 48.dp) {
        Headline("Ready to open\nsomething", punctuation = "?", punctuationColor = TaaColors.Yellow, color = TaaColors.Cream, modifier = Modifier.padding(bottom = 24.dp))
        PillButton("Find your workshop →", onClick = { actions.navigate(Routes.CHAPTERS) }, style = PillStyle.Yellow)
    }
}
