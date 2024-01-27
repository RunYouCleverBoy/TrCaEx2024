package com.playgrounds.sitescraper.repos.processors

import com.playgrounds.sitescraper.models.MatchedParagraph
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class WordSplitterTextProcessorImplTest {

    @Test
    fun processText() {
        val filtered = WordSplitterTextProcessorImpl()
            .processText("<p>ABCDEF GHI</p><p>JKL</p>")
        assertArrayEquals(
            arrayOf(
                MatchedParagraph("<p>", "ABCDEF GHI", "</p>"),
                MatchedParagraph("<p>", "JKM", "</p>")
            ),
            filtered.toTypedArray()
        )
    }
}