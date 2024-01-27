package com.playgrounds.sitescraper.models

data class ResultsModel(
    val singleChar: String,
    val periodicChar: List<String>,
    val wordSplitter: List<MatchedParagraph>
)