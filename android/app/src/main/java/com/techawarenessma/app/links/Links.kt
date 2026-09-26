package com.techawarenessma.app.links

import java.net.URLDecoder
import java.net.URLEncoder

/** Every address and outside URL the website links to, in one place. */
object Links {
    const val CONTACT_EMAIL = "contact@techawarenessma.com"
    const val RONIT_EMAIL = "ronit@techawarenessma.com"
    const val TANAY_EMAIL = "tanay@techawarenessma.com"
    const val MAIL_ADDRESS = "609 Main St, Shrewsbury, MA 01545"

    const val INSTAGRAM = "https://www.instagram.com/techawarenessma"
    const val INSTAGRAM_HANDLE = "@techawarenessma"
    const val TEARDOWN = "https://teardown.techawarenessma.com"
    const val SUSU = "https://susu.techawarenessma.com"
    const val IFIXIT_MANIFESTO = "https://www.ifixit.com/Manifesto"
    const val IFIXIT_GUIDES = "https://www.ifixit.com/Guide"

    /**
     * The site's GoFundMe slot is still a placeholder ("Campaign link pending").
     * Set this once the campaign is live and the Donate screens switch to it.
     */
    val DONATION_CAMPAIGN_URL: String? = null

    /** Prefix for links that point at a screen inside the app instead of the web. */
    const val APP_SCHEME = "app:"
}

/** An email the user will finish and send from their own mail app. Nothing is sent by us. */
data class EmailDraft(
    val to: String,
    val subject: String? = null,
    val body: String? = null,
) {
    /** RFC 6068 mailto URI. Spaces become %20 (not +) so every mail client reads them. */
    fun toMailtoUri(): String {
        val params = buildList {
            subject?.let { add("subject=" + encode(it)) }
            body?.let { add("body=" + encode(it)) }
        }
        return "mailto:$to" + if (params.isEmpty()) "" else params.joinToString("&", prefix = "?")
    }

    companion object {
        /** Parses the `mailto:` hrefs used across the site, e.g. `mailto:a@b.com?subject=Hi%20there`. */
        fun fromMailto(uri: String): EmailDraft? {
            if (!uri.startsWith("mailto:", ignoreCase = true)) return null
            val rest = uri.substring("mailto:".length)
            val to = rest.substringBefore('?')
            if (to.isBlank()) return null
            val query = rest.substringAfter('?', missingDelimiterValue = "")
            val params = query.split('&')
                .filter { it.contains('=') }
                .associate { it.substringBefore('=').lowercase() to decode(it.substringAfter('=')) }
            return EmailDraft(to = to, subject = params["subject"], body = params["body"])
        }

        // The String-charset overloads: the Charset ones need API 33.
        private fun encode(value: String): String =
            URLEncoder.encode(value, "UTF-8").replace("+", "%20")

        private fun decode(value: String): String =
            URLDecoder.decode(value.replace("+", "%2B"), "UTF-8")
    }
}

/** Newsletter and form fields: permissive, but rejects obvious typos before the mail app opens. */
fun isPlausibleEmail(value: String): Boolean =
    Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$").matches(value.trim())

/** The website's newsletter form: a pre-filled email to the contact inbox. */
fun newsletterDraft(email: String) = EmailDraft(
    to = Links.CONTACT_EMAIL,
    subject = "Newsletter signup",
    body = "Please add me to the updates list: ${email.trim()}",
)

/** The Start a Chapter request form, formatted exactly like the website's. */
fun startChapterDraft(town: String, name: String, email: String, why: String) = EmailDraft(
    to = Links.CONTACT_EMAIL,
    subject = "Start a chapter: ${town.trim()}",
    body = "Town: ${town.trim()}\nName: ${name.trim()}\nEmail: ${email.trim()}\nWhy this town: ${why.trim()}",
)
