package com.techawarenessma.app.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RoutesTest {

    @Test
    fun tabRootsAreRecognizedWithOrWithoutArguments() {
        assertEquals(Tab.Home, Tab.forRoute("home"))
        assertEquals(Tab.Programs, Tab.forRoute("programs"))
        assertEquals(Tab.Programs, Tab.forRoute(Routes.programs("seniors")))
        assertEquals(Tab.Programs, Tab.forRoute(Routes.PROGRAMS_PATTERN))
        assertEquals(Tab.More, Tab.forRoute("more"))
    }

    @Test
    fun pushedScreensAreNotTabs() {
        assertNull(Tab.forRoute(Routes.DONATE))
        assertNull(Tab.forRoute(Routes.member("ronit")))
        assertNull(Tab.forRoute(Routes.MEMBER_PATTERN))
        assertNull(Tab.forRoute(null))
    }

    @Test
    fun onlyTheCertificationPlatformHidesTheAppBars() {
        assertTrue(isImmersive(Routes.CERTIFICATION))
        assertTrue(isImmersive(Routes.CERT_SECTION_PATTERN))
        assertFalse(isImmersive(Routes.HOME))
        assertFalse(isImmersive(Routes.START_CHAPTER))
    }

    @Test
    fun routeBuilders() {
        assertEquals("programs?section=ewaste", Routes.programs("ewaste"))
        assertEquals("member/tanay", Routes.member("tanay"))
        assertEquals("certification/m2", Routes.certSection("m2"))
    }
}
