package com.playgrounds.sitescraper.repos.processors

import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Test

class WordSplitterTextProcessorImplTest {

    @Test
    fun processText() {
        runTest {
            val filtered: List<String> = WordSplitterTextProcessorImpl().processText("<p>Hey You</p>")
            assertArrayEquals(
                arrayOf(
                    "123456789"
                ),
                filtered.toTypedArray()
            )
        }
    }
}