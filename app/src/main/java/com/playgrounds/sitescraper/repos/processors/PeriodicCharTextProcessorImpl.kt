package com.playgrounds.sitescraper.repos.processors

interface PeriodicCharTextProcessor : TextProcessor<String>
class PeriodicCharTextProcessorImpl(private val period: Int) : PeriodicCharTextProcessor {
    override fun processText(text: String): List<String> {
        val filtered = HtmlParser().filterTheParagraphs(text, "\n")
        val range = (period - 1)..filtered.lastIndex step period
        return range.map { text[it].toString() }
    }
}