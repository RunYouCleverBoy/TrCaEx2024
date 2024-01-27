package com.playgrounds.sitescraper.models

sealed class TaskConfiguration {
    data class SingleCharTask(val url: String, val index: Int) : TaskConfiguration()
    data class PeriodicCharTask(val url: String, val index: Int) : TaskConfiguration()
    data class WordSplitTask(val url: String) : TaskConfiguration()
}

data class LoadConfiguration(
    val singleTaskConfiguration: TaskConfiguration.SingleCharTask,
    val periodicTaskConfiguration: TaskConfiguration.PeriodicCharTask,
    val wordSplitTaskConfiguration: TaskConfiguration.WordSplitTask
)