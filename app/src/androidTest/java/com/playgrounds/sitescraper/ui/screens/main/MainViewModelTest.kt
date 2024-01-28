package com.playgrounds.sitescraper.ui.screens.main

import com.playgrounds.sitescraper.models.LoadConfiguration
import com.playgrounds.sitescraper.models.TaskConfiguration
import com.playgrounds.sitescraper.repos.HtmlRepositoryImpl
import com.playgrounds.sitescraper.repos.processors.PeriodicCharTextProcessorImpl
import com.playgrounds.sitescraper.repos.processors.SingleCharTextProcessorImpl
import com.playgrounds.sitescraper.repos.processors.WordCounterTextProcessorImpl
import com.playgrounds.sitescraper.repos.providers.PageProvider
import com.playgrounds.sitescraper.ui.screens.main.models.MainEvent
import com.playgrounds.sitescraper.ui.screens.main.models.MainState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainViewModelTest {
    private val paragraph = "<html><body><p>Body of proof</p></body></html>"
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(
            htmlRepository = HtmlRepositoryImpl(
                MockPageProvider(paragraph),
                SingleCharTextProcessorImpl(4),
                PeriodicCharTextProcessorImpl(2),
                WordCounterTextProcessorImpl()
            ),
            loadConfiguration = LoadConfiguration(
                singleTaskConfiguration = TaskConfiguration.SingleCharTask("url1", 0),
                periodicTaskConfiguration = TaskConfiguration.PeriodicCharTask("url2", 0),
                wordSplitTaskConfiguration = TaskConfiguration.WordSplitTask("url3")
            )
        )
    }

    @Test
    fun reloadTest() {
        runTest {
            val result = CompletableDeferred<MainState>()
            launch {
                viewModel.stateFlow.collect { mainState ->
                    if (mainState.allFieldsArePopulated()) {
                        result.complete(mainState)
                        this.cancel()
                    }
                }
            }
            viewModel.dispatchEvent(MainEvent.ReloadPressed)
            val state = result.await()
            assertEquals("y", state.charOfInterest)
            assertEquals("5", state.wordsCount ?: 0)
        }
    }

    private fun MainState.allFieldsArePopulated() =
        charOfInterest != null && charsOfPeriodicInterest != null && wordsCount != null

    private class MockPageProvider(val paragraph: String) : PageProvider {
        override suspend fun getPage(url: String): Result<String> {
            return Result.success("<html><body>A$paragraph</body></html>")
        }
    }
}

