package com.techawarenessma.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techawarenessma.app.content.Chapter
import com.techawarenessma.app.content.Chapters
import com.techawarenessma.app.content.chapterStatusColor
import com.techawarenessma.app.navigation.Routes
import com.techawarenessma.app.ui.LocalAppActions
import com.techawarenessma.app.ui.components.Band
import com.techawarenessma.app.ui.components.DashedBox
import com.techawarenessma.app.ui.components.Eyebrow
import com.techawarenessma.app.ui.components.Headline
import com.techawarenessma.app.ui.components.Paragraph
import com.techawarenessma.app.ui.components.Photo
import com.techawarenessma.app.ui.components.PillButton
import com.techawarenessma.app.ui.components.PillStyle
import com.techawarenessma.app.ui.components.RichText
import com.techawarenessma.app.ui.components.SandCard
import com.techawarenessma.app.ui.components.SelectableTile
import com.techawarenessma.app.ui.components.SplitHeadline
import com.techawarenessma.app.ui.components.StatusDot
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType

@Composable
fun ChaptersScreen() {
    var selectedId by rememberSaveable { mutableStateOf(Chapters.first().id) }
    val selected = Chapters.first { it.id == selectedId }
    LazyColumn(Modifier.fillMaxSize()) {
        item(key = "header") {
            Band(top = 20.dp, bottom = 28.dp) {
                Eyebrow("Chapters")
                SplitHeadline("Pick", "your town.", accent = TaaColors.Red)
                RichText(
                    "Tap a chapter for its schedule and signup link. Three are live, three are forming — and forming means they need **you**.",
                    style = TaaType.BodyLarge,
                    modifier = Modifier.padding(top = 20.dp),
                )
            }
        }
        item(key = "selector") {
            Band(top = 0.dp, bottom = 48.dp) {
                ChapterPicker(selectedId = selectedId, onSelect = { selectedId = it })
                ChapterDetail(selected, Modifier.padding(top = 20.dp))
            }
        }
        item(key = "ambition") { FiftyTowns() }
    }
}

@Composable
private fun ChapterPicker(selectedId: String, onSelect: (String) -> Unit) {
    Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Chapters.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { chapter ->
                    SelectableTile(
                        selected = chapter.id == selectedId,
                        onClick = { onSelect(chapter.id) },
                        selectedRing = TaaColors.Coral,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(chapter.name, style = TaaType.Title.copy(fontSize = 16.sp))
                            Text(chapter.sub, style = TaaType.Small.copy(fontSize = 12.sp), modifier = Modifier.alpha(0.65f))
                            StatusLabel(chapter.live, Modifier.padding(top = 6.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusLabel(live: Boolean, modifier: Modifier = Modifier) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        StatusDot(chapterStatusColor(live))
        Text(if (live) "LIVE" else "FORMING", style = TaaType.Label)
    }
}

@Composable
private fun ChapterDetail(chapter: Chapter, modifier: Modifier = Modifier) {
    val actions = LocalAppActions.current
    AnimatedContent(
        targetState = chapter,
        transitionSpec = { fadeIn(tween(160)) togetherWith fadeOut(tween(120)) },
        modifier = modifier,
        label = "chapter detail",
    ) { shown ->
        SandCard(spacing = 16.dp, modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    shown.name.uppercase(),
                    style = TaaType.Subsection,
                    color = TaaColors.Ink,
                    modifier = Modifier.weight(1f, fill = false).semantics { heading() },
                )
                StatusLabel(
                    shown.live,
                    Modifier.background(TaaColors.Cream, CircleShape).padding(horizontal = 12.dp, vertical = 6.dp),
                )
            }
            Text(shown.blurb, style = TaaType.Body)
            InfoTile("Schedule", shown.schedule)
            InfoTile("Where", shown.venue)
            if (shown.photo != null) {
                Photo(shown.photo, shown.photoAlt, Modifier.fillMaxWidth().height(220.dp))
            } else {
                DashedBox {
                    Text(
                        "No photos yet — because it hasn't happened yet. Want to change that? Scroll down.",
                        style = TaaType.Small,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.alpha(0.7f),
                    )
                }
            }
            val signup = shown.signupUrl
            if (shown.live && signup != null) {
                PillButton("Library signup form →", onClick = { actions.openUrl(signup) })
            }
            if (!shown.live) {
                PillButton("Be the one who starts it →", onClick = { actions.navigate(Routes.START_CHAPTER) }, style = PillStyle.Outline)
            }
        }
    }
}

@Composable
private fun InfoTile(label: String, value: String) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(TaaColors.Cream)
            .padding(18.dp),
    ) {
        Column(Modifier.semantics(mergeDescendants = true) {}) {
            Text(label.uppercase(), style = TaaType.Label.copy(letterSpacing = 1.6.sp), modifier = Modifier.alpha(0.55f).padding(bottom = 6.dp))
            Text(value, style = TaaType.Body.copy(fontWeight = FontWeight.SemiBold))
        }
    }
}

@Composable
private fun FiftyTowns() {
    val actions = LocalAppActions.current
    Band(background = TaaColors.Espresso, contentColor = TaaColors.Cream, top = 56.dp, bottom = 56.dp) {
        Eyebrow("The ambition", color = TaaColors.Yellow)
        Column(Modifier.semantics(mergeDescendants = true) { heading() }) {
            Text("5 TOWNS →", style = TaaType.Section)
            Text("50 TOWNS.", style = TaaType.Section, color = TaaColors.Yellow, modifier = Modifier.padding(start = 30.dp))
            Headline("One year", punctuationColor = TaaColors.Cyan, color = TaaColors.Cream)
        }
        Paragraph(
            "Here's the honest math: we can't do that alone. Every new town needs one person willing to lead a chapter — we bring the curriculum, the toolkits, the training, and the playbook. You bring a library room and an hour a week.",
            modifier = Modifier.padding(top = 20.dp).alpha(0.88f),
        )
        Paragraph(
            "Live in Westborough, Worcester, Hopkinton — or anywhere we're not yet? That's not a gap. That's your chapter.",
            modifier = Modifier.alpha(0.88f).padding(bottom = 16.dp),
        )
        PillButton("I want to lead a chapter →", onClick = { actions.navigate(Routes.START_CHAPTER) }, style = PillStyle.Yellow)
    }
}
