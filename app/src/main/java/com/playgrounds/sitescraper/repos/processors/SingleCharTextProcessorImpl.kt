package com.playgrounds.sitescraper.repos.processors

class SingleCharTextProcessorImpl(private val atPosition: Int) : TextProcessor {
    override suspend fun processText(text: String): List<String> {
        return listOf(text[atPosition].toString())
    }
}