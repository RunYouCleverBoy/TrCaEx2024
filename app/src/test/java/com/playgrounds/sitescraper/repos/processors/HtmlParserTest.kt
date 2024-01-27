package com.playgrounds.sitescraper.repos.processors

import com.playgrounds.sitescraper.models.MatchedParagraph
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test


class HtmlParserTest {
    private val htmlParser = HtmlParser()

    @Test
    fun htmlFilterRegexFinder() {
        val testCasesWithTags: List<Pair<String, List<String>>> = listOf(
            "<p>First paragraph</p><p>Second paragraph</p>" to listOf(
                "<p>First paragraph</p>",
                "<p>Second paragraph</p>"
            ),
            "<q>Some junk</q><p>First paragraph</p><p>Second paragraph</p><q>SomeJunk</q>" to listOf(
                "<p>First paragraph</p>",
                "<p>Second paragraph</p>"
            ),
            "<Junk>some junk</Junk>" to listOf(),
        )
        testCasesWithTags.forEach { (input, expected) ->
            val result = htmlParser.htmlFilterRegexFinder(input).map { it.value }.toList()
            assertEquals(expected, result)
        }
    }

    @Test
    fun filterTheParagraphs() {
        val testCasesWithTags: List<Pair<String, String>> = listOf(
            "<p>First paragraph</p><p>Second paragraph</p>" to "<p>First paragraph</p>-><p>Second paragraph</p>",
            "<q>Some junk</q><p>First paragraph</p><p>Second paragraph</p><q>SomeJunk</q>" to "<p>First paragraph</p>-><p>Second paragraph</p>",
            "<Junk>some junk</Junk>" to "",
        )

        testCasesWithTags.forEach { (input, expected) ->
            val result = htmlParser.filterTheParagraphs(input, "->")
            assertEquals(expected, result)
        }
    }

    @Test
    fun extractTheParagraphs() {
        val html = "<p>First paragraph</p><p>Second paragraph</p>"
        var result = htmlParser.extractParagraphs(html).toTypedArray()
        assertArrayEquals(
            arrayOf(
                MatchedParagraph("<p>", "First paragraph", "</p>"),
                MatchedParagraph("<p>", "Second paragraph", "</p>")
            ), result
        )

        result =
            htmlParser.extractParagraphs("<q>Some junk</q><p>First paragraph</p><p>Second paragraph</p><q>SomeJunk</q>")
                .toTypedArray()
        assertArrayEquals(
            arrayOf(
                MatchedParagraph("<p>", "First paragraph", "</p>"),
                MatchedParagraph("<p>", "Second paragraph", "</p>")
            ), result
        )
    }
}