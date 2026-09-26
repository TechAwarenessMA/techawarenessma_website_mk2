@file:OptIn(ExperimentalLayoutApi::class)

package com.techawarenessma.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techawarenessma.app.R
import com.techawarenessma.app.links.Links
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
import com.techawarenessma.app.ui.components.TagPill
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType

// ---------------------------------------------------------------- About

@Composable
fun AboutScreen() {
    val actions = LocalAppActions.current
    ScrollingPage {
        item(key = "header") {
            PageHeader(eyebrow = "About", first = "We got told no.", second = "We built it anyway.", accent = TaaColors.Gold)
        }
        item(key = "origin") {
            Band(top = 0.dp, bottom = 48.dp) {
                Eyebrow("How it started — two true stories", color = TaaColors.Blue)
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    PillCard(
                        "The favor that grew", TaaColors.Cyan, title = "",
                        body = "It started with fixing phones for friends. Then their friends. Then people we'd never met were showing up with cracked screens and dead batteries — and instead of fixing them for people, we started showing them how.",
                    )
                    PillCard(
                        "The rejection", TaaColors.Coral, title = "",
                        body = "We pitched this as a school club. The school said no. So we skipped the club, filed for a 501(c)(3), and built it town by town instead. The \"no\" is the reason this is bigger than a classroom.",
                        background = TaaColors.Ink,
                        contentColor = TaaColors.Cream,
                    )
                }
            }
        }
        item(key = "philosophy") {
            Band(background = TaaColors.Espresso, contentColor = TaaColors.Cream, top = 56.dp, bottom = 56.dp) {
                SplitHeadline("Teach,", "don't fix-for.", accent = TaaColors.Red, color = TaaColors.Cream)
                Paragraph(
                    "We could fix your phone in ten minutes and you'd be back in six months. Instead we hand you the driver and sit next to you. It takes an hour. You never come back for the same problem — and usually, you come back to help someone else with theirs.",
                    style = TaaType.BodyLarge,
                    modifier = Modifier.padding(top = 24.dp),
                )
            }
        }
        item(key = "why") {
            Band(top = 56.dp, bottom = 32.dp) {
                Eyebrow("Why repair", color = TaaColors.GoldDeep)
                Text(
                    "iFixit's manifesto puts it bluntly: \"If you can't fix it, you don't own it.\"",
                    style = TaaType.Subsection,
                    color = TaaColors.Ink,
                )
                RichText(
                    "— iFixit, *Self-Repair Manifesto*, CC BY-NC-SA 3.0. [Read the whole thing →](${Links.IFIXIT_MANIFESTO})",
                    style = TaaType.Small,
                    modifier = Modifier.padding(top = 12.dp, bottom = 28.dp).alpha(0.75f),
                )
                Paragraph("We'd put it our own way: everyone has a reasonable claim to open, understand, and fix the things they own. Here's what that gets you —")
                Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(top = 8.dp)) {
                    PillCard("01", TaaColors.Yellow, "It's yours", "Repair is independence, not just thrift. A device you can open is a device you actually own — not one you rent from whoever holds the parts and the passwords.")
                    PillCard("02", TaaColors.Cyan, "It teaches you", "Take a phone apart once and you understand it forever. Repair is the fastest honest way to learn how the things you use every day actually work.")
                    PillCard("03", TaaColors.Coral, "It stays here", "A repaired device skips the landfill, and its value stays in town — it still works, it multiplies through giveaways, and it keeps working for a neighbor.")
                }
            }
        }
        item(key = "cta") {
            Band(top = 24.dp, bottom = 56.dp) {
                SandCard(background = TaaColors.Ink, contentColor = TaaColors.Cream, spacing = 16.dp) {
                    Text(
                        buildAnnotatedString {
                            append("SOLD")
                            withStyle(SpanStyle(color = TaaColors.Yellow)) { append("?") }
                            append(" COME OPEN SOMETHING.")
                        },
                        style = TaaType.Subsection,
                        modifier = Modifier.semantics { heading() },
                    )
                    PillButton("Find a workshop →", onClick = { actions.navigate(Routes.CHAPTERS) }, style = PillStyle.Yellow)
                    PillButton("Meet the team →", onClick = { actions.navigate(Routes.MEMBERS) }, style = PillStyle.OutlineLight)
                }
            }
        }
    }
}

// ---------------------------------------------------------------- Partners

@Composable
fun PartnersScreen() {
    val actions = LocalAppActions.current
    ScrollingPage {
        item(key = "header") {
            PageHeader(eyebrow = "Partners & Sponsors", eyebrowColor = TaaColors.Blue, first = "Backed by people", second = "who get it.", accent = TaaColors.Blue)
        }
        item(key = "partners") {
            Band(top = 0.dp, bottom = 48.dp) {
                SandCard(spacing = 14.dp, modifier = Modifier.padding(bottom = 16.dp)) {
                    Box(
                        Modifier.fillMaxWidth().height(110.dp).clip(RoundedCornerShape(16.dp)).background(Color.White),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painterResource(R.drawable.logo_ifixit),
                            contentDescription = "iFixit logo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.height(70.dp).fillMaxWidth(0.7f),
                        )
                    }
                    TitleWithPill("iFixit", "Partner", TaaColors.Yellow)
                    RichText(
                        "The 30 Pro Tech Toolkits and repair mats on every bench, and the open repair guides we teach from — both in the physical workshops and inside [Teardown](${Links.TEARDOWN}), where their guide photos appear with permission. Their [Self-Repair Manifesto](${Links.IFIXIT_MANIFESTO}) is half the reason we exist.",
                        style = TaaType.Body,
                    )
                }
                SandCard(spacing = 14.dp) {
                    GraniteWordmark()
                    TitleWithPill("Granite Telecommunications", "Hardware donor", TaaColors.Cyan)
                    RichText(
                        "Donated ~110 desktops and laptops — the hardware base for everything we do. Those machines get repaired in workshops, taught on weekly, and refurbished to give away. The same batch powers our [e-waste economics research](app:${Routes.RESEARCH}).",
                        style = TaaType.Body,
                    )
                }
            }
        }
        item(key = "tiers") {
            Band(background = TaaColors.Ink, contentColor = TaaColors.Cream, top = 48.dp, bottom = 56.dp) {
                Headline("Sponsor something real", color = TaaColors.Cream, punctuationColor = TaaColors.Yellow, modifier = Modifier.padding(bottom = 24.dp))
                Column(verticalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.padding(bottom = 24.dp)) {
                    val tierBackground = TaaColors.Cream.copy(alpha = 0.07f)
                    TierCard("$250", TaaColors.Yellow, "Event Sponsor", "Puts your name on an e-waste drive or a Fix-a-Thon night.", background = tierBackground, contentColor = TaaColors.Cream)
                    TierCard("$1,000", TaaColors.Cyan, "Bench Sponsor", "Equips and stocks a full workshop bench for a season.", background = tierBackground, contentColor = TaaColors.Cream)
                    TierCard("$5,000", TaaColors.Coral, "Chapter Sponsor", "Launches a whole new town — toolkits, devices, training, everything.", background = tierBackground, contentColor = TaaColors.Cream)
                }
                PillButton("Talk sponsorship →", onClick = { actions.email(Links.RONIT_EMAIL, subject = "Sponsorship") }, style = PillStyle.Yellow)
            }
        }
    }
}

// ---------------------------------------------------------------- Research

@Composable
fun ResearchScreen() {
    val actions = LocalAppActions.current
    ScrollingPage {
        item(key = "header") {
            PageHeader(
                eyebrow = "Research",
                eyebrowColor = TaaColors.Blue,
                first = "The data behind",
                second = "the drives.",
                accent = TaaColors.Blue,
                intro = "Our device intake isn't just a program — it's a dataset. This page is for researchers, admissions readers, and anyone who wants the rigorous version.",
            )
        }
        item(key = "paper") {
            Band(top = 0.dp, bottom = 56.dp) {
                SandCard(spacing = 14.dp) {
                    Eyebrow("Working paper — NSRI", color = TaaColors.Blue, modifier = Modifier.padding(bottom = 0.dp))
                    Text(
                        "The economics of donated e-waste: evidence from a 110-device corporate hardware batch",
                        style = TaaType.CardTitle.copy(fontSize = 22.sp, lineHeight = 30.sp),
                        color = TaaColors.Ink,
                        modifier = Modifier.semantics { heading() },
                    )
                    Text(
                        "The paper uses TAA's Granite Telecommunications device batch as a live dataset: intake condition, triage outcome (resell / refurbish / parts / recycle), repair cost, and realized resale value for each unit, tracked through the program's actual operations rather than modeled.",
                        style = TaaType.Body,
                    )
                    Text(
                        "It contributes a data point we have not found published elsewhere: the rate at which donated enterprise hardware arrives dead-on-arrival due to BIOS/UEFI firmware locks — machines that are physically functional but administratively unusable, a distinct and measurable loss category in device-donation pipelines.",
                        style = TaaType.Body,
                    )
                    SandCard(background = TaaColors.Ink, contentColor = TaaColors.Cream, spacing = 6.dp, padding = PaddingValues(20.dp)) {
                        Text("NOVEL FINDING", style = TaaType.Label.copy(letterSpacing = 1.6.sp), color = TaaColors.Yellow)
                        Text("BIOS/UEFI-lock DOA rates in donated corporate hardware — measured, not estimated.", style = TaaType.Title)
                    }
                    PillButton("Request the paper →", onClick = { actions.email(Links.RONIT_EMAIL, subject = "Research inquiry") })
                }
            }
        }
    }
}

// ---------------------------------------------------------------- Projects

@Composable
fun ProjectsScreen() {
    val actions = LocalAppActions.current
    ScrollingPage {
        item(key = "header") {
            PageHeader(
                eyebrow = "Projects",
                first = "We also",
                second = "build software.",
                accent = TaaColors.Blue,
                intro = "Two live apps, both free, both built because the workshops kept running into the same two problems.",
            )
        }
        item(key = "teardown") {
            Band(top = 0.dp, bottom = 24.dp) {
                SandCard(spacing = 14.dp) {
                    TitleWithPill("Teardown", "Live", TaaColors.Cyan)
                    Text("teardown.techawarenessma.com", style = TaaType.Small.copy(fontWeight = FontWeight.SemiBold), color = TaaColors.Blue)
                    Text(
                        "A gamified device-repair simulator. Drag-and-drop tools modeled on the iFixit kit, real iFixit guide photos (used with permission), and mistakes that cost what they'd actually cost — in real dollars. Built for facilitator-led, single-session workshops.",
                        style = TaaType.Body,
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        listOf(
                            "25" to "guided repairs",
                            "5" to "levels, ordered by iFixit repairability score",
                            "$" to "mistake costs shown in real dollars",
                            "XP" to "badges + optional account to save progress",
                        ).forEach { (value, label) ->
                            Row(
                                Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(TaaColors.Ink).padding(horizontal = 18.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                            ) {
                                Text(value, style = TaaType.CardTitle.copy(fontWeight = FontWeight.ExtraBold), color = TaaColors.Cyan, modifier = Modifier.widthIn(min = 36.dp))
                                Text(label, style = TaaType.Small, color = TaaColors.Cream)
                            }
                        }
                    }
                    PillButton("Play Teardown →", onClick = { actions.openUrl(Links.TEARDOWN) })
                }
            }
        }
        item(key = "susu") {
            Band(top = 0.dp, bottom = 56.dp) {
                SandCard(spacing = 14.dp) {
                    TitleWithPill("Susu", "Live", TaaColors.Cyan)
                    Text(
                        "susu.techawarenessma.com — \"Seniors Understanding Smart Use\"",
                        style = TaaType.Small.copy(fontWeight = FontWeight.SemiBold),
                        color = TaaColors.Blue,
                    )
                    Text(
                        "A free, self-paced digital-literacy tool for seniors. Age-based onboarding, no jargon, no judgment, no charge — the Senior Tech Support program, for the six days a week we're not sitting next to you.",
                        style = TaaType.Body,
                    )
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Just Getting Started", "Getting the Hang of It", "Feeling Pretty Confident").forEach { OutlineChip(it) }
                    }
                    PillButton("Try Susu →", onClick = { actions.openUrl(Links.SUSU) })
                }
            }
        }
    }
}

// ---------------------------------------------------------------- Contact

private data class ContactCard(val tag: String, val color: Color, val email: String, val detail: String)

private val ContactCards = listOf(
    ContactCard("Everything else", TaaColors.Coral, Links.CONTACT_EMAIL, "Workshops, volunteering, devices, questions."),
    ContactCard("Partnerships & chapters", TaaColors.Yellow, Links.RONIT_EMAIL, "Ronit Sharma, Executive Director."),
    ContactCard("Programs & curriculum", TaaColors.Cyan, Links.TANAY_EMAIL, "Tanay Mangal, Programs & Education."),
)

@Composable
fun ContactScreen() {
    val actions = LocalAppActions.current
    ScrollingPage {
        item(key = "header") {
            Band(top = 20.dp, bottom = 32.dp) {
                Eyebrow("Contact", color = TaaColors.GoldDeep)
                Headline("Say hi", style = TaaType.PageTitle)
                Paragraph("A human answers all three of these. Usually the same day.", style = TaaType.BodyLarge, modifier = Modifier.padding(top = 20.dp))
            }
        }
        item(key = "cards") {
            Band(top = 0.dp, bottom = 56.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    ContactCards.forEach { card ->
                        SandCard(
                            spacing = 10.dp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .clickable(role = Role.Button, onClickLabel = "Email ${card.email}") { actions.email(card.email) },
                        ) {
                            TagPill(card.tag, card.color)
                            Text(card.email, style = TaaType.CardTitle, color = TaaColors.Ink)
                            Text(card.detail, style = TaaType.Small, modifier = Modifier.alpha(0.75f))
                        }
                    }
                    SandCard(background = TaaColors.Cream, spacing = 8.dp, modifier = Modifier.padding(top = 8.dp)) {
                        Text("MAIL", style = TaaType.Label.copy(letterSpacing = 1.6.sp), modifier = Modifier.alpha(0.6f))
                        Text(Links.MAIL_ADDRESS, style = TaaType.Title)
                        RichText("Workshop visits: just show up — see [Chapters](app:${Routes.CHAPTERS}).", style = TaaType.Body)
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------- Privacy

@Composable
fun PrivacyScreen() {
    val actions = LocalAppActions.current
    ScrollingPage {
        item(key = "header") {
            PageHeader(
                eyebrow = "Privacy policy",
                first = "Your data,",
                second = "plainly.",
                accent = TaaColors.Blue,
                intro = "We're a student-run nonprofit, not an ad company. Here's everything we collect, in plain language — this covers techawarenessma.com and our two apps, Teardown and Susu.",
            )
        }
        item(key = "policy") {
            Band(top = 0.dp, bottom = 56.dp) {
                Headline("What we collect", style = TaaType.Subsection, modifier = Modifier.padding(bottom = 16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(bottom = 32.dp)) {
                    listOf(
                        "**An age range on Susu** — asked once during onboarding, so the lessons start at the right level. That's the only thing Susu asks for.",
                        "**An email and your progress on Teardown** — only if you choose to make an account to save your repairs. No account, no data.",
                        "**What you send us** — contact emails, volunteer and chapter-request submissions.",
                        "**A newsletter email** — only if you sign up for updates.",
                    ).forEach { item ->
                        SandCard(padding = PaddingValues(18.dp)) { RichText(item, style = TaaType.Body) }
                    }
                }
                Headline("What it's used for", style = TaaType.Subsection, modifier = Modifier.padding(bottom = 12.dp))
                Paragraph("Making Susu fit your starting level. Saving your Teardown progress. Answering your emails. Sending the updates you asked for — new chapters, schedule changes, e-waste drive dates. That's the whole list.")
                Headline("What we never do", style = TaaType.Subsection, modifier = Modifier.padding(top = 20.dp, bottom = 12.dp))
                Paragraph("We don't sell your data. We don't share it with advertisers. There's no third-party ad targeting anywhere on our sites or apps. We teach people to spot online sketchiness — we're not going to be it.")
                SandCard(background = TaaColors.Ink, contentColor = TaaColors.Cream, spacing = 8.dp, modifier = Modifier.padding(vertical = 20.dp)) {
                    Text("THIS ANDROID APP", style = TaaType.Label.copy(letterSpacing = 1.6.sp), color = TaaColors.Yellow)
                    Text(
                        "No account, no analytics, no ads, and no internet permission. Forms and newsletter signups open your own email app — nothing is sent until you send it. Chapter certification progress is stored only on this device.",
                        style = TaaType.Body,
                    )
                }
                SandCard(spacing = 12.dp) {
                    Text("Questions about any of this?", style = TaaType.Title)
                    PillButton("${Links.CONTACT_EMAIL} →", onClick = { actions.email(Links.CONTACT_EMAIL, subject = "Privacy question") })
                }
            }
        }
    }
}
