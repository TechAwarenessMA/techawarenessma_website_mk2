package com.techawarenessma.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techawarenessma.app.content.Member
import com.techawarenessma.app.content.Members
import com.techawarenessma.app.content.memberById
import com.techawarenessma.app.content.nextMember
import com.techawarenessma.app.navigation.Routes
import com.techawarenessma.app.ui.LocalAppActions
import com.techawarenessma.app.ui.components.Band
import com.techawarenessma.app.ui.components.Eyebrow
import com.techawarenessma.app.ui.components.Paragraph
import com.techawarenessma.app.ui.components.Photo
import com.techawarenessma.app.ui.components.PillButton
import com.techawarenessma.app.ui.components.PillStyle
import com.techawarenessma.app.ui.components.SandCard
import com.techawarenessma.app.ui.components.SplitHeadline
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType

@Composable
fun MembersScreen() {
    val actions = LocalAppActions.current
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 40.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        item(key = "header", span = { GridItemSpan(maxLineSpan) }) {
            Column {
                SplitHeadline("Meet the", "members.", accent = TaaColors.Red)
                Paragraph(
                    "One big team, and everyone fixes things on a school night. Tap a face to read what they actually do.",
                    style = TaaType.BodyLarge,
                    modifier = Modifier.padding(top = 20.dp),
                )
                Text("TAA TEAM", style = TaaType.Subsection, color = TaaColors.Ink, modifier = Modifier.padding(top = 12.dp).semantics { heading() })
                Text("One large team", style = TaaType.Small, modifier = Modifier.alpha(0.65f))
            }
        }
        items(Members, key = { it.id }) { member ->
            MemberCard(member, onClick = { actions.navigate(Routes.member(member.id)) })
        }
    }
}

@Composable
private fun MemberCard(member: Member, onClick: () -> Unit) {
    Column(
        Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(role = Role.Button, onClickLabel = "Read ${member.firstName}'s profile", onClick = onClick)
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Photo(member.photo, contentDescription = null, modifier = Modifier.fillMaxWidth().aspectRatio(1f), shape = CircleShape)
        Text(member.name, style = TaaType.Title, color = TaaColors.Ink, modifier = Modifier.padding(top = 10.dp))
        Text(member.role, style = TaaType.Small.copy(fontWeight = FontWeight.SemiBold), color = member.roleColor)
        member.does?.let { Text(it, style = TaaType.Small, modifier = Modifier.alpha(0.8f)) }
    }
}

@Composable
fun MemberProfileScreen(
    memberId: String?,
    onOpenMember: (String) -> Unit,
) {
    val actions = LocalAppActions.current
    val member = memberById(memberId) ?: Members.first()
    val next = nextMember(member)
    LazyColumn(Modifier.fillMaxSize()) {
        item(key = member.id) {
            Band(top = 24.dp, bottom = 56.dp) {
                Photo(
                    member.photo,
                    contentDescription = "Photo of ${member.name}",
                    modifier = Modifier.size(200.dp).align(Alignment.CenterHorizontally),
                    shape = CircleShape,
                )
                Eyebrow(member.role, color = member.roleColor, modifier = Modifier.padding(top = 28.dp))
                Text(
                    member.name.uppercase(),
                    style = TaaType.PageTitle,
                    color = TaaColors.Ink,
                    modifier = Modifier.padding(bottom = 20.dp).semantics { heading() },
                )
                Paragraph(member.bio, style = TaaType.BodyLarge)
                if (member.work.isNotEmpty()) {
                    Eyebrow("What ${member.firstName} does", color = TaaColors.Blue, modifier = Modifier.padding(top = 12.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        member.work.forEach { work ->
                            SandCard(spacing = 4.dp, padding = PaddingValues(18.dp)) {
                                Text(work.title, style = TaaType.Title)
                                Text(work.detail, style = TaaType.Small)
                            }
                        }
                    }
                }
                Column(Modifier.padding(top = 28.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    PillButton("Get in touch", onClick = { actions.navigate(Routes.CONTACT) })
                    PillButton("Next: ${next.name} →", onClick = { onOpenMember(next.id) }, style = PillStyle.Outline)
                }
            }
        }
    }
}
