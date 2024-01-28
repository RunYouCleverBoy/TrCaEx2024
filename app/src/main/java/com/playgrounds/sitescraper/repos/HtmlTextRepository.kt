package com.playgrounds.sitescraper.repos

import android.util.Log
import com.playgrounds.sitescraper.models.ResultsModel
import com.playgrounds.sitescraper.repos.processors.PeriodicCharTextProcessor
import com.playgrounds.sitescraper.repos.processors.SingleCharTextProcessor
import com.playgrounds.sitescraper.repos.processors.TextProcessor
import com.playgrounds.sitescraper.repos.processors.WordCounterTextProcessor
import com.playgrounds.sitescraper.repos.providers.PageProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class HtmlRepositoryImpl @Inject constructor(
    private val pageProvider: PageProvider,
    private val singleCharProcessor: SingleCharTextProcessor,
    private val periodicCharProcessor: PeriodicCharTextProcessor,
    private val wordCounterProcessor: WordCounterTextProcessor
) : HtmlRepository {

    private val _stateFlow: MutableStateFlow<ResultsModel> = MutableStateFlow(ResultsModel())
    override val stateFlow: StateFlow<ResultsModel> = _stateFlow

    override suspend fun clearState() {
        _stateFlow.value = ResultsModel()
    }

    override suspend fun refresh(type: HtmlRepository.LoadJobType, url: String): Result<Any?> {
        val result = pageProvider.getPage(url)
        return if (result.isSuccess) {
            val text = result.getOrNull() ?: ""
            val processor = getProcessor(type)
            val processed = processor.processText(text)
            Log.v(TAG, "refresh: $type")
            dispatchResult(type, processed)
            Result.success(processed)
        } else {
            val throwable = result.exceptionOrNull() ?: Exception("Unknown error")
            Log.e(TAG, "refresh: $type", throwable)
            Result.failure(throwable)
        }
    }

    private fun getProcessor(type: HtmlRepository.LoadJobType): TextProcessor<out Any?> =
        when (type) {
            HtmlRepository.LoadJobType.SINGLE_CHAR -> singleCharProcessor
            HtmlRepository.LoadJobType.PERIODIC_CHAR -> periodicCharProcessor
            HtmlRepository.LoadJobType.WORD_COUNT -> wordCounterProcessor
        }

    @Suppress("UNCHECKED_CAST")
    private fun dispatchResult(type: HtmlRepository.LoadJobType, data: Any?) {
        when (type) {
            HtmlRepository.LoadJobType.SINGLE_CHAR -> updateSingleChar(data as Char?)
            HtmlRepository.LoadJobType.PERIODIC_CHAR -> updatePeriodicChar(data as List<Char>)
            HtmlRepository.LoadJobType.WORD_COUNT -> updateWordCount(data as Int)
        }
    }

    private fun updateSingleChar(data: Char?) {
        _stateFlow.update { it.copy(singleChar = data) }
    }

    private fun updatePeriodicChar(data: List<Char>) {
        _stateFlow.update { it.copy(periodicChar = data) }
    }

    private fun updateWordCount(data: Int) {
        _stateFlow.update { it.copy(wordsCount = data) }
    }

    companion object {
        private const val TAG = "HtmlRepository"
    }
}
