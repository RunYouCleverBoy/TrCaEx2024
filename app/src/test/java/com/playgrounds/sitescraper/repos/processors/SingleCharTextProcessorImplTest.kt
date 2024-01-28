package com.playgrounds.sitescraper.repos.processors

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SingleCharTextProcessorImplTest {

    @Test
    fun processTextSimple() {
        val singleCharTextProcessorImpl = SingleCharTextProcessorImpl(4)
        assertEquals(
            '4',
            singleCharTextProcessorImpl.processText("<p>123456789</p>")
        )
    }

    @Test
    fun processIllegalIndex() {
        val singleCharTextProcessorImpl = SingleCharTextProcessorImpl(30)
        assertNull(singleCharTextProcessorImpl.processText("<p>123456789</p>"))
    }
}