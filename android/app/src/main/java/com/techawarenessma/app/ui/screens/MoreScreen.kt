package com.techawarenessma.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.techawarenessma.app.links.Links
import com.techawarenessma.app.navigation.Routes
import com.techawarenessma.app.ui.LocalAppActions
import com.techawarenessma.app.ui.components.Band
import com.techawarenessma.app.ui.components.Headline
import com.techawarenessma.app.ui.components.SiteFooter
import com.techawarenessma.app.ui.components.TaaIcons
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType

private data class MoreLink(val title: String, val subtitle: String, val target: String)

private data class MoreGroup(val title: String, val links: List<MoreLink>)

/** Everything the website reaches from its nav bar and footer that isn't a bottom tab. */
private val MoreGroups = listOf(
    MoreGroup(
        "Get involved",
        listOf(
            MoreLink("Start a Chapter", "5 towns → 50 towns in a year", "app:${Routes.START_CHAPTER}"),
            MoreLink("Get Involved", "Five ways in, no application form", "app:${Routes.GET_INVOLVED}"),
            MoreLink("Donate", "Money or machines — both work", "app:${Routes.DONATE}"),
        ),
    ),
    MoreGroup(
        "Explore",
        listOf(
            MoreLink("About & Why Repair", "We got told no. We built it anyway.", "app:${Routes.ABOUT}"),
            MoreLink("Partners & Sponsors", "iFixit and Granite Telecommunications", "app:${Routes.PARTNERS}"),
            MoreLink("Research", "The economics of donated e-waste", "app:${Routes.RESEARCH}"),
            MoreLink("Contact", "A human answers, usually the same day", "app:${Routes.CONTACT}"),
            MoreLink("Privacy Policy", "Your data, plainly", "app:${Routes.PRIVACY}"),
        ),
    ),
    MoreGroup(
        "Projects",
        listOf(
            MoreLink("Our apps", "Teardown and Susu", "app:${Routes.PROJECTS}"),
            MoreLink("Teardown ↗", "teardown.techawarenessma.com", Links.TEARDOWN),
            MoreLink("Susu ↗", "susu.techawarenessma.com", Links.SUSU),
            MoreLink("Instagram ↗", Links.INSTAGRAM_HANDLE, Links.INSTAGRAM),
        ),
    ),
    MoreGroup(
        "For chapter leads",
        listOf(
            MoreLink("Chapter Lead Certification", "Access code required · issued after approval", "app:${Routes.CERTIFICATION}"),
        ),
    ),
)

@Composable
fun MoreScreen() {
    val actions = LocalAppActions.current
    ScrollingPage {
        item(key = "header") {
            Band(top = 20.dp, bottom = 8.dp) {
                Headline("More", style = TaaType.PageTitle)
            }
        }
        MoreGroups.forEach { group ->
            item(key = group.title) {
                Band(top = 20.dp, bottom = 8.dp) {
                    Text(
                        group.title.uppercase(),
                        style = TaaType.Eyebrow,
                        color = TaaColors.Red,
                        modifier = Modifier.padding(bottom = 8.dp).semantics { heading() },
                    )
                    Column(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(TaaColors.Sand),
                    ) {
                        group.links.forEachIndexed { index, link ->
                            if (index > 0) HorizontalDivider(color = TaaColors.Cream, thickness = 2.dp)
                            MoreRow(link) { actions.open(link.target) }
                        }
                    }
                }
            }
        }
        item(key = "footer") {
            SiteFooter(modifier = Modifier.padding(top = 32.dp), showSiteLinks = false)
        }
    }
}

@Composable
private fun MoreRow(link: MoreLink, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(Modifier.weight(1f)) {
            Text(link.title, style = TaaType.Title, color = TaaColors.Ink)
            Text(link.subtitle, style = TaaType.Small, modifier = Modifier.alpha(0.7f))
        }
        Icon(TaaIcons.ChevronRight, contentDescription = null, tint = TaaColors.Slate)
    }
}
