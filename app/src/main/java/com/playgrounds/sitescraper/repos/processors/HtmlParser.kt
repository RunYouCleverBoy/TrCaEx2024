package com.playgrounds.sitescraper.repos.processors

class HtmlParser {
    data class ParseResult(val prefix: String, val body: String, val suffix: String)

    fun filterTheParagraphs(html: String, separator: String): String {
        val regexFinds = htmlFilterRegexFinder(html)
        return regexFinds.map { it.body }.joinToString(separator)
    }

    fun countWords(html: String): Int {
        val regexFinds = htmlFilterRegexFinder(html)
        return regexFinds.sumOf { parseResult ->
            parseResult.body.split(Regex("\\s+")).count { it.isNotBlank() } + count(
                parseResult.prefix,
                parseResult.suffix
            )
        }
    }

    fun htmlFilterRegexFinder(html: String): Sequence<ParseResult> {
        return Regex("(<p>)(.*?)(</p>)").findAll(html).map {
            ParseResult(it.groupValues[1], it.groupValues[2], it.groupValues[3])
        }
    }

    private fun count(vararg strings: String): Int = strings.count { it.isNotBlank() }
}
