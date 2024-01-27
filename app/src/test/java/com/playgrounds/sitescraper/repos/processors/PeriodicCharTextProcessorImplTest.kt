package com.playgrounds.sitescraper.repos.processors

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertArrayEquals
import org.junit.Test


class PeriodicCharTextProcessorImplTest {
    private val periodicCharTextProcessorImpl = PeriodicCharTextProcessorImpl(2)

    @Test
    fun processText() {
        val filtered = periodicCharTextProcessorImpl.processText("<p>123456789</p>")
        assertArrayEquals(
            arrayOf(
                "p",
                "1",
                "3",
                "5",
                "7",
                "9",
                "/",
                ">"
            ),
            filtered.toTypedArray()
        )
    }
}