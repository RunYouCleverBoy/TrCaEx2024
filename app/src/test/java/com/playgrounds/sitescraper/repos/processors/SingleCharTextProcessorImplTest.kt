package com.playgrounds.sitescraper.repos.processors

import org.junit.Assert.*

import org.junit.Test

class SingleCharTextProcessorImplTest {

    @Test
    fun processTextSimple() {
        val singleCharTextProcessorImpl = SingleCharTextProcessorImpl(1)
        assertArrayEquals(
            arrayOf("1"),
            singleCharTextProcessorImpl.processText("<p>123456789</p>").toTypedArray()
        )
    }

    @Test
    fun processIllegalIndex() {
        val singleCharTextProcessorImpl = SingleCharTextProcessorImpl(30)
        assertArrayEquals(
            emptyArray<String>(),
            singleCharTextProcessorImpl.processText("<p>123456789</p>").toTypedArray()
        )
    }
}