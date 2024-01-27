package com.playgrounds.sitescraper.repos.processors

class SingleCharTextProcessorImpl(private val atPosition: Int) : TextProcessor<String> {
    override fun processText(text: String): List<String> {
        val filtered = HtmlParser().filterTheParagraphs(text, "")
        val adjustedPosition = (atPosition - 1)
            .takeIf { it in filtered.indices } ?: return emptyList()
        return listOf(filtered[adjustedPosition].toString())
    }
}