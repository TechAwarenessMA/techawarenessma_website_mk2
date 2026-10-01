package com.techawarenessma.app.ui

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import com.techawarenessma.app.MainActivity
import com.techawarenessma.app.certification.CertSection
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Walks every screen and captures it page by page. Images are only written when recording:
 *
 *     ./gradlew :app:testDebugUnitTest --tests '*ScreenshotTourTest*' -Proborazzi.test.record=true
 *
 * They land in app/build/outputs/roborazzi/. Without the flag the tour still runs as a
 * smoke test that every screen composes and every link in the path works.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w411dp-h914dp-hdpi")
class ScreenshotTourTest {

    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun tabs() {
        capturePages("01_home")
        compose.tapTab("Programs")
        capturePages("02_programs")
        compose.tapTab("Chapters")
        capturePages("03_chapters")
        compose.onNode(hasText("Worcester") and hasClickAction()).performClick()
        compose.settle()
        capturePages("03b_chapters_forming", maxPages = 2)
        compose.tapTab("Members")
        capturePages("04_members")
        compose.onNodeWithText("Ronit Sharma").performClick()
        capturePages("04b_member_profile")
        compose.tapTab("More")
        capturePages("05_more")
    }

    @Test
    fun pagesFromMore() {
        listOf(
            "Start a Chapter" to "06_start_chapter",
            "Get Involved" to "07_get_involved",
            "Donate" to "08_donate",
            "About & Why Repair" to "09_about",
            "Partners & Sponsors" to "10_partners",
            "Research" to "11_research",
            "Our apps" to "12_projects",
            "Contact" to "13_contact",
            "Privacy Policy" to "14_privacy",
        ).forEach { (item, name) ->
            compose.tapTab("More")
            compose.scrollTo(item).performClick()
            capturePages(name)
        }
    }

    @Test
    fun formErrors() {
        compose.tapTab("More")
        compose.onNodeWithText("Start a Chapter").performClick()
        compose.settle()
        compose.scrollTo("Send it →").performClick()
        compose.settle()
        compose.onNode(hasError("Tell us which town.")).performScrollTo()
        compose.settle()
        compose.onRoot().captureRoboImage("build/outputs/roborazzi/15_start_chapter_errors.png")
    }

    @Test
    fun certificationPlatform() {
        compose.tapTab("More")
        compose.scrollTo("Chapter Lead Certification").performClick()
        compose.settle()
        val code = compose.onNode(hasSetTextAction() and hasText("Your access code"))
        code.performTextInput("nope")
        compose.tapButton("Enter →")
        compose.onRoot().captureRoboImage("build/outputs/roborazzi/20_cert_gate_error.png")

        code.performTextClearance()
        code.performTextInput("TAA-2026")
        compose.tapButton("Enter →")
        capturePages("21_cert_overview")

        compose.onNodeWithText("Continue: How to use this →").performClick()
        compose.settle()
        CertSection.entries.forEach { section ->
            capturePages("22_cert_${section.ordinal.toString().padStart(2, '0')}_${section.id}", maxPages = 12)
            val next = section.next ?: return@forEach
            compose.onNode(hasText("Next: ${next.label} →") and hasClickAction()).performScrollTo().performClick()
            compose.settle()
        }
    }

    /** Captures the screen, then pages down its main scroller until the end. */
    private fun capturePages(name: String, maxPages: Int = 16) {
        waitForImages()
        val scroller = compose.onAllNodes(isVerticalScroller).onFirst()
        val pageHeight = compose.onRoot().fetchSemanticsNode().size.height * 0.72f
        for (page in 1..maxPages) {
            compose.onRoot().captureRoboImage("build/outputs/roborazzi/${name}_${page.toString().padStart(2, '0')}.png")
            // Lazy lists only estimate their scroll position, so stop when a page-down leaves
            // the frame unchanged.
            val before = frameHash()
            scroller.performSemanticsAction(SemanticsActions.ScrollBy) { it(0f, pageHeight) }
            waitForImages()
            if (frameHash() == before) break
        }
        // Back to the top so the next capture of this screen starts at the header.
        scroller.performSemanticsAction(SemanticsActions.ScrollBy) { it(0f, -1_000_000f) }
        compose.settle(1)
    }

    private fun frameHash(): Int {
        val image = compose.onRoot().captureToImage()
        val pixels = IntArray(image.width * image.height)
        image.readPixels(pixels)
        return pixels.contentHashCode()
    }

    /** Photos decode on Coil's background threads; give them real time before capturing. */
    private fun waitForImages() {
        repeat(8) {
            Thread.sleep(120)
            compose.settle(1)
        }
    }
}
