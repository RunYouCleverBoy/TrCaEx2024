package com.playgrounds.sitescraper.repos.processors

import com.playgrounds.sitescraper.models.MatchedParagraph
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class WordSplitterTextProcessorImplTest {

    @Test
    fun processText() {
        val filtered = WordSplitterTextProcessorImpl().processText("<p>ABCDEF GHI</p><p>JKL</p>")
        assertArrayEquals(
            arrayOf(
                MatchedParagraph("<p>", "ABCDEF GHI", "</p>"),
                MatchedParagraph("<p>", "JKL", "</p>")
            ),
            filtered.toTypedArray()
        )
    }

    @Test
    fun processTextWithJunk() {
        val filtered = WordSplitterTextProcessorImpl().processText("<junk>aaa</junk><p>JKL</p><moreJunk>bbb</moreJunk>")
        assertArrayEquals(
            arrayOf(
                MatchedParagraph("<p>", "JKL", "</p>")
            ),
            filtered.toTypedArray()
        )
    }
}