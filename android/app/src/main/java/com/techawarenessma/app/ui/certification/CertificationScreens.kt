package com.techawarenessma.app.ui.certification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techawarenessma.app.R
import com.techawarenessma.app.certification.ACCESS_CODE_ERROR
import com.techawarenessma.app.certification.CertSection
import com.techawarenessma.app.certification.CertificationState
import com.techawarenessma.app.certification.CertificationStore
import com.techawarenessma.app.certification.SectionGroup
import com.techawarenessma.app.certification.StageStatus
import com.techawarenessma.app.certification.pathStages
import com.techawarenessma.app.navigation.Routes
import com.techawarenessma.app.ui.components.Headline
import com.techawarenessma.app.ui.components.MaxContentWidth
import com.techawarenessma.app.ui.components.PillButton
import com.techawarenessma.app.ui.components.PillStyle
import com.techawarenessma.app.ui.components.RichText
import com.techawarenessma.app.ui.components.TaaIcons
import com.techawarenessma.app.ui.components.TaaTextField
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType

/** Entry point: the access gate until a code is accepted, then the course overview. */
@Composable
fun CertificationHomeScreen(
    store: CertificationStore,
    onOpenSection: (CertSection) -> Unit,
    onClose: () -> Unit,
) {
    val state by store.state.collectAsStateWithLifecycle()
    if (!state.authed) {
        AccessGate(onSubmit = store::signIn, onClose = onClose)
    } else {
        CourseOverview(state = state, onOpenSection = onOpenSection, onSignOut = store::signOut, onClose = onClose)
    }
}

@Composable
private fun AccessGate(onSubmit: (String) -> Boolean, onClose: () -> Unit) {
    var code by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    val submit = { error = if (onSubmit(code)) null else ACCESS_CODE_ERROR }

    Column(
        Modifier
            .fillMaxSize()
            .background(TaaColors.Espresso)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState()),
    ) {
        IconButton(onClick = onClose, modifier = Modifier.padding(4.dp)) {
            Icon(TaaIcons.ArrowBack, contentDescription = "Back", tint = TaaColors.Cream)
        }
        Column(
            Modifier
                .align(Alignment.CenterHorizontally)
                .widthIn(max = 560.dp)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(TaaColors.Cream)
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            LogoLockup()
            Text("NOT FOR PUBLIC DISTRIBUTION", style = TaaType.Label.copy(letterSpacing = 1.6.sp), color = TaaColors.Red)
            Text(
                "CHAPTER LEAD CERTIFICATION",
                style = TaaType.Subsection,
                color = TaaColors.Ink,
                modifier = Modifier.semantics { heading() },
            )
            RichText(
                "This is the whole training program for running a Tech Awareness chapter. Access codes are issued by TAA after your chapter application is approved. If you don't have one, [apply first](app:${Routes.START_CHAPTER}).",
                style = TaaType.Body,
                color = TaaColors.Slate,
            )
            TaaTextField(
                value = code,
                onValueChange = {
                    code = it
                    error = null
                },
                label = "Your access code",
                placeholder = "TAA-XXXX",
                error = error,
                capitalization = KeyboardCapitalization.Characters,
                imeAction = ImeAction.Go,
                onImeAction = submit,
            )
            PillButton("Enter →", onClick = submit, modifier = Modifier.fillMaxWidth())
            Text(
                "Prototype: any code in TAA-XXXX form works",
                style = TaaType.Small,
                color = TaaColors.Slate,
                modifier = Modifier
                    .background(TaaColors.Sand, CircleShape)
                    .padding(horizontal = 14.dp, vertical = 6.dp),
            )
        }
    }
}

@Composable
private fun LogoLockup() {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Image(painterResource(R.drawable.logo_taa), contentDescription = "Tech Awareness Association logo", modifier = Modifier.size(44.dp))
        Column {
            Text("TECH AWARENESS", style = TaaType.Label.copy(letterSpacing = 1.6.sp), color = TaaColors.Slate)
            Text("ASSOCIATION", style = TaaType.Label.copy(letterSpacing = 1.6.sp), color = TaaColors.Slate)
        }
    }
}

/** The dark platform header shared by the overview and each section. */
@Composable
private fun PlatformHeader(
    onBack: () -> Unit,
    subtitle: String,
    trailing: @Composable () -> Unit = {},
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(TaaColors.Espresso)
            .statusBarsPadding()
            .padding(start = 4.dp, end = 12.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack) {
            Icon(TaaIcons.ArrowBack, contentDescription = "Back", tint = TaaColors.Cream)
        }
        Image(painterResource(R.drawable.logo_taa), contentDescription = null, modifier = Modifier.size(32.dp))
        Column(Modifier.weight(1f).padding(start = 10.dp)) {
            Text("Chapter Replication Guide", style = TaaType.Title.copy(fontSize = 15.sp), color = TaaColors.Cream)
            Text(subtitle, style = TaaType.Small.copy(fontSize = 12.sp), color = TaaColors.Cream.copy(alpha = 0.7f))
        }
        trailing()
    }
}

@Composable
private fun CourseOverview(
    state: CertificationState,
    onOpenSection: (CertSection) -> Unit,
    onSignOut: () -> Unit,
    onClose: () -> Unit,
) {
    Column(Modifier.fillMaxSize().background(TaaColors.Cream)) {
        PlatformHeader(onBack = onClose, subtitle = "v1.0 · Certification platform") {
            TextButton(onClick = onSignOut) {
                Text("Sign out", style = TaaType.Button.copy(fontSize = 14.sp), color = TaaColors.Cream)
            }
        }
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding(),
        ) {
            PathStepper(state)
            Column(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .widthIn(max = MaxContentWidth)
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Text(
                    "NOT FOR PUBLIC DISTRIBUTION",
                    style = TaaType.Label.copy(letterSpacing = 1.6.sp),
                    color = TaaColors.Red,
                    modifier = Modifier.border(1.dp, TaaColors.Red, CircleShape).padding(horizontal = 12.dp, vertical = 5.dp),
                )
                Column(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(TaaColors.Ink).padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text("YOUR PROGRESS", style = TaaType.Label.copy(letterSpacing = 1.6.sp), color = TaaColors.Yellow)
                    Text(
                        "${state.doneCount} of 5 module checkpoints done. Finishing the reading doesn't make you a chapter lead. Getting your certification submission approved does.",
                        style = TaaType.Body,
                        color = TaaColors.Cream,
                    )
                }
                PillButton(
                    "Continue: ${state.lastSection.label} →",
                    onClick = { onOpenSection(state.lastSection) },
                    style = PillStyle.Yellow,
                )
                SectionGroup.entries.forEach { group ->
                    SectionList(group, state, onOpenSection)
                }
            }
        }
    }
}

@Composable
private fun SectionList(group: SectionGroup, state: CertificationState, onOpenSection: (CertSection) -> Unit) {
    Column {
        Text(
            group.title.uppercase(),
            style = TaaType.Eyebrow,
            color = TaaColors.Red,
            modifier = Modifier.padding(bottom = 8.dp).semantics { heading() },
        )
        Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(TaaColors.Sand)) {
            CertSection.entries.filter { it.group == group }.forEachIndexed { index, section ->
                if (index > 0) HorizontalDivider(color = TaaColors.Cream, thickness = 2.dp)
                val tick = when {
                    section.isModule && state.isDone(section) -> "✓ done"
                    section == CertSection.P8 -> "GAP"
                    section == CertSection.P9 -> "${state.certCount}/6"
                    else -> ""
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .clickable(role = Role.Button) { onOpenSection(section) }
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(section.label, style = TaaType.Title.copy(fontSize = 16.sp), color = TaaColors.Ink, modifier = Modifier.weight(1f))
                    if (tick.isNotEmpty()) {
                        Text(tick, style = TaaType.Label, color = if (tick == "GAP") TaaColors.Red else TaaColors.Blue)
                    }
                    Icon(TaaIcons.ChevronRight, contentDescription = null, tint = TaaColors.Slate, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}

/** "Application → … → Full charter": the whole program, filling in as you go. */
@Composable
private fun PathStepper(state: CertificationState) {
    val stages = pathStages(state)
    Row(
        Modifier
            .fillMaxWidth()
            .background(TaaColors.Espresso)
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.Top,
    ) {
        stages.forEachIndexed { index, stage ->
            val done = stage.status == StageStatus.Done
            val current = stage.status == StageStatus.Current
            val fill = when {
                done -> TaaColors.Cyan
                current -> TaaColors.Yellow
                else -> TaaColors.Espresso
            }
            val ring = when {
                done -> TaaColors.Cyan
                current -> TaaColors.Yellow
                else -> TaaColors.Cream.copy(alpha = 0.3f)
            }
            val status = when (stage.status) {
                StageStatus.Done -> "done"
                StageStatus.Current -> "in progress, ${stage.mark}"
                StageStatus.Locked -> "locked"
                StageStatus.Upcoming -> "upcoming"
            }
            Column(
                Modifier
                    .width(92.dp)
                    .alpha(if (done || current) 1f else 0.55f)
                    .semantics(mergeDescendants = true) { contentDescription = "Step ${stage.number}, ${stage.label}, $status" },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    Modifier.size(30.dp).background(fill, CircleShape).border(2.dp, ring, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        stage.mark,
                        style = TaaType.Label.copy(fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.sp),
                        color = if (done || current) TaaColors.Ink else TaaColors.Cream,
                    )
                }
                Text(
                    stage.label.uppercase(),
                    style = TaaType.Label.copy(fontSize = 9.sp, lineHeight = 12.sp, letterSpacing = 0.6.sp),
                    color = TaaColors.Cream,
                    textAlign = TextAlign.Center,
                )
            }
            if (index < stages.lastIndex) {
                Box(
                    Modifier
                        .padding(top = 14.dp)
                        .width(14.dp)
                        .heightIn(min = 2.dp, max = 2.dp)
                        .background(if (done) TaaColors.Cyan else TaaColors.Cream.copy(alpha = 0.2f)),
                )
            }
        }
    }
}

/** One section of the course, with the checkpoint (for modules) and previous/next links. */
@Composable
fun CertificationSectionScreen(
    store: CertificationStore,
    sectionId: String?,
    onOpenSection: (CertSection) -> Unit,
    onBack: () -> Unit,
) {
    val state by store.state.collectAsStateWithLifecycle()
    val section = CertSection.byId(sectionId) ?: CertSection.HowToUse
    if (!state.authed) {
        // Signed out from elsewhere (e.g. restored after process death): gate again.
        AccessGate(onSubmit = store::signIn, onClose = onBack)
        return
    }
    LaunchedEffect(section) { store.openSection(section) }

    Column(Modifier.fillMaxSize().background(TaaColors.Cream)) {
        PlatformHeader(onBack = onBack, subtitle = section.group.title)
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding(),
        ) {
            Column(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .widthIn(max = MaxContentWidth)
                    .padding(horizontal = 20.dp, vertical = 24.dp),
            ) {
                CompositionLocalProvider(LocalContentColor provides TaaColors.Slate) {
                    SectionContent(section, state, store)
                }
                if (section.isModule) {
                    CheckpointButton(
                        section = section,
                        done = state.isDone(section),
                        onToggle = { store.setModuleDone(section, it) },
                        modifier = Modifier.padding(top = 24.dp),
                    )
                }
                SectionPager(section, onOpenSection, Modifier.padding(top = 32.dp, bottom = 24.dp))
            }
        }
    }
}

@Composable
private fun SectionContent(section: CertSection, state: CertificationState, store: CertificationStore) {
    Column {
        when (section) {
            CertSection.HowToUse -> HowToUseSection()
            CertSection.WhatTaaIs -> WhatTaaIsSection()
            CertSection.WhatAChapterIs -> WhatAChapterIsSection()
            CertSection.Roles -> RolesSection()
            CertSection.M1 -> Module1Section()
            CertSection.M2 -> Module2Section()
            CertSection.M3 -> Module3Section()
            CertSection.M4 -> Module4Section()
            CertSection.M5 -> Module5Section()
            CertSection.P7 -> Part7Section()
            CertSection.P8 -> Part8Section()
            CertSection.P9 -> Part9Section(state.certItems, onToggle = store::setCertItemReady)
        }
    }
}

@Composable
private fun CheckpointButton(section: CertSection, done: Boolean, onToggle: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    PillButton(
        text = if (done) "✓ Checkpoint done — tap to undo" else "Mark ${section.shortName} checkpoint done",
        onClick = { onToggle(!done) },
        style = if (done) PillStyle.Dark else PillStyle.Outline,
        modifier = modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite },
    )
}

@Composable
private fun SectionPager(section: CertSection, onOpenSection: (CertSection) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        section.next?.let { next ->
            PillButton("Next: ${next.label} →", onClick = { onOpenSection(next) }, modifier = Modifier.fillMaxWidth())
        }
        section.previous?.let { previous ->
            PillButton("← ${previous.label}", onClick = { onOpenSection(previous) }, style = PillStyle.Outline, modifier = Modifier.fillMaxWidth())
        }
        if (section.next == null) {
            Spacer(Modifier.heightIn(min = 4.dp))
            Text(
                "Finishing the reading doesn't make you a chapter lead. Getting your certification submission approved does.",
                style = TaaType.Small,
                modifier = Modifier.alpha(0.7f),
            )
        }
    }
}

// ------------------------------------------------------------------ shared course blocks

/** "Part 1 · 45 minutes reading" */
@Composable
internal fun Kicker(text: String, modifier: Modifier = Modifier) {
    Text(text.uppercase(), style = TaaType.Label.copy(letterSpacing = 1.4.sp, lineHeight = 17.sp), color = TaaColors.Red, modifier = modifier.padding(bottom = 10.dp))
}

@Composable
internal fun CourseTitle(text: String, punctuation: String = ".") {
    Headline(
        text,
        punctuation = punctuation,
        style = TaaType.Section.copy(fontSize = 30.sp, lineHeight = 33.sp),
        modifier = Modifier.padding(bottom = 18.dp),
    )
}

@Composable
internal fun SubHeading(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        style = TaaType.CardTitle.copy(fontSize = 21.sp, fontWeight = FontWeight.ExtraBold),
        color = TaaColors.Ink,
        modifier = modifier.padding(top = 28.dp, bottom = 12.dp).semantics { heading() },
    )
}

@Composable
internal fun Body(markup: String, modifier: Modifier = Modifier) {
    RichText(markup, style = TaaType.Body, modifier = modifier.padding(bottom = 14.dp))
}

/** A highlighted note: "Don't rush it", "Read this first", "Point 10, plainly". */
@Composable
internal fun Callout(
    label: String,
    modifier: Modifier = Modifier,
    dark: Boolean = false,
    accent: Color = TaaColors.Red,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(if (dark) TaaColors.Ink else TaaColors.Sand)
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(label.uppercase(), style = TaaType.Label.copy(letterSpacing = 1.4.sp), color = if (dark) TaaColors.Yellow else accent)
        CompositionLocalProvider(LocalContentColor provides if (dark) TaaColors.Cream else TaaColors.Slate) {
            content()
        }
    }
}

/** A bold lead-in, then its explanation. Numbered commitments, requirements, rules. */
@Composable
internal fun Rule(lead: String, detail: String?, modifier: Modifier = Modifier, kicker: String? = null) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(TaaColors.Sand)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (kicker != null) Text(kicker.uppercase(), style = TaaType.Label, color = TaaColors.Blue)
        RichText(lead, style = TaaType.Body.copy(fontWeight = FontWeight.Bold), color = TaaColors.Ink)
        if (detail != null) RichText(detail, style = TaaType.Small)
    }
}

/** A rule written as one sentence with its own bold lead-in: "**1 · Below 25% charge** before…". */
@Composable
internal fun InlineRule(markup: String, modifier: Modifier = Modifier) {
    RichText(
        markup,
        style = TaaType.Body,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(TaaColors.Sand)
            .padding(16.dp),
    )
}

/** The site's pull-quote: large text set off by an accent rule. */
@Composable
internal fun Quote(markup: String, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth().padding(bottom = 12.dp).height(IntrinsicSize.Min)) {
        Box(Modifier.width(4.dp).fillMaxHeight().background(TaaColors.Blue, RoundedCornerShape(2.dp)))
        RichText(
            markup,
            style = TaaType.BodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = TaaColors.Ink,
            modifier = Modifier.padding(start = 16.dp),
        )
    }
}
