package com.playgrounds.sitescraper.repos.processors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PeriodicCharTextProcessorImpl(private val period: Int) : TextProcessor {
    override suspend fun processText(text: String): List<String> {
        val filtered = HtmlParser().filterTheParagraphs(text, "\n")
        val processed = withContext(Dispatchers.IO) {
            val range = (period-1)..filtered.lastIndex step period
            range.map { text[it].toString() }
        }
        return processed
    }
}