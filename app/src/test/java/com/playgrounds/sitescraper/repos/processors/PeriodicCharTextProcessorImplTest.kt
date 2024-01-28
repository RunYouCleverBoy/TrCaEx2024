package com.playgrounds.sitescraper.repos.processors

import org.junit.Assert.assertEquals
import org.junit.Test


class PeriodicCharTextProcessorImplTest {
    private val periodicCharTextProcessorImpl = PeriodicCharTextProcessorImpl(2)

    @Test
    fun processText() {
        assertEquals(
            "246",
            periodicCharTextProcessorImpl.processText("<p>123456</p>")
                .joinToString("") { it.toString() }
        )
    }

    @Test
    fun processTextWithSpaces() {
        assertEquals(
            "135 ",
            periodicCharTextProcessorImpl.processText("<p> 123456 </p>")
                .joinToString("") { it.toString() }
        )
    }
}