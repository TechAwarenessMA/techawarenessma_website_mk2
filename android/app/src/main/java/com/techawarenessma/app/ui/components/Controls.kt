package com.techawarenessma.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType

enum class PillStyle(val container: Color, val content: Color, val border: Color?) {
    Dark(TaaColors.Ink, TaaColors.Cream, null),
    Outline(Color.Transparent, TaaColors.Ink, TaaColors.Ink),
    Yellow(TaaColors.Yellow, TaaColors.Ink, null),
    Light(TaaColors.Cream, TaaColors.Ink, null),
    OutlineLight(Color.Transparent, TaaColors.Cream, TaaColors.Cream),
}

/** The site's rounded "pill" call to action. */
@Composable
fun PillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: PillStyle = PillStyle.Dark,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.heightIn(min = 48.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = style.container,
            contentColor = style.content,
            disabledContainerColor = style.container.copy(alpha = 0.4f),
            disabledContentColor = style.content.copy(alpha = 0.6f),
        ),
        border = style.border?.let { BorderStroke(2.dp, it) },
        contentPadding = PaddingValues(horizontal = 26.dp, vertical = 12.dp),
    ) {
        Text(text, style = TaaType.Button, textAlign = TextAlign.Center)
    }
}

/** An inline "See how it works →" link with a full-size touch target. */
@Composable
fun ArrowLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = TaaColors.Ink,
) {
    Box(
        modifier = modifier
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(text, style = TaaType.Button.copy(fontSize = 14.sp), color = color)
    }
}

/** Small filled label: "01", "Volunteer", "Live". */
@Composable
fun TagPill(text: String, background: Color, modifier: Modifier = Modifier, color: Color = TaaColors.Ink) {
    Text(
        text.uppercase(),
        style = TaaType.Label,
        color = color,
        modifier = modifier
            .background(background, CircleShape)
            .padding(horizontal = 12.dp, vertical = 4.dp),
    )
}

/** Outlined chip used for tiers, playbook rules and device types. */
@Composable
fun OutlineChip(
    text: String,
    modifier: Modifier = Modifier,
    borderColor: Color = TaaColors.Slate,
    color: Color = Color.Unspecified,
    dot: Color? = null,
    dimmed: Boolean = false,
) {
    Row(
        modifier = modifier
            .alpha(if (dimmed) 0.7f else 1f)
            .border(1.dp, borderColor, CircleShape)
            .padding(horizontal = 16.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (dot != null) StatusDot(dot)
        Text(text, style = TaaType.Small.copy(fontWeight = FontWeight.SemiBold), color = color)
    }
}

@Composable
fun StatusDot(color: Color, modifier: Modifier = Modifier, size: Dp = 8.dp) {
    Box(modifier.size(size).background(color, CircleShape))
}

/**
 * A choice in a single-select group (tools, chapters, ladder rungs). Announced to
 * TalkBack as a radio button so the selected state is spoken.
 */
@Composable
fun SelectableTile(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedRing: Color = TaaColors.Blue,
    background: Color = TaaColors.Cream,
    selectedBackground: Color = TaaColors.Sand,
    idleBorder: Color = TaaColors.Sand,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    content: @Composable RowScope.() -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = modifier
            .heightIn(min = 56.dp)
            .clip(shape)
            .background(if (selected) selectedBackground else background)
            .border(2.dp, if (selected) selectedRing else idleBorder, shape)
            .selectable(selected = selected, role = Role.RadioButton, onClick = onClick)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = content,
    )
}

/** A tile that flips on and off (certification checklist, module checkpoints). */
@Composable
fun ToggleTile(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = modifier
            .heightIn(min = 56.dp)
            .clip(shape)
            .background(if (checked) TaaColors.Sand else TaaColors.Cream)
            .border(2.dp, if (checked) TaaColors.Blue else TaaColors.Sand, shape)
            .toggleable(value = checked, role = Role.Checkbox, onValueChange = onCheckedChange)
            .padding(16.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        content = content,
    )
}
