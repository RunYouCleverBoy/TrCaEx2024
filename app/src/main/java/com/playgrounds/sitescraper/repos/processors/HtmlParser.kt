package com.playgrounds.sitescraper.repos.processors

import com.playgrounds.sitescraper.models.MatchedParagraph

class HtmlParser {

    fun filterTheParagraphs(html: String, separator: String): String {
        val regex = Regex("(<p>)(.*?)(</p>)")
        val result = regex.findAll(html).map {
            it.groupValues[2]
        }.joinToString(separator)
        return result
    }

    fun extractParagraphs(html: String): List<MatchedParagraph> {
        val regex = Regex("(<p>)(.*?)(</p>)")
        val result = regex.findAll(html).map {
            MatchedParagraph(it.groupValues[1], it.groupValues[2], it.groupValues[3])
        }
        return result.toList()
    }


}
