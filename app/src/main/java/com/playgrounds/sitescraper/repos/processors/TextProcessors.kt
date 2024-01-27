package com.playgrounds.sitescraper.repos.processors

data class TextProcessors (
    val singleChar: SingleCharTextProcessorImpl,
    val periodicChar: PeriodicCharTextProcessorImpl,
    val wordSplitter: WordSplitterTextProcessorImpl
)