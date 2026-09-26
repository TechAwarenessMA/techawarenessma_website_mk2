package com.techawarenessma.app.ui

import androidx.compose.runtime.staticCompositionLocalOf
import com.techawarenessma.app.links.EmailDraft
import com.techawarenessma.app.links.Links

/**
 * What a tap on a link can do. Screens reach it through [LocalAppActions] so content can
 * carry website-style hrefs (`mailto:`, `https:`, `app:route`) without threading callbacks.
 */
interface AppActions {
    /** Opens an app screen. Tab roots switch tabs; every other screen is pushed. */
    fun navigate(route: String)

    fun back()

    fun openUrl(url: String)

    /** Opens the user's mail app with the draft filled in. The app itself never sends mail. */
    fun email(draft: EmailDraft)

    fun email(to: String, subject: String? = null, body: String? = null) =
        email(EmailDraft(to, subject, body))

    /** Dispatches a site-style href. */
    fun open(target: String) {
        when {
            target.startsWith(Links.APP_SCHEME) -> navigate(target.removePrefix(Links.APP_SCHEME))
            target.startsWith("mailto:", ignoreCase = true) -> EmailDraft.fromMailto(target)?.let(::email)
            else -> openUrl(target)
        }
    }
}

val LocalAppActions = staticCompositionLocalOf<AppActions> {
    error("LocalAppActions is not provided; wrap content in TaaApp or a preview helper.")
}

/** Used by previews, where taps have nowhere to go. */
object NoOpAppActions : AppActions {
    override fun navigate(route: String) = Unit
    override fun back() = Unit
    override fun openUrl(url: String) = Unit
    override fun email(draft: EmailDraft) = Unit
}
