package com.playgrounds.sitescraper.repos.processors

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test


class HtmlParserTest {
    private val htmlParser = HtmlParser()

    @Test
    fun filterTheParagraphs() {
        val testCasesWithTags: List<Pair<String, String>> = listOf(
            "<p>First paragraph</p><p>Second paragraph</p>" to "<p>First paragraph->Second paragraph</p>",
            "<q>Some junk</q><p>First paragraph</p><p>Second paragraph</p><q>SomeJunk</q>" to "<p>First paragraph</p>-><p>Second paragraph</p>",
            "<Junk>some junk</Junk>" to "",
        )

        runTest {
            testCasesWithTags.forEach { (input, expected) ->
                val result = htmlParser.filterTheParagraphs(input, "->")
                assertEquals(expected, result)
            }
        }
    }

    @Test
    fun extractTheParagraphs() {
        runTest {
            val html = "<p>First paragraph</p><p>Second paragraph</p>"
            var result = htmlParser.extractParagraphs(html).toTypedArray()
            assertArrayEquals(arrayOf(
                HtmlParser.MatchedParagraph("<p>", "First paragraph", "</p>"),
                HtmlParser.MatchedParagraph("<p>", "Second paragraph", "</p>")
            ), result)

            result = htmlParser.extractParagraphs("<q>Some junk</q><p>First paragraph</p><p>Second paragraph</p><q>SomeJunk</q>").toTypedArray()
            assertArrayEquals(arrayOf(
                HtmlParser.MatchedParagraph("<p>", "First paragraph", "</p>"),
                HtmlParser.MatchedParagraph("<p>", "Second paragraph", "</p>")
            ), result)
        }
    }
}