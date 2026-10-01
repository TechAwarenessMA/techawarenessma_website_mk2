package com.techawarenessma.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.techawarenessma.app.ui.components.Band
import com.techawarenessma.app.ui.components.Eyebrow
import com.techawarenessma.app.ui.components.Paragraph
import com.techawarenessma.app.ui.components.RichText
import com.techawarenessma.app.ui.components.SandCard
import com.techawarenessma.app.ui.components.SplitHeadline
import com.techawarenessma.app.ui.components.TagPill
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType

/** A scrolling page made of full-width bands. */
@Composable
fun ScrollingPage(content: LazyListScope.() -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), content = content)
}

/** The page opener every website page shares: eyebrow, two-line headline, intro. */
@Composable
fun PageHeader(
    eyebrow: String?,
    first: String,
    second: String,
    accent: Color,
    intro: String? = null,
    eyebrowColor: Color = TaaColors.Red,
) {
    Band(top = 20.dp, bottom = 36.dp) {
        if (eyebrow != null) Eyebrow(eyebrow, color = eyebrowColor)
        SplitHeadline(first, second, accent = accent)
        if (intro != null) {
            Paragraph(intro, style = TaaType.BodyLarge, modifier = Modifier.padding(top = 20.dp))
        }
    }
}

/** A card with a colored pill, a title and copy: roles, reasons, tiers. */
@Composable
fun PillCard(
    pill: String,
    pillColor: Color,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    background: Color = TaaColors.Sand,
    contentColor: Color = TaaColors.Slate,
) {
    SandCard(modifier = modifier, background = background, contentColor = contentColor, spacing = 12.dp) {
        TagPill(pill, pillColor)
        if (title.isNotEmpty()) Text(title, style = TaaType.CardTitle, modifier = Modifier.semantics { heading() })
        RichText(body, style = TaaType.Body)
    }
}

/** "$150 · Start a chapter · Covers a new chapter's startup kit…" */
@Composable
fun TierCard(
    amount: String,
    amountColor: Color,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    background: Color = TaaColors.Sand,
    contentColor: Color = TaaColors.Slate,
) {
    SandCard(modifier = modifier, background = background, contentColor = contentColor, spacing = 6.dp) {
        Text(amount, style = TaaType.BigNumber, color = amountColor)
        Text(title, style = TaaType.CardTitle)
        Text(body, style = TaaType.Body)
    }
}

/** A title with a small status pill beside it ("Teardown · LIVE"). Brand names keep their casing. */
@Composable
fun TitleWithPill(title: String, pill: String, pillColor: Color, modifier: Modifier = Modifier, color: Color = TaaColors.Ink) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, style = TaaType.Subsection, color = color, modifier = Modifier.weight(1f, fill = false).semantics { heading() })
        TagPill(pill, pillColor)
    }
}

/** Two stacked lines of light text in a list ("We bring" / "You bring"). */
@Composable
fun LinesCard(label: String, lines: List<String>, labelColor: Color, modifier: Modifier = Modifier) {
    SandCard(modifier = modifier, background = TaaColors.Cream.copy(alpha = 0.08f), contentColor = TaaColors.Cream, spacing = 6.dp) {
        Text(label.uppercase(), style = TaaType.Label.copy(letterSpacing = TaaType.Eyebrow.letterSpacing), color = labelColor)
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            lines.forEach { Text(it, style = TaaType.Body) }
        }
    }
}
