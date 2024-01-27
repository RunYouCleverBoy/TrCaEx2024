package com.playgrounds.sitescraper.repos.processors

interface TextProcessor {
    suspend fun processText(text: String): List<String>
}