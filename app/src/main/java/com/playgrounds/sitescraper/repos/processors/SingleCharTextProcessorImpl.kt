package com.playgrounds.sitescraper.repos.processors

interface SingleCharTextProcessor : TextProcessor<Char?>
class SingleCharTextProcessorImpl(private val atPosition: Int) : SingleCharTextProcessor {
    override fun processText(text: String): Char? {
        val filtered = HtmlParser().filterTheParagraphs(text, "")
        val adjustedPosition = (atPosition - 1)
            .takeIf { it in filtered.indices } ?: return null
        return filtered[adjustedPosition]
    }
}