package com.techawarenessma.app.content

import com.techawarenessma.app.ui.theme.TaaColors
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SiteContentTest {

    @Test
    fun membersHaveUniqueIdsAndTheNextLinkWraps() {
        assertEquals(15, Members.size)
        assertEquals(Members.size, Members.map { it.id }.toSet().size)
        assertEquals("tanay", nextMember(Members.first()).id)
        assertEquals(Members.first(), nextMember(Members.last()))
        assertEquals("Ronit Sharma", memberById("ronit")?.name)
        assertNull(memberById("nobody"))
    }

    @Test
    fun yellowRoleLinesUseTheReadableGold() {
        val ronit = memberById("ronit")!!
        assertEquals(TaaColors.Yellow, ronit.accent)
        assertEquals(TaaColors.GoldText, ronit.roleColor)
        assertEquals(TaaColors.Blue, memberById("tanay")!!.roleColor)
    }

    @Test
    fun threeLiveChaptersAndThreeForming() {
        assertEquals(6, Chapters.size)
        assertEquals(listOf("shrewsbury", "grafton", "southgate"), Chapters.filter { it.live }.map { it.id })
        // Forming chapters have no photos yet, and every live one does.
        assertTrue(Chapters.all { (it.photo != null) == it.live })
    }

    @Test
    fun twelveWeekCurriculum() {
        assertEquals(12, CurriculumWeeks.size)
        assertEquals("Capstone Fix-a-Thon — Community Repair Night", CurriculumWeeks.last())
    }

    @Test
    fun benchToolsAndStats() {
        assertEquals(6, BenchTools.size)
        assertEquals(BenchTools.size, BenchTools.map { it.id }.toSet().size)
        assertEquals(listOf(442, 1545, 24, 100, 120), ImpactStats.map { it.value })
    }
}
