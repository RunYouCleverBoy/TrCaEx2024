package com.playgrounds.sitescraper.repos.processors

data class TextProcessors (
    val singleChar: SingleCharTextProcessor,
    val periodicChar: PeriodicCharTextProcessor,
    val wordSplitter: WordSplitterTextProcessor
)