package com.techawarenessma.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.techawarenessma.app.ui.LocalAppActions
import com.techawarenessma.app.ui.text.parseMarkup
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType

/** The small, letter-spaced, uppercase label that opens most sections on the site. */
@Composable
fun Eyebrow(text: String, modifier: Modifier = Modifier, color: Color = TaaColors.Red) {
    Text(text.uppercase(), style = TaaType.Eyebrow, color = color, modifier = modifier.padding(bottom = 14.dp))
}

/**
 * The site's signature two-line headline: first line in ink, second line indented and in an
 * accent color ("We fix it / together.").
 */
@Composable
fun SplitHeadline(
    first: String,
    second: String,
    accent: Color,
    modifier: Modifier = Modifier,
    style: TextStyle = TaaType.PageTitle,
    color: Color = TaaColors.Ink,
) {
    val indent = with(LocalDensity.current) { (style.fontSize * 0.9f).toDp() }
    Column(modifier.semantics(mergeDescendants = true) { heading() }) {
        Text(first.uppercase(), style = style, color = color)
        Text(second.uppercase(), style = style, color = accent, modifier = Modifier.padding(start = indent))
    }
}

/** An uppercase headline whose closing punctuation is picked out in an accent color. */
@Composable
fun Headline(
    text: String,
    modifier: Modifier = Modifier,
    punctuation: String = ".",
    punctuationColor: Color = TaaColors.Red,
    style: TextStyle = TaaType.Section,
    color: Color = TaaColors.Ink,
    uppercase: Boolean = true,
) {
    val annotated = remember(text, punctuation, punctuationColor, uppercase) {
        buildAnnotatedString {
            append(if (uppercase) text.uppercase() else text)
            withStyle(SpanStyle(color = punctuationColor)) { append(punctuation) }
        }
    }
    Text(annotated, style = style, color = color, modifier = modifier.semantics { heading() })
}

/**
 * Body copy written in the tiny markup from [parseMarkup]. Links route through
 * [LocalAppActions], so `app:`, `mailto:` and `https:` targets all work.
 */
@Composable
fun RichText(
    markup: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TaaType.Body,
    color: Color = Color.Unspecified,
    linkColor: Color = Color.Unspecified,
) {
    val actions = LocalAppActions.current
    val resolvedColor = if (color == Color.Unspecified) LocalContentColor.current else color
    val text = remember(markup, actions, linkColor) {
        val linkStyles = TextLinkStyles(
            style = SpanStyle(
                color = linkColor,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
            ),
        )
        buildAnnotatedString {
            parseMarkup(markup).forEach { span ->
                val spanStyle = SpanStyle(
                    fontWeight = if (span.bold) FontWeight.Bold else null,
                    fontStyle = if (span.italic) FontStyle.Italic else null,
                )
                val target = span.link
                if (target != null) {
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = target,
                            styles = linkStyles,
                            linkInteractionListener = { actions.open(target) },
                        ),
                    ) { withStyle(spanStyle) { append(span.text) } }
                } else {
                    withStyle(spanStyle) { append(span.text) }
                }
            }
        }
    }
    Text(text, modifier = modifier, style = style, color = resolvedColor)
}

@Composable
fun Paragraph(
    markup: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TaaType.Body,
    color: Color = Color.Unspecified,
) {
    RichText(markup, modifier = modifier.padding(bottom = 14.dp), style = style, color = color)
}
