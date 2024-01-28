package com.playgrounds.sitescraper.repos.processors

import org.junit.Assert.assertEquals
import org.junit.Test

class WordCounterTextProcessorImplTest {

    @Test
    fun processText() {
        val result = WordCounterTextProcessorImpl().processText("<p>ABCDEF GHI</p><p>JKL</p>")
        // 4 for the tags, 3 for the words
        assertEquals(7, result)
    }

    @Test
    fun processTextWithJunk() {
        val result =
            WordCounterTextProcessorImpl().processText("<junk>aaa</junk><p>JKL</p><moreJunk>bbb</moreJunk>")
        assertEquals(3, result)
    }

    @Test
    fun emptyTag() {
        val result = WordCounterTextProcessorImpl().processText("<p></p>")
        assertEquals(2, result)
    }
}