package com.techawarenessma.app.ui

import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.techawarenessma.app.MainActivity
import com.techawarenessma.app.certification.ACCESS_CODE_ERROR
import com.techawarenessma.app.links.Links
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/** End-to-end flows through the real activity, one per website feature. */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w411dp-h914dp-hdpi")
class AppFlowTest {

    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun bottomTabsReachEveryTopLevelScreen() {
        compose.onNodeWithText("WE FIX IT").assertIsDisplayed()
        compose.tapTab("Programs")
        compose.onNodeWithText("DON'T FIX-FOR.").assertIsDisplayed()
        compose.tapTab("Chapters")
        compose.onNodeWithText("YOUR TOWN.").assertIsDisplayed()
        compose.tapTab("Members")
        compose.onNodeWithText("Ronit Sharma").assertIsDisplayed()
        compose.tapTab("More")
        compose.onNodeWithText("Start a Chapter").assertIsDisplayed()
        compose.tapTab("Home")
        compose.onNodeWithText("WE FIX IT").assertIsDisplayed()
    }

    @Test
    fun homeProgramLinkOpensProgramsAtThatSection() {
        compose.scrollTo("Senior Tech Support")
        compose.onAllNodesWithText("See how it works →")[1].performScrollTo().performClick()
        compose.settle()
        compose.onNodeWithText("SENIOR TECH SUPPORT").assertIsDisplayed()
    }

    @Test
    fun toolMatShowsTheSelectedTool() {
        compose.scrollTo("Spudger").performClick()
        compose.settle(3)
        compose.onNode(hasText("A stiff nylon stick", substring = true)).assertIsDisplayed()
    }

    @Test
    fun chapterPickerSwapsTheDetailPanel() {
        compose.tapTab("Chapters")
        compose.onNodeWithText("Tuesdays, 7:30–8:30 PM").assertExists()
        compose.onNode(hasText("Grafton") and hasClickAction()).performClick()
        compose.settle(3)
        compose.onNodeWithText("Saturdays, 12:00–1:00 PM").assertExists()
        compose.onNode(hasText("Worcester") and hasClickAction()).performClick()
        compose.settle(3)
        compose.onNodeWithText("Be the one who starts it →").assertExists()
    }

    @Test
    fun memberProfileNextAndBack() {
        compose.tapTab("Members")
        compose.onNodeWithText("Ronit Sharma").performClick()
        compose.settle()
        compose.onNodeWithText("WHAT RONIT DOES").assertExists()
        compose.scrollTo("Next: Tanay Mangal →").performClick()
        compose.settle()
        compose.onNodeWithText("TANAY MANGAL").assertIsDisplayed()
        // "Next" replaced the profile, so Back returns straight to the grid.
        compose.runOnUiThread { compose.activity.onBackPressedDispatcher.onBackPressed() }
        compose.settle()
        compose.onNodeWithText("MEET THE").assertIsDisplayed()
    }

    @Test
    fun startChapterFormValidatesThenOpensAPrefilledEmail() {
        compose.tapTab("More")
        compose.onNodeWithText("Start a Chapter").performClick()
        compose.settle()
        compose.scrollTo("Send it →").performClick()
        compose.settle()
        compose.onNode(hasError("Tell us which town.")).assertExists()
        assertNull("no email should open for an empty form", compose.nextStartedIntent())

        compose.onNode(hasSetTextAction() and hasText("Your town")).performTextInput("Northborough")
        compose.onNode(hasSetTextAction() and hasText("Your name")).performTextInput("Sam Lee")
        compose.onNode(hasSetTextAction() and hasText("Email")).performTextInput("sam@example.com")
        compose.onNode(hasSetTextAction() and hasText("Why this town?")).performTextInput("No repair café yet.")
        compose.onNode(hasText("Send it →") and hasClickAction()).performScrollTo().performClick()
        compose.settle()

        val intent = compose.nextStartedIntent()!!
        assertEquals(Intent.ACTION_SENDTO, intent.action)
        assertTrue(intent.dataString!!.startsWith("mailto:${Links.CONTACT_EMAIL}?subject=Start%20a%20chapter%3A%20Northborough"))
        assertEquals("Start a chapter: Northborough", intent.getStringExtra(Intent.EXTRA_SUBJECT))
        assertEquals(
            "Town: Northborough\nName: Sam Lee\nEmail: sam@example.com\nWhy this town: No repair café yet.",
            intent.getStringExtra(Intent.EXTRA_TEXT),
        )
    }

    @Test
    fun newsletterSignupRejectsTyposAndEmailsTheContactInbox() {
        compose.scrollTo("Sign up →").performClick()
        compose.settle()
        compose.onNode(hasError("Enter an email address like you@example.com.")).assertExists()
        assertNull(compose.nextStartedIntent())

        compose.onNode(hasSetTextAction() and hasText("Email address for updates")).performTextInput("you@example.com")
        compose.onNode(hasText("Sign up →") and hasClickAction()).performClick()
        compose.settle()
        val intent = compose.nextStartedIntent()!!
        assertEquals(Intent.ACTION_SENDTO, intent.action)
        assertEquals("Newsletter signup", intent.getStringExtra(Intent.EXTRA_SUBJECT))
        assertEquals("Please add me to the updates list: you@example.com", intent.getStringExtra(Intent.EXTRA_TEXT))
    }

    @Test
    fun contactCardsOpenTheRightInbox() {
        compose.tapTab("More")
        compose.scrollTo("Contact").performClick()
        compose.settle()
        compose.onNodeWithText(Links.RONIT_EMAIL).performClick()
        compose.settle()
        assertEquals("mailto:${Links.RONIT_EMAIL}", compose.nextStartedIntent()!!.dataString)
    }

    @Test
    fun outsideLinksOpenInTheBrowser() {
        compose.tapTab("More")
        compose.scrollTo("Our apps").performClick()
        compose.settle()
        compose.scrollTo("Play Teardown →").performClick()
        compose.settle()
        val intent = compose.nextStartedIntent()!!
        assertEquals(Intent.ACTION_VIEW, intent.action)
        assertEquals(Links.TEARDOWN, intent.dataString)
    }

    @Test
    fun donatePillOpensDonate() {
        compose.onAllNodes(hasText("Donate") and hasClickAction()).onFirst().performClick()
        compose.settle()
        compose.onNodeWithText("THE FIX.").assertIsDisplayed()
        compose.onNodeWithText("Ask about giving →").assertExists()
    }

    @Test
    fun certificationGateCheckpointAndSignOut() {
        compose.tapTab("More")
        compose.scrollTo("Chapter Lead Certification").performClick()
        compose.settle()

        val codeField = compose.onNode(hasSetTextAction() and hasText("Your access code"))
        codeField.performTextInput("hello")
        compose.tapButton("Enter →")
        compose.onNode(hasError(ACCESS_CODE_ERROR)).assertExists()

        codeField.performTextClearance()
        codeField.performTextInput("TAA-1234")
        compose.tapButton("Enter →")
        compose.onNodeWithText("Continue: How to use this →").assertExists()
        compose.onNode(hasText("0 of 5 module checkpoints done", substring = true)).assertExists()

        compose.scrollTo("M1 · Tools & safety").performClick()
        compose.settle()
        compose.onNodeWithText("TOOLS, WORKBENCH,\nAND SAFETY", substring = true).assertExists()
        compose.scrollTo("Mark M1 checkpoint done").performClick()
        compose.settle()
        compose.onNodeWithText("✓ Checkpoint done — tap to undo").assertExists()

        compose.runOnUiThread { compose.activity.onBackPressedDispatcher.onBackPressed() }
        compose.settle()
        compose.onNode(hasText("1 of 5 module checkpoints done", substring = true)).assertExists()
        compose.onNodeWithText("Continue: M1 · Tools & safety →").assertExists()

        compose.tapButton("Sign out")
        compose.onNodeWithText("CHAPTER LEAD CERTIFICATION").assertExists()
    }
}
