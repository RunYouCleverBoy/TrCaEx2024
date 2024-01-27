package com.playgrounds.sitescraper.repos.processors

interface TextProcessor<T> {
    fun processText(text: String): List<T>
}