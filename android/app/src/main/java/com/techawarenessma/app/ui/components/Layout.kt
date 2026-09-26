package com.techawarenessma.app.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType

/** Widest a text column gets on tablets and landscape, so lines stay readable. */
val MaxContentWidth = 720.dp

/**
 * A full-bleed page section. The background runs edge to edge; the content is centered
 * and capped at [MaxContentWidth], like the site's `max-width:1400px` wrappers.
 */
@Composable
fun Band(
    modifier: Modifier = Modifier,
    background: Color = Color.Transparent,
    contentColor: Color = LocalContentColor.current,
    top: Dp = 40.dp,
    bottom: Dp = 40.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth().background(background),
        contentAlignment = Alignment.TopCenter,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Column(
                modifier = Modifier
                    .widthIn(max = MaxContentWidth)
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = top, bottom = bottom),
                content = content,
            )
        }
    }
}

/** The rounded sand-colored card the site uses for almost every grouped block. */
@Composable
fun SandCard(
    modifier: Modifier = Modifier,
    background: Color = TaaColors.Sand,
    contentColor: Color = LocalContentColor.current,
    padding: PaddingValues = PaddingValues(24.dp),
    spacing: Dp = 12.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(background)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(spacing),
            content = content,
        )
    }
}

/** The dashed placeholder box ("No photos yet", "Campaign link pending"). */
@Composable
fun DashedBox(
    modifier: Modifier = Modifier,
    color: Color = TaaColors.Slate,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val stroke = 2.dp.toPx()
                drawRoundRect(
                    color = color,
                    topLeft = Offset(stroke / 2, stroke / 2),
                    size = Size(size.width - stroke, size.height - stroke),
                    cornerRadius = CornerRadius(16.dp.toPx()),
                    style = Stroke(width = stroke, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))),
                )
            }
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = content,
    )
}

/** A bundled photo, decoded off the main thread and cropped to fill its box. */
@Composable
fun Photo(
    @DrawableRes res: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
) {
    AsyncImage(
        model = res,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(shape).background(TaaColors.Sand),
    )
}

/** "Fig. 01 — Battery swap, weekly workshop" */
@Composable
fun FigureCaption(text: String, modifier: Modifier = Modifier) {
    Text(
        text.uppercase(),
        style = TaaType.Caption,
        modifier = modifier.padding(top = 10.dp).alpha(0.6f),
    )
}

/** A bold lead-in followed by its explanation, stacked. Used for rules, roles and values. */
@Composable
fun TermBlock(
    term: String,
    detail: String,
    modifier: Modifier = Modifier,
    background: Color = TaaColors.Sand,
    kicker: String? = null,
    kickerColor: Color = TaaColors.Blue,
) {
    SandCard(modifier = modifier, background = background, padding = PaddingValues(18.dp), spacing = 6.dp) {
        if (kicker != null) {
            Text(kicker.uppercase(), style = TaaType.Label, color = kickerColor)
        }
        Text(term, style = TaaType.Title)
        RichText(detail, style = TaaType.Small)
    }
}

/** A numbered line: "01  General Awareness". */
@Composable
fun NumberedLine(
    number: String,
    text: String,
    modifier: Modifier = Modifier,
    numberColor: Color = TaaColors.Yellow,
    divider: Color = Color.Transparent,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                if (divider != Color.Transparent) {
                    drawLine(
                        color = divider,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx(),
                    )
                }
            }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            number,
            style = TaaType.Small.copy(fontWeight = FontWeight.Bold, fontFeatureSettings = "tnum"),
            color = numberColor,
            modifier = Modifier.widthIn(min = 32.dp).alignByBaseline(),
        )
        RichText(text, style = TaaType.Body, modifier = Modifier.alignByBaseline())
    }
}

/** An arrow bullet: "→ Wrist strap on, clipped to unpainted metal…". */
@Composable
fun ArrowBullet(text: String, modifier: Modifier = Modifier, arrowColor: Color = TaaColors.Red) {
    Row(modifier.padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("→", style = TaaType.Body.copy(fontWeight = FontWeight.Bold), color = arrowColor)
        RichText(text, style = TaaType.Body)
    }
}

/** A big number with a caption under it: "30 / iFixit Pro Tech Toolkits". */
@Composable
fun BigStat(value: String, label: String, color: Color, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(value, style = TaaType.BigNumber, color = color)
        Text(label.uppercase(), style = TaaType.Caption, modifier = Modifier.alpha(0.7f))
    }
}
