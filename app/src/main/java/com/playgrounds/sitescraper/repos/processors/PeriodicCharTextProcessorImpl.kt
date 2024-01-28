package com.playgrounds.sitescraper.repos.processors

class PeriodicCharTextProcessorImpl(private val period: Int) : PeriodicCharTextProcessor {
    override fun processText(text: String): List<Char> {
        val filtered = HtmlParser().filterTheParagraphs(text, "\n")
        val range = (period - 1)..filtered.lastIndex step period
        return range.map { filtered[it] }
    }
}