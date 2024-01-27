package com.playgrounds.sitescraper.repos.processors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HtmlParser {
    data class MatchedParagraph(val tag: String, val text: String, val suffix: String)
    suspend fun filterTheParagraphs(html: String, separator: String): String {
        return withContext(Dispatchers.IO) {
            filterTheParagraphsSync(html, separator)
        }
    }

    fun extractParagraphs(html: String): List<MatchedParagraph> {
        val regex = Regex("(<p>)(.*?)(</p>)")
        val result = regex.findAll(html).map {
            MatchedParagraph(it.groupValues[1], it.groupValues[2], it.groupValues[3])
        }
        return result.toList()
    }


    private fun filterTheParagraphsSync(
        html: String,
        separator: String
    ): String {
        val regex = Regex("(<p>)(.*?)(</p>)")
        val result = regex.findAll(html).map {
            it.groupValues[2]
        }.joinToString(separator)
        return result
    }
}
