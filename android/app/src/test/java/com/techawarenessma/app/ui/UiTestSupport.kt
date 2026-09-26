package com.techawarenessma.app.ui

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowLooper

typealias ActivityComposeRule<A> = AndroidComposeTestRule<ActivityScenarioRule<A>, A>

val isVerticalScroller = SemanticsMatcher.keyIsDefined(SemanticsProperties.VerticalScrollAxisRange)

private val isTab = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Tab)

/** A field whose semantic error (what TalkBack reads) is [message]. */
fun hasError(message: String) = SemanticsMatcher.expectValue(SemanticsProperties.Error, message)

/** Lets Compose, Coil's background decodes and the main looper all catch up. */
fun <A : ComponentActivity> ActivityComposeRule<A>.settle(rounds: Int = 6) {
    repeat(rounds) {
        mainClock.advanceTimeBy(250)
        waitForIdle()
        Thread.sleep(30)
        ShadowLooper.idleMainLooper()
    }
    waitForIdle()
}

fun <A : ComponentActivity> ActivityComposeRule<A>.tapTab(label: String) {
    onNode(hasText(label) and isTab).performClick()
    settle(2)
}

/** Scrolls the screen's main scroller until [text] is on screen, then returns that node. */
fun <A : ComponentActivity> ActivityComposeRule<A>.scrollTo(text: String, substring: Boolean = false): SemanticsNodeInteraction {
    val target = hasText(text, substring = substring) and hasAnyAncestor(isVerticalScroller)
    val lazyScroller = hasScrollToNodeAction() and isVerticalScroller
    if (onAllNodes(lazyScroller).fetchSemanticsNodes().isNotEmpty()) {
        // Lazy lists only compose what's near the viewport, so page until the item exists.
        onAllNodes(lazyScroller).onFirst().performScrollToNode(hasText(text, substring = substring))
    } else {
        onNode(target).performScrollTo()
    }
    settle(2)
    return onNode(target)
}

fun <A : ComponentActivity> ActivityComposeRule<A>.tapButton(text: String) {
    onNode(hasText(text) and hasClickAction()).performClick()
    settle(2)
}

/** The next activity the app asked Android to start (email app, browser…), or null. */
fun <A : ComponentActivity> ActivityComposeRule<A>.nextStartedIntent(): Intent? =
    shadowOf(activity).nextStartedActivity
