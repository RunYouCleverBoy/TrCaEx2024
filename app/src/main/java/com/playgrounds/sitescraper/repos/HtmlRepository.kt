package com.playgrounds.sitescraper.repos

import com.playgrounds.sitescraper.repos.processors.TextProcessor
import com.playgrounds.sitescraper.repos.processors.TextProcessors
import com.playgrounds.sitescraper.repos.providers.PageProvider
import com.playgrounds.sitescraper.models.LoadConfiguration
import com.playgrounds.sitescraper.models.ResultsModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class HtmlRepository @Inject constructor(
    private val pageProvider: PageProvider,
    private val processEngines: TextProcessors,
) {

    private val _stateFlow: MutableStateFlow<ResultsModel> = MutableStateFlow(ResultsModel("", listOf(), listOf()))
    val stateFlow: StateFlow<ResultsModel> = _stateFlow

    data class LoadJob(
        val url: String,
        val textProcessor: TextProcessor,
        val onDone: (List<String>) -> Unit
    )

    fun getLoadJobs(configuration: LoadConfiguration) = listOf(
        LoadJob(
            configuration.singleTaskConfiguration.url,
            processEngines.singleChar
        ) { data -> _stateFlow.update { it.copy(singleChar = data.firstOrNull() ?: "") } },
        LoadJob(
            configuration.periodicTaskConfiguration.url,
            processEngines.periodicChar
        ) { data -> _stateFlow.update { it.copy(periodicChar = data) } },
        LoadJob(
            configuration.wordSplitTaskConfiguration.url,
            processEngines.wordSplitter
        ) { data -> _stateFlow.update { it.copy(wordSplitter = data) } }
    )

    suspend fun refresh(loadJob: LoadJob): Result<String> {
        val result = pageProvider.getPage(loadJob.url)
        if (result.isSuccess) {
            loadJob.textProcessor.processText(result.getOrThrow())
            loadJob.onDone
        }
        return result
    }
}
