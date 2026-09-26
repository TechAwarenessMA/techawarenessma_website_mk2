@file:OptIn(ExperimentalLayoutApi::class)

package com.techawarenessma.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techawarenessma.app.R
import com.techawarenessma.app.content.BenchTool
import com.techawarenessma.app.content.BenchTools
import com.techawarenessma.app.content.Chapters
import com.techawarenessma.app.content.ImpactStat
import com.techawarenessma.app.content.ImpactStats
import com.techawarenessma.app.content.chapterStatusColor
import com.techawarenessma.app.links.Links
import com.techawarenessma.app.navigation.Routes
import com.techawarenessma.app.ui.LocalAppActions
import com.techawarenessma.app.ui.components.ArrowLink
import com.techawarenessma.app.ui.components.Band
import com.techawarenessma.app.ui.components.DashedBox
import com.techawarenessma.app.ui.components.Eyebrow
import com.techawarenessma.app.ui.components.FigureCaption
import com.techawarenessma.app.ui.components.Headline
import com.techawarenessma.app.ui.components.LineIcon
import com.techawarenessma.app.ui.components.LoopingVideo
import com.techawarenessma.app.ui.components.NewsletterSignup
import com.techawarenessma.app.ui.components.OutlineChip
import com.techawarenessma.app.ui.components.Paragraph
import com.techawarenessma.app.ui.components.PhoneFrame
import com.techawarenessma.app.ui.components.Photo
import com.techawarenessma.app.ui.components.PillButton
import com.techawarenessma.app.ui.components.PillStyle
import com.techawarenessma.app.ui.components.RichText
import com.techawarenessma.app.ui.components.SandCard
import com.techawarenessma.app.ui.components.SelectableTile
import com.techawarenessma.app.ui.components.SiteFooter
import com.techawarenessma.app.ui.components.SplitHeadline
import com.techawarenessma.app.ui.components.TagPill
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType
import java.util.Locale
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun HomeScreen() {
    LazyColumn(Modifier.fillMaxSize()) {
        item(key = "hero") { HomeHero() }
        item(key = "stats") { ImpactStatsBand() }
        item(key = "what") { WhatWeDo() }
        item(key = "timelapse") { HomeTimelapse() }
        item(key = "mat") { ProMatWidget() }
        item(key = "partners") { HomePartners() }
        item(key = "chapters") { ChaptersTeaser() }
        item(key = "involved") { ThreeDoors() }
        item(key = "feed") { FromTheFeed() }
        item(key = "newsletter") { NewsletterCard() }
        item(key = "donate") { DonateStrip() }
        item(key = "footer") { SiteFooter(showNewsletter = false) }
    }
}

@Composable
private fun HomeHero() {
    val actions = LocalAppActions.current
    Band(top = 20.dp, bottom = 44.dp) {
        Eyebrow("Student-run 501(c)(3) · Shrewsbury, MA")
        SplitHeadline("We fix it", "together.", accent = TaaColors.Red, style = TaaType.Hero)
        Spacer(Modifier.height(24.dp))
        Paragraph(
            "We run free, hands-on workshops that teach people — especially seniors and first-time repairers — how to fix their own devices, stay safe online, and keep usable electronics out of landfills.",
            style = TaaType.BodyLarge,
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PillButton("Find your workshop", onClick = { actions.navigate(Routes.CHAPTERS) })
            PillButton("Donate", onClick = { actions.navigate(Routes.DONATE) }, style = PillStyle.Outline)
        }
        Spacer(Modifier.height(40.dp))
        Box(Modifier.fillMaxWidth().padding(top = 18.dp)) {
            // Decorative dots, placed like the site's absolutely positioned circles.
            Box(Modifier.align(Alignment.TopEnd).offset(x = 14.dp, y = (-18).dp).size(72.dp).background(TaaColors.Yellow, CircleShape))
            Box(Modifier.align(Alignment.BottomStart).offset(x = (-16).dp, y = (-40).dp).size(48.dp).border(8.dp, TaaColors.Cyan, CircleShape))
            Photo(
                R.drawable.photo_hero,
                contentDescription = "A student driving a screw out of an open iPhone on an iFixit mat",
                modifier = Modifier.fillMaxWidth().height(300.dp),
                shape = RoundedCornerShape(24.dp),
            )
        }
        FigureCaption("Fig. 01 — Battery swap, weekly workshop")
    }
}

private val EaseOutCubic = Easing { fraction -> 1f - (1f - fraction).pow(3) }

private fun formatCount(value: Int) = String.format(Locale.US, "%,d", value)

/**
 * "The impact so far". Like the site, the numbers count up once the section is 40% on
 * screen. With animations turned off system-wide the animation completes instantly.
 */
@Composable
private fun ImpactStatsBand() {
    var started by rememberSaveable { mutableStateOf(false) }
    val progress = remember { Animatable(if (started) 1f else 0f) }
    LaunchedEffect(started) {
        if (started) progress.animateTo(1f, tween(durationMillis = 1100, easing = EaseOutCubic))
    }
    Band(
        background = TaaColors.Ink,
        contentColor = TaaColors.Cream,
        top = 56.dp,
        bottom = 40.dp,
        modifier = Modifier.onGloballyPositioned { coordinates ->
            val height = coordinates.size.height
            if (!started && height > 0 && coordinates.boundsInWindow().height / height >= 0.4f) started = true
        },
    ) {
        Text(
            "The impact so far",
            style = TaaType.CardTitle.copy(fontSize = 26.sp, lineHeight = 32.sp),
            modifier = Modifier.padding(bottom = 36.dp).semantics { heading() },
        )
        ImpactStats.chunked(2).forEach { row ->
            Row(Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
                row.forEach { stat -> StatCell(stat, progress.value, Modifier.weight(1f)) }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StatCell(stat: ImpactStat, progress: Float, modifier: Modifier = Modifier) {
    val finalText = formatCount(stat.value) + stat.suffix
    val divider = TaaColors.Cream.copy(alpha = 0.14f)
    Column(
        modifier
            .drawBehind { drawLine(divider, Offset.Zero, Offset(0f, size.height), 1.dp.toPx()) }
            .padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
            // Screen readers always hear the real number, never a mid-animation value.
            .clearAndSetSemantics { contentDescription = "$finalText ${stat.label}" },
    ) {
        Text(formatCount((stat.value * progress).roundToInt()) + stat.suffix, style = TaaType.Stat, color = stat.color)
        Text(stat.label, style = TaaType.Small, modifier = Modifier.padding(top = 10.dp).alpha(0.65f))
    }
}

private data class WayIn(
    val number: String,
    val color: Color,
    val photo: Int,
    val photoAlt: String,
    val title: String,
    val body: String,
    val section: String,
)

private val WaysIn = listOf(
    WayIn("01", TaaColors.Yellow, R.drawable.photo_workshop_table, "Kids at a workshop table with iFixit toolkits", "Weekly Repair Workshops", "Drop in, pick up a driver, open something. A 12-week curriculum you can join any week — no signup needed to attend.", "workshops"),
    WayIn("02", TaaColors.Cyan, R.drawable.photo_parent_laptop, "A parent and student opening a laptop together", "Senior Tech Support", "One-on-one help at Southgate Senior Center. Your device, your questions, your pace. No judgment, ever.", "seniors"),
    WayIn("03", TaaColors.Coral, R.drawable.photo_ewaste, "Boxes of donated electronics at an outdoor e-waste drive", "E-Waste Drives", "Bring us your dead electronics. We triage every device: resell, refurbish, parts, or recycle. Nothing goes to waste by default.", "ewaste"),
)

@Composable
private fun WhatWeDo() {
    val actions = LocalAppActions.current
    Band(top = 56.dp, bottom = 32.dp) {
        Eyebrow("What we do")
        Headline("Three ways in", punctuationColor = TaaColors.Gold, modifier = Modifier.padding(bottom = 28.dp))
        WaysIn.forEach { way ->
            SandCard(padding = PaddingValues(16.dp), spacing = 14.dp, modifier = Modifier.padding(bottom = 20.dp)) {
                Photo(way.photo, way.photoAlt, Modifier.fillMaxWidth().height(200.dp))
                TagPill(way.number, way.color)
                Text(way.title, style = TaaType.CardTitle, modifier = Modifier.semantics { heading() })
                Text(way.body, style = TaaType.Body)
                ArrowLink("See how it works →", onClick = { actions.navigate(Routes.programs(way.section)) })
            }
        }
    }
}

@Composable
private fun HomeTimelapse() {
    Band(background = TaaColors.Sand, top = 56.dp, bottom = 56.dp) {
        Eyebrow("Fig. 02 — From the bench")
        Headline("Watch a repair happen", modifier = Modifier.padding(bottom = 16.dp))
        Paragraph("A real timelapse from our benches — a teardown, start to finish, inside the phone it happened to.")
        Spacer(Modifier.height(20.dp))
        PhoneFrame(Modifier.align(Alignment.CenterHorizontally)) {
            LoopingVideo(R.raw.timelapse_home, contentDescription = "Timelapse of a phone repair at a TAA workshop", modifier = Modifier.fillMaxSize())
        }
    }
}

/** "Tap a tool. Learn what it does." — the Pro Tech Mat teaser. */
@Composable
private fun ProMatWidget() {
    var selectedId by rememberSaveable { mutableStateOf(BenchTools.first().id) }
    val selected = BenchTools.first { it.id == selectedId }
    Band(top = 56.dp, bottom = 40.dp) {
        Eyebrow("The bench, explained", color = TaaColors.Blue)
        Headline("Tap a tool.\nLearn what it does", punctuationColor = TaaColors.Blue, modifier = Modifier.padding(bottom = 16.dp))
        Paragraph("Every bench runs on the iFixit Pro Tech Toolkit and mat. Here's what's actually in it — tap around.")
        Spacer(Modifier.height(12.dp))
        ToolMat(selected)
        Spacer(Modifier.height(20.dp))
        Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            BenchTools.chunked(2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    row.forEach { tool ->
                        SelectableTile(
                            selected = tool.id == selectedId,
                            onClick = { selectedId = tool.id },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                        ) {
                            LineIcon(tool.iconPath, tint = TaaColors.Slate, size = 26.dp)
                            Column {
                                Text(tool.name, style = TaaType.Small.copy(fontWeight = FontWeight.Bold))
                                Text(tool.tag, style = TaaType.Small.copy(fontSize = 12.sp), modifier = Modifier.alpha(0.65f))
                            }
                        }
                    }
                }
            }
        }
        RichText(
            "This is the teaser. The full graded repair simulator — real iFixit guide photos, mistakes priced in real dollars — lives in [Teardown](${Links.TEARDOWN}).",
            style = TaaType.Small,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
private fun ToolMat(tool: BenchTool) {
    val gridLine = Color.White.copy(alpha = 0.14f)
    Box(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 340.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(TaaColors.MatBlue)
            .drawBehind {
                val step = 28.dp.toPx()
                var x = 0f
                while (x < size.width) { drawLine(gridLine, Offset(x, 0f), Offset(x, size.height), 1f); x += step }
                var y = 0f
                while (y < size.height) { drawLine(gridLine, Offset(0f, y), Offset(size.width, y), 1f); y += step }
            }
            .padding(horizontal = 18.dp, vertical = 44.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            "IFIXIT PRO TECH MAT",
            style = TaaType.Label.copy(letterSpacing = 1.8.sp),
            color = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.align(Alignment.TopStart).offset(y = (-30).dp),
        )
        Text(
            "MAGNETIC · SORTS SCREWS · SAVES REPAIRS",
            style = TaaType.Label.copy(fontSize = 9.sp),
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier.align(Alignment.BottomEnd).offset(y = 30.dp),
        )
        AnimatedContent(
            targetState = tool,
            transitionSpec = { fadeIn(tween(160)) togetherWith fadeOut(tween(120)) },
            label = "tool card",
        ) { shown ->
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(TaaColors.Cream)
                    .padding(22.dp)
                    .semantics { liveRegion = LiveRegionMode.Polite },
            ) {
                Text("ON THE MAT", style = TaaType.Label.copy(letterSpacing = 1.6.sp), color = TaaColors.Blue)
                Text(shown.name, style = TaaType.CardTitle.copy(fontSize = 22.sp), modifier = Modifier.padding(top = 6.dp, bottom = 8.dp))
                RichText("**What it is:** ${shown.what}", style = TaaType.Small, modifier = Modifier.padding(bottom = 8.dp))
                RichText("**What it's for:** ${shown.why}", style = TaaType.Small)
            }
        }
    }
}

@Composable
private fun HomePartners() {
    Band(top = 24.dp, bottom = 48.dp) {
        Eyebrow("Who backs the benches")
        SandCard(spacing = 14.dp, modifier = Modifier.padding(bottom = 20.dp)) {
            Box(
                Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(16.dp)).background(Color.White),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painterResource(R.drawable.logo_ifixit),
                    contentDescription = "iFixit logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(64.dp).fillMaxWidth(0.7f),
                )
            }
            Text("iFixit — Partner", style = TaaType.CardTitle)
            Text("The 30 Pro Tech Toolkits and mats on our benches, plus the open repair guides we teach from — in the workshops and inside Teardown.", style = TaaType.Body)
        }
        SandCard(spacing = 14.dp) {
            GraniteWordmark()
            Text("Granite Telecommunications — Donor", style = TaaType.CardTitle)
            Text("Donated the ~110 desktops and laptops that form our hardware base — the machines we repair, teach on, and refurbish to give away.", style = TaaType.Body)
        }
    }
}

@Composable
fun GraniteWordmark(modifier: Modifier = Modifier) {
    Box(
        modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(16.dp)).background(TaaColors.Espresso),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            "GRANITE",
            style = TaaType.Subsection.copy(letterSpacing = 4.sp),
            color = TaaColors.Cream,
            modifier = Modifier.semantics { contentDescription = "Granite Telecommunications logo" },
        )
    }
}

@Composable
private fun ChaptersTeaser() {
    val actions = LocalAppActions.current
    Band(background = TaaColors.Espresso, contentColor = TaaColors.Cream, top = 56.dp, bottom = 56.dp) {
        SplitHeadline("Six towns.", "Aiming for fifty.", accent = TaaColors.Yellow, style = TaaType.Section, color = TaaColors.Cream)
        Paragraph(
            "Pick your town, get its schedule and signup link. No chapter near you yet? That's your opening — we'll help you start one.",
            modifier = Modifier.padding(top = 20.dp).alpha(0.88f),
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 16.dp),
        ) {
            Chapters.forEach { chapter ->
                val status = when {
                    chapter.id == "shrewsbury" -> "live · flagship"
                    chapter.live -> "live"
                    else -> "forming"
                }
                OutlineChip(
                    "${chapter.name} — $status",
                    borderColor = TaaColors.Cream.copy(alpha = 0.3f),
                    dot = chapterStatusColor(chapter.live),
                    dimmed = !chapter.live,
                )
            }
        }
        PillButton("Pick your chapter →", onClick = { actions.navigate(Routes.CHAPTERS) }, style = PillStyle.Light)
    }
}

private data class Door(val tag: String, val color: Color, val title: String, val body: String, val target: String)

private val Doors = listOf(
    Door("Volunteer", TaaColors.Yellow, "Teach, triage, or start a chapter", "Instructors, refurbishers, chapter leaders for new towns. Email us — we'll find you a bench.", "mailto:${Links.CONTACT_EMAIL}?subject=Volunteering"),
    Door("Donate", TaaColors.Coral, "Money or machines — both work", "Tax-deductible. Old laptops, desktops, and parts become the next class's teaching hardware.", "app:${Routes.DONATE}"),
    Door("Partner", TaaColors.Cyan, "Sponsor a bench or a chapter", "Companies keep the toolkits stocked and the drives running. Talk to us about sponsorship.", "mailto:${Links.RONIT_EMAIL}?subject=Partnership"),
)

@Composable
private fun ThreeDoors() {
    val actions = LocalAppActions.current
    Band(top = 56.dp, bottom = 32.dp) {
        Eyebrow("Get involved")
        Headline("Three doors", modifier = Modifier.padding(bottom = 28.dp))
        Doors.forEach { door ->
            SandCard(
                spacing = 12.dp,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable(role = Role.Button) { actions.open(door.target) },
            ) {
                TagPill(door.tag, door.color)
                Text(door.title, style = TaaType.CardTitle)
                Text(door.body, style = TaaType.Body)
            }
        }
    }
}

private val FeedPhotos = listOf(
    R.drawable.photo_toolkit to "An iFixit Pro Tech Toolkit spread open beside a phone screen being removed",
    R.drawable.photo_marking_screws to "A student marking screw positions on an iFixit mat next to an opened phone",
    R.drawable.photo_tweezers to "A student lifting a component out of an open phone with precision tweezers",
    R.drawable.photo_camera to "A young student examining a disassembled camera over an iFixit mat",
    R.drawable.photo_workshop_table to "Kids working through toolkits and mats at a workshop table",
    R.drawable.photo_macbook to "Hands positioning a precision driver over an open MacBook",
)

@Composable
private fun FromTheFeed() {
    val actions = LocalAppActions.current
    Band(top = 24.dp, bottom = 48.dp) {
        Eyebrow("From the feed", color = TaaColors.Blue)
        Headline("This is what a\nworkshop looks like", modifier = Modifier.padding(bottom = 20.dp))
        PillButton("Follow ${Links.INSTAGRAM_HANDLE} ↗", onClick = { actions.openUrl(Links.INSTAGRAM) })
        Spacer(Modifier.height(24.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FeedPhotos.chunked(3).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { (photo, alt) ->
                        Photo(photo, alt, Modifier.weight(1f).aspectRatio(1f), shape = RoundedCornerShape(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun NewsletterCard() {
    Band(top = 0.dp, bottom = 32.dp) {
        SandCard(background = TaaColors.Yellow, contentColor = TaaColors.Ink, padding = PaddingValues(24.dp), spacing = 10.dp) {
            Text("BETWEEN WORKSHOPS.", style = TaaType.Subsection, modifier = Modifier.semantics { heading() })
            Text(
                "New chapter announcements, workshop schedule changes, e-waste drive dates. That's the whole email — nothing else.",
                style = TaaType.Body,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            NewsletterSignup()
        }
    }
}

@Composable
private fun DonateStrip() {
    val actions = LocalAppActions.current
    Band(top = 0.dp, bottom = 56.dp) {
        SandCard(spacing = 16.dp) {
            Headline(
                "Donations are tax-deductible",
                punctuationColor = TaaColors.Gold,
                style = TaaType.Subsection,
            )
            Text(
                "501(c)(3) status: granted. And here's the part donors like: when a donated device can't be refurbished, we resell it for parts — and the proceeds fund the program directly.",
                style = TaaType.Body,
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("$150 — start a chapter", "$250 — an e-waste drive", "$500 — 5 new chapters", "$1,000 — 10 new chapters")
                    .forEach { OutlineChip(it) }
            }
            DashedBox(Modifier.padding(top = 4.dp)) {
                Text("GoFundMe campaign", style = TaaType.Title)
                Text(
                    "Campaign link pending. Device donations welcome too — desktops, laptops, batteries, screens, parts.",
                    style = TaaType.Small,
                    modifier = Modifier.alpha(0.75f),
                )
                ArrowLink("Ways to give →", onClick = { actions.navigate(Routes.DONATE) })
            }
        }
    }
}
