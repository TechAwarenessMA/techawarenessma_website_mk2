package com.techawarenessma.app.ui.text

/** One run of text with the formatting the site's copy actually uses. */
data class MarkupSpan(
    val text: String,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val link: String? = null,
)

/**
 * A deliberately tiny markup for porting the website's copy: `**bold**`, `*italic*` and
 * `[label](target)`, where target is a URL, a `mailto:` href, or `app:route`.
 * Unmatched brackets are kept as literal text.
 */
fun parseMarkup(source: String): List<MarkupSpan> {
    val spans = mutableListOf<MarkupSpan>()
    val buffer = StringBuilder()
    var bold = false
    var italic = false

    fun flush() {
        if (buffer.isNotEmpty()) {
            spans += MarkupSpan(buffer.toString(), bold, italic)
            buffer.clear()
        }
    }

    var i = 0
    while (i < source.length) {
        when {
            source.startsWith("**", i) -> {
                flush()
                bold = !bold
                i += 2
            }
            source[i] == '*' -> {
                flush()
                italic = !italic
                i += 1
            }
            source[i] == '[' -> {
                val labelEnd = source.indexOf("](", i)
                val targetEnd = if (labelEnd >= 0) source.indexOf(')', labelEnd + 2) else -1
                if (targetEnd >= 0) {
                    flush()
                    spans += MarkupSpan(
                        text = source.substring(i + 1, labelEnd),
                        bold = bold,
                        italic = italic,
                        link = source.substring(labelEnd + 2, targetEnd),
                    )
                    i = targetEnd + 1
                } else {
                    buffer.append('[')
                    i += 1
                }
            }
            else -> {
                buffer.append(source[i])
                i += 1
            }
        }
    }
    flush()
    return spans
}

/** The copy with all markup removed, for accessibility labels and tests. */
fun plainText(source: String): String = parseMarkup(source).joinToString("") { it.text }
