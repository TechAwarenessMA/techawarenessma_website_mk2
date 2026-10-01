package com.techawarenessma.app.navigation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.toArgb
import androidx.core.net.toUri
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.techawarenessma.app.links.EmailDraft
import com.techawarenessma.app.ui.AppActions
import com.techawarenessma.app.ui.theme.TaaColors

/** Hosts whose own apps should handle the link (Instagram, YouTube) instead of a browser tab. */
private val NativeAppHosts = listOf("instagram.com", "youtube.com", "youtu.be")

class AndroidAppActions(
    private val context: Context,
    private val navController: NavHostController,
) : AppActions {

    override fun navigate(route: String) {
        if (Tab.forRoute(route) != null) {
            // Bottom-nav semantics: one saved back stack per tab. A route with arguments
            // (Programs#section) always opens fresh so the requested section is honored.
            val hasArguments = route.contains('?')
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = !hasArguments
            }
        } else {
            navController.navigate(route)
        }
    }

    override fun back() {
        navController.popBackStack()
    }

    override fun openUrl(url: String) {
        val uri = url.toUri()
        val host = uri.host.orEmpty()
        if (NativeAppHosts.any { host == it || host.endsWith(".$it") }) {
            startOrToast(Intent(Intent.ACTION_VIEW, uri), "No app found to open $url")
            return
        }
        val toolbar = CustomTabColorSchemeParams.Builder().setToolbarColor(TaaColors.Cream.toArgb()).build()
        try {
            CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setDefaultColorSchemeParams(toolbar)
                .build()
                .launchUrl(context, uri)
        } catch (_: ActivityNotFoundException) {
            startOrToast(Intent(Intent.ACTION_VIEW, uri), "No browser found to open $url")
        }
    }

    override fun email(draft: EmailDraft) {
        val intent = Intent(Intent.ACTION_SENDTO, draft.toMailtoUri().toUri()).apply {
            // Some mail apps read the URI, others read extras; supply both.
            putExtra(Intent.EXTRA_EMAIL, arrayOf(draft.to))
            draft.subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
            draft.body?.let { putExtra(Intent.EXTRA_TEXT, it) }
        }
        startOrToast(intent, "No email app found. Write to ${draft.to}.")
    }

    private fun startOrToast(intent: Intent, failureMessage: String) {
        try {
            context.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, failureMessage, Toast.LENGTH_LONG).show()
        }
    }
}
