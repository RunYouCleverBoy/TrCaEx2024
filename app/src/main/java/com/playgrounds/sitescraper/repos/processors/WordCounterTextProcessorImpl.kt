package com.playgrounds.sitescraper.repos.processors

class WordCounterTextProcessorImpl : WordCounterTextProcessor {
    override fun processText(text: String): Int {
        return HtmlParser().countWords(text)
    }
}