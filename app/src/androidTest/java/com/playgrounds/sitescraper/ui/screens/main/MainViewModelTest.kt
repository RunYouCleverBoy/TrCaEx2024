package com.playgrounds.sitescraper.ui.screens.main

import com.playgrounds.sitescraper.models.LoadConfiguration
import com.playgrounds.sitescraper.models.MatchedParagraph
import com.playgrounds.sitescraper.models.TaskConfiguration
import com.playgrounds.sitescraper.repos.HtmlRepositoryImpl
import com.playgrounds.sitescraper.repos.processors.PeriodicCharTextProcessorImpl
import com.playgrounds.sitescraper.repos.processors.SingleCharTextProcessorImpl
import com.playgrounds.sitescraper.repos.processors.TextProcessors
import com.playgrounds.sitescraper.repos.processors.WordSplitterTextProcessorImpl
import com.playgrounds.sitescraper.repos.providers.PageProvider
import com.playgrounds.sitescraper.ui.screens.main.models.MainEvent
import com.playgrounds.sitescraper.ui.screens.main.models.MainState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainViewModelTest {
    private val matchedParagraph = MatchedParagraph("<p>", "Body", "</p>")
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(
            htmlRepository = HtmlRepositoryImpl(
                MockPageProvider(matchedParagraph), TextProcessors(
                    SingleCharTextProcessorImpl(4),
                    PeriodicCharTextProcessorImpl(2),
                    WordSplitterTextProcessorImpl()
                )
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
                viewModel.stateFlow.collect {
                    if (it.charOfInterest.isNotBlank()) {
                        result.complete(it)
                        this.cancel()
                    }
                }
            }
            viewModel.dispatchEvent(MainEvent.ReloadPressed)
            val state = result.await()
            assertEquals("B", state.charOfInterest)
            assertArrayEquals(
                arrayOf("${ matchedParagraph.tag } ${ matchedParagraph.text } ${ matchedParagraph.suffix }"), state.splitWords.toTypedArray()
            )
        }
    }


    private class MockPageProvider(val paragraph: MatchedParagraph) : PageProvider {
        override suspend fun getPage(url: String): Result<String> {
            return Result.success("<html><body>A${paragraph.toHtml}</body></html>")
        }
        private val MatchedParagraph.toHtml get() = "$tag$text$suffix"
    }
}

