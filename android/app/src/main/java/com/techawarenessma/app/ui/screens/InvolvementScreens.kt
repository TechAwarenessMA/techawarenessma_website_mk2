@file:OptIn(ExperimentalLayoutApi::class)

package com.techawarenessma.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.techawarenessma.app.links.Links
import com.techawarenessma.app.links.isPlausibleEmail
import com.techawarenessma.app.links.startChapterDraft
import com.techawarenessma.app.navigation.Routes
import com.techawarenessma.app.ui.LocalAppActions
import com.techawarenessma.app.ui.components.Band
import com.techawarenessma.app.ui.components.Eyebrow
import com.techawarenessma.app.ui.components.Headline
import com.techawarenessma.app.ui.components.OutlineChip
import com.techawarenessma.app.ui.components.Paragraph
import com.techawarenessma.app.ui.components.PillButton
import com.techawarenessma.app.ui.components.PillStyle
import com.techawarenessma.app.ui.components.RichText
import com.techawarenessma.app.ui.components.SandCard
import com.techawarenessma.app.ui.components.SplitHeadline
import com.techawarenessma.app.ui.components.TaaTextField
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType

// ---------------------------------------------------------------- Start a Chapter

@Composable
fun StartChapterScreen() {
    ScrollingPage {
        item(key = "header") {
            PageHeader(
                eyebrow = "Start a Chapter · 5 towns → 50 towns in a year",
                first = "Your town",
                second = "is next.",
                accent = TaaColors.Red,
                intro = "We run 5 towns today. We want 50 within a year — and every one of them starts the same way: one person from that town saying yes. Not a repair expert. Not a nonprofit veteran. One person.",
            )
        }
        item(key = "lead") { WhatALeadDoes() }
        item(key = "provides") { WhatTaaProvides() }
        item(key = "playbook") { Playbook() }
        item(key = "form") { StartChapterForm() }
    }
}

@Composable
private fun WhatALeadDoes() {
    Band(background = TaaColors.Ink, contentColor = TaaColors.Cream, top = 48.dp, bottom = 40.dp) {
        Headline("What a chapter lead does", color = TaaColors.Cream, punctuationColor = TaaColors.Yellow, modifier = Modifier.padding(bottom = 24.dp))
        listOf(
            Triple("01", TaaColors.Yellow, "Runs the local sessions" to "One hour a week, same room, same time. You work through the 12-week curriculum with whoever shows up."),
            Triple("02", TaaColors.Cyan, "Is the town's point of contact" to "The library knows your name, the regulars know your face. You're the local end of the operation."),
            Triple("03", TaaColors.Coral, "Gets trained first" to "Before your first session, we train you — the curriculum, the tools, the way we teach. You're never winging it."),
        ).forEach { (number, color, copy) ->
            Column(Modifier.padding(bottom = 24.dp)) {
                Text(number, style = TaaType.BigNumber, color = color)
                Text(copy.first, style = TaaType.CardTitle, modifier = Modifier.padding(vertical = 4.dp))
                Text(copy.second, style = TaaType.Body, modifier = Modifier.alpha(0.85f))
            }
        }
    }
}

@Composable
private fun WhatTaaProvides() {
    Band(top = 48.dp, bottom = 24.dp) {
        Headline("You're not building\nfrom scratch", modifier = Modifier.padding(bottom = 16.dp))
        Paragraph("Everything a chapter needs, we already have. It arrives with you:")
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf(
                "A full iFixit toolkit" to "Pro Tech Toolkits and mats for every bench.",
                "The 12-week curriculum" to "Written, tested, running in every live town. [See it →](app:${Routes.PROGRAMS})",
                "Training" to "You run your first session after we've run it with you.",
                "Ongoing support" to "Devices, parts, and a team one text away when something breaks.",
            ).forEach { (title, body) ->
                SandCard(spacing = 6.dp, padding = PaddingValues(20.dp)) {
                    Text(title, style = TaaType.Title)
                    RichText(body, style = TaaType.Body)
                }
            }
        }
    }
}

@Composable
private fun Playbook() {
    Band(top = 24.dp, bottom = 24.dp) {
        SandCard(spacing = 16.dp) {
            Headline("Same playbook, every town", punctuationColor = TaaColors.Cyan, style = TaaType.Subsection)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("One room", "One hour a week", "No membership fee", "No parachuting in from outside").forEach { OutlineChip(it) }
            }
            Text(
                "It's your town's chapter, run by someone from your town. We just make sure you never run it alone.",
                style = TaaType.Body,
            )
        }
    }
}

/**
 * The "Raise your hand" form. Same as the website: nothing is posted anywhere; it opens
 * the user's mail app with the request addressed to the contact inbox.
 */
@Composable
private fun StartChapterForm() {
    val actions = LocalAppActions.current
    var town by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var why by rememberSaveable { mutableStateOf("") }
    var showErrors by rememberSaveable { mutableStateOf(false) }

    val townError = if (showErrors && town.isBlank()) "Tell us which town." else null
    val nameError = if (showErrors && name.isBlank()) "Add your name so we know who to write back to." else null
    val emailError = if (showErrors && !isPlausibleEmail(email)) "Enter an email address like you@example.com." else null

    Band(top = 24.dp, bottom = 56.dp) {
        Headline("Raise your hand", modifier = Modifier.padding(bottom = 16.dp))
        Paragraph("Tell us your town and why it needs a chapter. This goes straight to ${Links.CONTACT_EMAIL} — a human replies, usually the same day.")
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            TaaTextField(town, { town = it }, label = "Your town", placeholder = "e.g. Northborough", error = townError, capitalization = KeyboardCapitalization.Words)
            TaaTextField(name, { name = it }, label = "Your name", placeholder = "First and last", error = nameError, capitalization = KeyboardCapitalization.Words)
            TaaTextField(email, { email = it }, label = "Email", placeholder = "you@example.com", error = emailError, keyboardType = KeyboardType.Email)
            TaaTextField(
                why, { why = it },
                label = "Why this town?",
                placeholder = "A line or two is plenty.",
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Default,
                singleLine = false,
                minLines = 4,
            )
            PillButton(
                "Send it →",
                onClick = {
                    showErrors = true
                    if (town.isNotBlank() && name.isNotBlank() && isPlausibleEmail(email)) {
                        actions.email(startChapterDraft(town, name, email, why))
                    }
                },
                modifier = Modifier.padding(top = 4.dp),
            )
            Text(
                "Opens your email app with everything filled in. Nothing is sent until you hit send there.",
                style = TaaType.Small,
                modifier = Modifier.alpha(0.7f),
            )
        }
    }
}

// ---------------------------------------------------------------- Get Involved

@Composable
fun GetInvolvedScreen() {
    val actions = LocalAppActions.current
    ScrollingPage {
        item(key = "header") {
            PageHeader(
                eyebrow = "Get Involved",
                first = "Pick up",
                second = "a driver.",
                accent = TaaColors.Red,
                intro = "Everything we run is run by volunteers. Five ways in — one of them is the reason this page exists.",
            )
        }
        item(key = "lead") {
            Band(background = TaaColors.Espresso, contentColor = TaaColors.Cream, top = 48.dp, bottom = 48.dp) {
                Eyebrow("The job we need most", color = TaaColors.Yellow)
                SplitHeadline("Lead a chapter", "in your town.", accent = TaaColors.Yellow, style = TaaType.Section, color = TaaColors.Cream)
                Paragraph(
                    "We're going from 5 towns to 50 in a year, and every one of those towns starts with one person saying yes. You don't need to be a repair expert — you need to show up weekly and care.",
                    modifier = Modifier.padding(top = 20.dp).alpha(0.9f),
                )
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(bottom = 24.dp)) {
                    LinesCard("We bring", listOf("The 12-week curriculum", "iFixit toolkits & mats", "Devices to teach on", "Training & the playbook"), TaaColors.Cyan)
                    LinesCard("You bring", listOf("A library room", "An hour a week", "Word of mouth in your town", "That's it"), TaaColors.Yellow)
                }
                PillButton(
                    "I want to lead a chapter →",
                    onClick = { actions.email(Links.RONIT_EMAIL, subject = "I want to lead a chapter") },
                    style = PillStyle.Yellow,
                )
            }
        }
        item(key = "roles") {
            Band(top = 48.dp, bottom = 24.dp) {
                Headline("The other four jobs", punctuationColor = TaaColors.Blue, modifier = Modifier.padding(bottom = 24.dp))
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    VolunteerRoles.forEach { role -> PillCard(role.tag, role.color, role.title, role.body) }
                }
            }
        }
        item(key = "start") {
            Band(top = 24.dp, bottom = 56.dp) {
                SandCard(spacing = 14.dp) {
                    Headline("No application form", style = TaaType.Subsection)
                    Text("Email us your town and what you want to do. A human writes back — usually the same day.", style = TaaType.Body)
                    PillButton(
                        "${Links.CONTACT_EMAIL} →",
                        onClick = { actions.email(Links.CONTACT_EMAIL, subject = "Volunteering") },
                    )
                }
            }
        }
    }
}

private data class VolunteerRole(val tag: String, val color: Color, val title: String, val body: String)

private val VolunteerRoles = listOf(
    VolunteerRole("Workshop instructor", TaaColors.Yellow, "Teach the weekly sessions", "Walk people through the curriculum, bench by bench. If you've ever fixed a phone at a kitchen table and explained it while you did it — that's the job."),
    VolunteerRole("Device triage & refurb", TaaColors.Cyan, "Sort, fix, and give away", "Work through the donated-device pile: what resells, what refurbs, what becomes parts. The quiet job that funds everything else."),
    VolunteerRole("Outreach & partnerships", TaaColors.Coral, "Get us into new rooms", "Libraries, senior centers, companies with old hardware. If you like talking to people, we'll give you something worth talking about."),
    VolunteerRole("Admin", TaaColors.Yellow, "Keep the wheels on", "Scheduling, inventory, email. Not glamorous — but 24 weekly workshops don't coordinate themselves."),
)

// ---------------------------------------------------------------- Donate

@Composable
fun DonateScreen() {
    val actions = LocalAppActions.current
    ScrollingPage {
        item(key = "header") {
            Band(top = 20.dp, bottom = 32.dp) {
                Eyebrow("Support us — 501(c)(3), status granted", color = TaaColors.GoldDeep)
                SplitHeadline("Fund", "the fix.", accent = TaaColors.Red)
                Paragraph(
                    "Every dollar is tax-deductible, and we take devices as happily as money. Old laptops become teaching hardware; teaching hardware becomes someone's first repair.",
                    style = TaaType.BodyLarge,
                    modifier = Modifier.padding(top = 20.dp),
                )
                DonationChannel()
            }
        }
        item(key = "resale") {
            Band(background = TaaColors.Ink, contentColor = TaaColors.Cream, top = 48.dp, bottom = 40.dp) {
                SplitHeadline("Dead devices", "fund live workshops.", accent = TaaColors.Yellow, style = TaaType.Section, color = TaaColors.Cream)
                Paragraph(
                    "Here's exactly what happens to a donated device: we triage it. Works? We resell it, and **every dollar of proceeds goes directly into the program** — toolkits, parts, new chapters. Fixable? It becomes teaching hardware, then a giveaway. Dead? Its screen and battery live on as parts.",
                    modifier = Modifier.padding(top = 20.dp),
                )
                Paragraph("We tell you this up front because most nonprofits put it in a footnote. It's not a footnote — it's how the whole thing runs.")
            }
        }
        item(key = "tiers") {
            Band(top = 48.dp, bottom = 24.dp) {
                Headline("What a dollar does", modifier = Modifier.padding(bottom = 24.dp))
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    TierCard("$150", TaaColors.Gold, "Start a chapter", "Covers a new chapter's ~$100 startup kit — toolkit, first batch of parts — plus the buffer every launch needs.")
                    TierCard("$250", TaaColors.Blue, "An e-waste drive", "Funds a full device collection drive — bins, transport, and staffing for a town-wide intake day.")
                    TierCard("$500", TaaColors.Red, "5 new chapters", "Seeds five new chapters at $100 each — five more towns with a workshop of their own.")
                    TierCard("$1,000", TaaColors.Blue, "10 new chapters", "Seeds ten new chapters at $100 each — a whole new regional network of workshops.")
                }
            }
        }
        item(key = "devices") {
            Band(top = 24.dp, bottom = 56.dp) {
                SandCard(spacing = 14.dp) {
                    Headline("Got hardware instead", punctuation = "?", punctuationColor = TaaColors.Blue, style = TaaType.Subsection)
                    Text("We can use what we can teach on and fix with — same intake criteria as our Granite batch:", style = TaaType.Body)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Desktops", "Laptops", "Batteries", "Screens", "Parts").forEach { OutlineChip(it) }
                    }
                    PillButton("Donate devices →", onClick = { actions.email(Links.CONTACT_EMAIL, subject = "Device donation") })
                }
            }
        }
    }
}

/**
 * The site's "Primary donation channel" slot. The GoFundMe campaign isn't live yet, so
 * until [Links.DONATION_CAMPAIGN_URL] is set this offers an email instead of a dead link.
 */
@Composable
private fun DonationChannel() {
    val actions = LocalAppActions.current
    val campaign = Links.DONATION_CAMPAIGN_URL
    SandCard(spacing = 10.dp, modifier = Modifier.padding(top = 8.dp)) {
        Eyebrow("Primary donation channel", color = TaaColors.Blue, modifier = Modifier.padding(bottom = 0.dp))
        Text("GoFundMe campaign", style = TaaType.CardTitle, color = TaaColors.Ink)
        if (campaign != null) {
            PillButton("Donate on GoFundMe ↗", onClick = { actions.openUrl(campaign) })
        } else {
            Text(
                "Campaign link pending. Until it's live, email us and we'll tell you how to give.",
                style = TaaType.Body,
            )
            PillButton("Ask about giving →", onClick = { actions.email(Links.CONTACT_EMAIL, subject = "Donation") })
        }
    }
}
