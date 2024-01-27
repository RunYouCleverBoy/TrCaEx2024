package com.playgrounds.sitescraper.repos.processors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordSplitterTextProcessorImpl : TextProcessor {
    override suspend fun processText(text: String): List<String> {
        return withContext(Dispatchers.IO) {
            HtmlParser().filterTheParagraphs(text, " ").split(Regex("\\s+"))
        }
    }
}