package com.playgrounds.sitescraper.repos.processors

class SingleCharTextProcessorImpl(private val atPosition: Int) : TextProcessor<String> {
    override fun processText(text: String): List<String> {
        val filtered = HtmlParser().filterTheParagraphs(text, "")
        return listOf(filtered[atPosition].toString())
    }
}