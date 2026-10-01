package com.techawarenessma.app.links

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EmailDraftTest {

    @Test
    fun mailtoUriEncodesSpacesAsPercent20AndNewlines() {
        val draft = EmailDraft("contact@techawarenessma.com", subject = "Start a chapter: Grafton", body = "Town: Grafton\nName: A B")
        assertEquals(
            "mailto:contact@techawarenessma.com?subject=Start%20a%20chapter%3A%20Grafton&body=Town%3A%20Grafton%0AName%3A%20A%20B",
            draft.toMailtoUri(),
        )
    }

    @Test
    fun mailtoUriWithoutSubjectOrBodyHasNoQuery() {
        assertEquals("mailto:ronit@techawarenessma.com", EmailDraft("ronit@techawarenessma.com").toMailtoUri())
    }

    @Test
    fun parsesTheWebsitesMailtoHrefs() {
        val draft = EmailDraft.fromMailto("mailto:contact@techawarenessma.com?subject=Device%20donation")
        assertEquals(EmailDraft("contact@techawarenessma.com", subject = "Device donation"), draft)

        val handbook = EmailDraft.fromMailto(
            "mailto:contact@techawarenessma.com?subject=Full%20handbook%20request%20(Part%208%20%2B%20appendices)",
        )
        assertEquals("Full handbook request (Part 8 + appendices)", handbook?.subject)
    }

    @Test
    fun roundTripsThroughMailtoUri() {
        val original = EmailDraft("a@b.co", subject = "Hi & bye + more", body = "Line 1\nLine 2 — ünïcode")
        assertEquals(original, EmailDraft.fromMailto(original.toMailtoUri()))
    }

    @Test
    fun rejectsNonMailtoAndEmptyAddresses() {
        assertNull(EmailDraft.fromMailto("https://techawarenessma.com"))
        assertNull(EmailDraft.fromMailto("mailto:?subject=Hi"))
    }

    @Test
    fun newsletterDraftMatchesTheWebsiteForm() {
        val draft = newsletterDraft("  you@example.com ")
        assertEquals(Links.CONTACT_EMAIL, draft.to)
        assertEquals("Newsletter signup", draft.subject)
        assertEquals("Please add me to the updates list: you@example.com", draft.body)
    }

    @Test
    fun startChapterDraftMatchesTheWebsiteForm() {
        val draft = startChapterDraft("Northborough", "Sam Lee", "sam@example.com", "No repair café yet.")
        assertEquals("Start a chapter: Northborough", draft.subject)
        assertEquals(
            "Town: Northborough\nName: Sam Lee\nEmail: sam@example.com\nWhy this town: No repair café yet.",
            draft.body,
        )
    }

    @Test
    fun plausibleEmailCheck() {
        assertTrue(isPlausibleEmail("you@example.com"))
        assertTrue(isPlausibleEmail(" first.last+tag@sub.example.org "))
        assertFalse(isPlausibleEmail(""))
        assertFalse(isPlausibleEmail("you@example"))
        assertFalse(isPlausibleEmail("you example@x.com"))
        assertFalse(isPlausibleEmail("@example.com"))
    }
}
