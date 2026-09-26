package com.techawarenessma.app.ui.text

import org.junit.Assert.assertEquals
import org.junit.Test

class MarkupTest {

    @Test
    fun plainTextIsOneSpan() {
        assertEquals(listOf(MarkupSpan("Just text.")), parseMarkup("Just text."))
    }

    @Test
    fun boldItalicAndLinks() {
        assertEquals(
            listOf(
                MarkupSpan("Read "),
                MarkupSpan("this", bold = true),
                MarkupSpan(", then "),
                MarkupSpan("that", italic = true),
                MarkupSpan(" in "),
                MarkupSpan("Teardown", link = "https://teardown.techawarenessma.com"),
                MarkupSpan("."),
            ),
            parseMarkup("Read **this**, then *that* in [Teardown](https://teardown.techawarenessma.com)."),
        )
    }

    @Test
    fun linkInsideBoldKeepsBold() {
        assertEquals(
            listOf(MarkupSpan("See ", bold = true), MarkupSpan("it", bold = true, link = "app:programs")),
            parseMarkup("**See [it](app:programs)**"),
        )
    }

    @Test
    fun urlsWithEncodedCharactersSurvive() {
        val url = "https://www.ifixit.com/Guide/Lenovo+ThinkPad+E14+Gen+4+Fan+%26+Heat+Sink+Replacement/200825"
        assertEquals(url, parseMarkup("[Fan & heat sink]($url)").single().link)
    }

    @Test
    fun unmatchedBracketIsLiteral() {
        assertEquals("[not a link", plainText("[not a link"))
        assertEquals("a [b] c", plainText("a [b] c"))
    }

    @Test
    fun plainTextStripsMarkup() {
        assertEquals("Hello world, click here.", plainText("**Hello** *world*, [click here](app:home)."))
    }
}
