package com.techawarenessma.app.certification

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CertificationTest {

    @Test
    fun accessCodesFollowTheWebsitesPrototypeRule() {
        listOf("TAA-1234", "taa-abc", "  TAA-XYZ9  ", "TAA-ABCDEFGH12").forEach {
            assertTrue("expected valid: '$it'", isValidAccessCode(it))
        }
        listOf("", "TAA-", "TAA-12", "TAA_1234", "TAA-12 34", "ABC-1234", "TAA-12!4", "XTAA-1234").forEach {
            assertFalse("expected invalid: '$it'", isValidAccessCode(it))
        }
    }

    @Test
    fun freshStepperMatchesTheWebsite() {
        val stages = pathStages(CertificationState())
        assertEquals(9, stages.size)
        assertEquals(listOf("✓", "✓", "0/5", "4", "5", "6", "7", "8", "9"), stages.map { it.mark })
        assertEquals(
            listOf(
                StageStatus.Done, StageStatus.Done, StageStatus.Current, StageStatus.Locked, StageStatus.Upcoming,
                StageStatus.Upcoming, StageStatus.Upcoming, StageStatus.Upcoming, StageStatus.Upcoming,
            ),
            stages.map { it.status },
        )
        assertEquals("Full charter · 3 workshops", stages.last().label)
    }

    @Test
    fun stepperFillsInAsProgressIsMade() {
        val partial = pathStages(CertificationState(doneModules = setOf("m1", "m3")))
        assertEquals("2/5", partial[2].mark)

        val complete = pathStages(
            CertificationState(
                doneModules = CertSection.modules.map { it.id }.toSet(),
                certItems = CertItems.map { it.id }.toSet(),
            ),
        )
        assertEquals(StageStatus.Done, complete[2].status)
        assertEquals(StageStatus.Done, complete[4].status)
        assertEquals("✓", complete[4].mark)
        // The knowledge check stays locked: its content is still pending the full handbook.
        assertEquals(StageStatus.Locked, complete[3].status)
    }

    @Test
    fun countsIgnoreUnknownIds() {
        val state = CertificationState(doneModules = setOf("m1", "bogus", "p7"), certItems = setOf("kc", "nope"))
        assertEquals(1, state.doneCount)
        assertEquals(1, state.certCount)
    }

    @Test
    fun sectionsRunInCourseOrder() {
        assertEquals(12, CertSection.entries.size)
        assertEquals(listOf("m1", "m2", "m3", "m4", "m5"), CertSection.modules.map { it.id })
        assertNull(CertSection.HowToUse.previous)
        assertEquals(CertSection.WhatTaaIs, CertSection.HowToUse.next)
        assertEquals(CertSection.P7, CertSection.M5.next)
        assertNull(CertSection.P9.next)
        assertEquals(CertSection.M3, CertSection.byId("m3"))
        assertNull(CertSection.byId("missing"))
        assertEquals("M4", CertSection.M4.shortName)
    }

    @Test
    fun sixSubmissionItemsWithUniqueIds() {
        assertEquals(6, CertItems.size)
        assertEquals(6, CertItems.map { it.id }.toSet().size)
    }
}
