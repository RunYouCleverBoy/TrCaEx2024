package com.playgrounds.sitescraper.repos.processors

import com.playgrounds.sitescraper.models.MatchedParagraph

class WordSplitterTextProcessorImpl : TextProcessor<MatchedParagraph> {
    override fun processText(text: String): List<MatchedParagraph> {
        return HtmlParser().extractParagraphs(text)
    }
}