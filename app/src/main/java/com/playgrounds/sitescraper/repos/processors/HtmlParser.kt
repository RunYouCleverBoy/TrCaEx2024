package com.playgrounds.sitescraper.repos.processors

import com.playgrounds.sitescraper.models.MatchedParagraph

class HtmlParser {

    fun filterTheParagraphs(html: String, separator: String): String {
        val regexFinds = htmlFilterRegexFinder(html)
        val result = regexFinds.map { it.value }.joinToString(separator)
        return result
    }

    fun extractParagraphs(html: String): List<MatchedParagraph> {
        val regexFinds = htmlFilterRegexFinder(html)
        val result = regexFinds.map {
            MatchedParagraph(it.groupValues[1], it.groupValues[2], it.groupValues[3])
        }
        return result.toList()
    }

    fun htmlFilterRegexFinder(html: String): Sequence<MatchResult> {
        return Regex("(<p>)([^<>]*?)(</p>)").findAll(html)
    }


}
