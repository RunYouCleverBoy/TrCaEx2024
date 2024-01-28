package com.playgrounds.sitescraper.ui.screens.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.playgrounds.sitescraper.models.LoadConfiguration
import com.playgrounds.sitescraper.repos.HtmlRepository
import com.playgrounds.sitescraper.ui.screens.MVIViewModel
import com.playgrounds.sitescraper.ui.screens.main.models.MainAction
import com.playgrounds.sitescraper.ui.screens.main.models.MainEvent
import com.playgrounds.sitescraper.ui.screens.main.models.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val htmlRepository: HtmlRepository,
    private val loadConfiguration: LoadConfiguration,
) : MVIViewModel<MainState, MainEvent, MainAction>(MainState()) {

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        errorHandler(throwable)
    }

    init {
        viewModelScope.launch {
            dispatchEvent(MainEvent.ReloadPressed)
            htmlRepository.stateFlow.collect { model ->
                _stateFlow.update { state ->
                    state.copy(
                        charOfInterest = model.singleChar?.toString(),
                        charsOfPeriodicInterest = model.periodicChar.joinToString(" ") {
                            when (it) {
                                '\n' -> "⏎"
                                ' ' -> "␣"
                                '\t' -> "⇥"
                                else -> it.toString()
                            }
                        },
                        wordsCount = model.wordsCount?.toString()
                    )
                }
            }
        }
    }

    override fun dispatchEvent(event: MainEvent) {
        when (event) {
            MainEvent.ReloadPressed -> onReloadPressed()
        }
    }

    private fun onReloadPressed() {
        _stateFlow.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            htmlRepository.clearState()
            val loadJobs = listOf(
                HtmlRepository.LoadJobType.SINGLE_CHAR to loadConfiguration.singleTaskConfiguration.url,
                HtmlRepository.LoadJobType.PERIODIC_CHAR to loadConfiguration.periodicTaskConfiguration.url,
                HtmlRepository.LoadJobType.WORD_COUNT to loadConfiguration.wordSplitTaskConfiguration.url
            )
            val jobs = loadJobs.map { (type, url) ->
                launch(Dispatchers.IO + errorHandler) {
                    htmlRepository.refresh(type, url)
                }
            }
            jobs.joinAll()
            _stateFlow.update { it.copy(isLoading = false) }
        }
    }

    private fun errorHandler(throwable: Throwable) {
        Log.w(TAG, "errorHandler: ", throwable)
        emit(MainAction.Error(throwable.message ?: "Unknown error"))
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}