package com.playgrounds.sitescraper.repos

import com.playgrounds.sitescraper.models.LoadConfiguration
import com.playgrounds.sitescraper.models.ResultsModel
import com.playgrounds.sitescraper.repos.processors.TextProcessor
import com.playgrounds.sitescraper.repos.processors.TextProcessors
import com.playgrounds.sitescraper.repos.providers.PageProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface HtmlRepository {
    data class LoadJob<T>(
        val url: String,
        val textProcessor: TextProcessor<T>,
        val onDone: (List<T>) -> Unit
    )

    val stateFlow: StateFlow<ResultsModel>
    fun getLoadJobs(configuration: LoadConfiguration): List<LoadJob<out Any>>
    suspend fun <T> refresh(loadJob: LoadJob<T>): Result<String>
}

class HtmlRepositoryImpl @Inject constructor(
    private val pageProvider: PageProvider,
    private val processEngines: TextProcessors,
): HtmlRepository {

    private val _stateFlow: MutableStateFlow<ResultsModel> =
        MutableStateFlow(ResultsModel("", listOf(), listOf()))
    override val stateFlow: StateFlow<ResultsModel> = _stateFlow

    override fun getLoadJobs(configuration: LoadConfiguration) = listOf(
        HtmlRepository.LoadJob(
            configuration.singleTaskConfiguration.url,
            processEngines.singleChar
        ) { data -> _stateFlow.update { it.copy(singleChar = data.firstOrNull() ?: "") } },
        HtmlRepository.LoadJob(
            configuration.periodicTaskConfiguration.url,
            processEngines.periodicChar
        ) { data -> _stateFlow.update { it.copy(periodicChar = data) } },
        HtmlRepository.LoadJob(
            configuration.wordSplitTaskConfiguration.url,
            processEngines.wordSplitter
        ) { data -> _stateFlow.update { it.copy(wordSplitter = data) } }
    )

    override suspend fun <T> refresh(loadJob: HtmlRepository.LoadJob<T>): Result<String> {
        val result = pageProvider.getPage(loadJob.url)
        if (result.isSuccess) {
            val data = loadJob.textProcessor.processText(result.getOrThrow())
            loadJob.onDone(data)
        }
        return result
    }
}
