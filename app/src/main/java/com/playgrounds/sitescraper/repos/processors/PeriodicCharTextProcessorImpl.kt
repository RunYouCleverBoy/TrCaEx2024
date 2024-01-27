package com.playgrounds.sitescraper.repos.processors

class PeriodicCharTextProcessorImpl(private val period: Int) : TextProcessor<String> {
    override fun processText(text: String): List<String> {
        val filtered = HtmlParser().filterTheParagraphs(text, "\n")
        val range = (period - 1)..filtered.lastIndex step period
        return range.map { text[it].toString() }
    }
}