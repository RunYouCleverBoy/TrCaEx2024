package com.playgrounds.sitescraper.repos.processors

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test


class HtmlParserTest {
    private val htmlParser = HtmlParser()

    @Test
    fun htmlFilterRegexFinder() {
        val testCases = listOf(
            "<p>First paragraph</p><p>Second paragraph</p>" shouldResultIn arrayOf(
                HtmlParser.ParseResult("<p>", "First paragraph", "</p>"),
                HtmlParser.ParseResult("<p>", "Second paragraph", "</p>")
            ),
            "<q>Some junk</q><p>First paragraph</p><p>Second paragraph</p><q>SomeJunk</q>" shouldResultIn arrayOf(
                HtmlParser.ParseResult("<p>", "First paragraph", "</p>"),
                HtmlParser.ParseResult("<p>", "Second paragraph", "</p>")
            ),
            "<Junk>some junk</Junk>" shouldResultIn emptyArray(),
        )
        testCases.forEach { (input, expected) ->
            val result = htmlParser.htmlFilterRegexFinder(input).toList().toTypedArray()
            assertArrayEquals(expected, result)
        }
    }

    @Test
    fun filterTheParagraphs() {
        val testCases = listOf(
            "<p>First paragraph</p><p>Second paragraph</p>" shouldResultIn "First paragraph->Second paragraph",
            "<q>Some junk</q><p>First paragraph</p><p>Second paragraph</p><q>SomeJunk</q>" shouldResultIn "First paragraph->Second paragraph",
            "<Junk>some junk</Junk>" shouldResultIn "",
        )

        testCases.forEach { (input, expected) ->
            val result = htmlParser.filterTheParagraphs(input, "->")
            assertEquals(expected, result)
        }
    }

    @Test
    fun countTheWords() {
        val testCases = listOf(
            "<p>First paragraph</p><p>Second paragraph</p>" shouldResultIn 8,
            "<m>Some junk</m><p>First paragraph</p><p>Second paragraph</p><m>SomeJunk</m>" shouldResultIn 8,
            "<Junk>some junk</Junk>" shouldResultIn 0
        )
        testCases.forEach { (input, expected) ->
            val result = htmlParser.countWords(input)
            assertEquals(input, expected, result)
        }
    }

    private data class TestCase<T>(val input: String, val expected: T)

    private infix fun <T> String.shouldResultIn(expected: T) = TestCase(this, expected)
}