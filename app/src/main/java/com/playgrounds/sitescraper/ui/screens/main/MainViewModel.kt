package com.playgrounds.sitescraper.ui.screens.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.playgrounds.sitescraper.ui.screens.MVIViewModel
import com.playgrounds.sitescraper.models.LoadConfiguration
import com.playgrounds.sitescraper.ui.screens.main.models.MainAction
import com.playgrounds.sitescraper.ui.screens.main.models.MainEvent
import com.playgrounds.sitescraper.ui.screens.main.models.MainState
import com.playgrounds.sitescraper.repos.HtmlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val htmlRepository: HtmlRepository,
    private val loadConfiguration: LoadConfiguration,
): MVIViewModel<MainState, MainEvent, MainAction>(MainState()) {

    init {
        viewModelScope.launch {
            dispatchEvent(MainEvent.ReloadPressed)
            htmlRepository.stateFlow.collect{ model ->
                _stateFlow.update { it.copy(
                    charOfInterest = model.singleChar,
                    charsOfPeriodicInterest = model.periodicChar.joinToString(","),
                    splitWords = model.wordSplitter.joinToString(", ")
                ) }
            }
        }
    }

    override fun dispatchEvent(event: MainEvent) {
        when(event) {
            MainEvent.ReloadPressed -> onReloadPressed()
        }
    }

    private fun onReloadPressed() {
        _stateFlow.update { it.copy(isLoading = true) }
        viewModelScope.launch(CoroutineExceptionHandler{ _, throwable ->
            errorHandler(throwable)
        }) {
            val loadJobs = htmlRepository.getLoadJobs(loadConfiguration)
            val jobs = loadJobs.map { loadJob ->
                launch {
                    htmlRepository.refresh(loadJob)
                }
            }
            jobs.joinAll()
            _stateFlow.update { it.copy(isLoading = false) }
        }
    }

    private fun errorHandler(throwable: Throwable) {
        Log.w(TAG, "errorHandler: ", throwable)
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}