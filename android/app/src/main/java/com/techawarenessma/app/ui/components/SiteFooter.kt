@file:OptIn(ExperimentalLayoutApi::class)

package com.techawarenessma.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.techawarenessma.app.links.Links
import com.techawarenessma.app.navigation.Routes
import com.techawarenessma.app.ui.LocalAppActions
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType

private data class FooterLink(val label: String, val target: String)

private val SiteLinks = listOf(
    FooterLink("Home", "app:${Routes.HOME}"),
    FooterLink("Programs & Workshops", "app:${Routes.PROGRAMS}"),
    FooterLink("Chapters", "app:${Routes.CHAPTERS}"),
    FooterLink("Start a Chapter", "app:${Routes.START_CHAPTER}"),
    FooterLink("Get Involved", "app:${Routes.GET_INVOLVED}"),
    FooterLink("Donate", "app:${Routes.DONATE}"),
)
private val ExploreLinks = listOf(
    FooterLink("About & Why Repair", "app:${Routes.ABOUT}"),
    FooterLink("Members", "app:${Routes.MEMBERS}"),
    FooterLink("Partners & Sponsors", "app:${Routes.PARTNERS}"),
    FooterLink("Research", "app:${Routes.RESEARCH}"),
    FooterLink("Contact", "app:${Routes.CONTACT}"),
)
private val ProjectLinks = listOf(
    FooterLink("Our apps", "app:${Routes.PROJECTS}"),
    FooterLink("Teardown ↗", Links.TEARDOWN),
    FooterLink("Susu ↗", Links.SUSU),
)

/** "Learn. Repair. Reduce." — the footer every website page ends with. */
@Composable
fun SiteFooter(modifier: Modifier = Modifier, showNewsletter: Boolean = true, showSiteLinks: Boolean = true) {
    val actions = LocalAppActions.current
    Band(modifier = modifier, background = TaaColors.Night, contentColor = TaaColors.Cream, top = 56.dp, bottom = 32.dp) {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(color = TaaColors.Cyan)) { append("LEARN.") }
                append("\nREPAIR. ")
                withStyle(SpanStyle(color = TaaColors.Yellow)) { append("REDUCE.") }
            },
            style = TaaType.Hero.copy(fontSize = TaaType.Section.fontSize * 1.35f, lineHeight = TaaType.Section.fontSize * 1.3f),
            modifier = Modifier.padding(bottom = 32.dp).semantics { heading() },
        )

        if (showNewsletter) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(TaaColors.Cream.copy(alpha = 0.06f))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    buildAnnotatedString {
                        append("Between workshops")
                        withStyle(SpanStyle(color = TaaColors.Yellow)) { append(".") }
                    },
                    style = TaaType.Title,
                )
                Text(
                    "New chapter announcements, workshop schedule changes, e-waste drive dates. That's the whole email.",
                    style = TaaType.Small,
                    modifier = Modifier.alpha(0.75f).padding(bottom = 8.dp),
                )
                NewsletterSignup(onDark = true)
            }
        }

        FooterHeading("Get in touch", TaaColors.Coral, Modifier.padding(top = 32.dp))
        FooterLinkText(Links.CONTACT_EMAIL) { actions.email(Links.CONTACT_EMAIL) }
        FooterLinkText(Links.RONIT_EMAIL) { actions.email(Links.RONIT_EMAIL) }
        FooterLinkText(Links.TANAY_EMAIL) { actions.email(Links.TANAY_EMAIL) }
        Text(Links.MAIL_ADDRESS, style = TaaType.Small, modifier = Modifier.alpha(0.7f).padding(vertical = 8.dp))
        FooterLinkText("${Links.INSTAGRAM_HANDLE} ↗") { actions.openUrl(Links.INSTAGRAM) }

        if (showSiteLinks) {
            FooterColumns(actions::open)
        }

        HorizontalDivider(Modifier.padding(top = 24.dp), color = TaaColors.Cream.copy(alpha = 0.15f))
        Text(
            "© 2026 Tech Awareness Association — a 501(c)(3) nonprofit",
            style = TaaType.Small,
            modifier = Modifier.alpha(0.55f).padding(top = 20.dp),
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalArrangement = Arrangement.Center) {
            FooterLinkText("Privacy Policy") { actions.navigate(Routes.PRIVACY) }
            Box(Modifier.heightIn(min = 48.dp), contentAlignment = Alignment.CenterStart) {
                Text("Teach, don't fix-for.", style = TaaType.Small, modifier = Modifier.alpha(0.55f))
            }
        }
    }
}

@Composable
private fun FooterColumns(open: (String) -> Unit) {
    FlowRow(
        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        FooterGroup("Site", TaaColors.Yellow, SiteLinks, open)
        FooterGroup("Explore", TaaColors.Cyan, ExploreLinks, open)
        FooterGroup("Projects", TaaColors.Coral, ProjectLinks, open)
    }
}

@Composable
private fun FooterGroup(title: String, color: Color, links: List<FooterLink>, open: (String) -> Unit) {
    Column(Modifier.width(150.dp)) {
        FooterHeading(title, color)
        links.forEach { link -> FooterLinkText(link.label) { open(link.target) } }
    }
}

@Composable
private fun FooterHeading(text: String, color: Color, modifier: Modifier = Modifier) {
    Text(
        text.uppercase(),
        style = TaaType.Label.copy(letterSpacing = TaaType.Eyebrow.letterSpacing),
        color = color,
        modifier = modifier.padding(bottom = 4.dp).semantics { heading() },
    )
}

@Composable
private fun FooterLinkText(text: String, onClick: () -> Unit) {
    Box(
        Modifier
            .heightIn(min = 44.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(text, style = TaaType.Small.copy(fontWeight = FontWeight.Medium), color = TaaColors.Cream)
    }
}
