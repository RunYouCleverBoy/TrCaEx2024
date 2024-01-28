package com.playgrounds.sitescraper.repos.processors

interface WordCounterTextProcessor : TextProcessor<Int>
class WordCounterTextProcessorImpl : WordCounterTextProcessor {
    override fun processText(text: String): Int {
        return HtmlParser().countWords(text)
    }
}