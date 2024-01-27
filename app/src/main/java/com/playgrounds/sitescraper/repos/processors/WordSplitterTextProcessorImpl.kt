package com.playgrounds.sitescraper.repos.processors

import com.playgrounds.sitescraper.models.MatchedParagraph

interface WordSplitterTextProcessor : TextProcessor<MatchedParagraph>
class WordSplitterTextProcessorImpl : WordSplitterTextProcessor {
    override fun processText(text: String): List<MatchedParagraph> {
        return HtmlParser().extractParagraphs(text)
    }
}