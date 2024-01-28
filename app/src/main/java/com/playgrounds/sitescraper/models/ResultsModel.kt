package com.playgrounds.sitescraper.models

data class ResultsModel(
    val singleChar: Char? = null,
    val periodicChar: List<Char> = emptyList(),
    val wordsCount: Int? = null
)