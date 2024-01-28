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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

class MainViewModelTest {
    private val paragraph = "<html><body><p>Body of proof</p></body></html>"
    private lateinit var viewModel: MainViewModel

    private lateinit var mockPageProvider: MockPageProvider

    @Before
    fun setUp() {
        mockPageProvider = MockPageProvider(paragraph)
        viewModel = MainViewModel(
            htmlRepository = HtmlRepositoryImpl(
                mockPageProvider,
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

    @Test
    fun parallelismCheck() {
        val duration = 1.seconds
        mockPageProvider.delayTime = duration
        runTest {
            val times = mutableListOf<Duration>()
            val refTime = System.nanoTime()
            val job = launch {
                viewModel.stateFlow.collect { mainState ->
                    val elapsed = (System.nanoTime() - refTime).nanoseconds
                    if (elapsed >= duration) {
                        times.add(elapsed)
                    }

                    if (mainState.allFieldsArePopulated()) {
                        cancel()
                    }
                }
            }

            viewModel.dispatchEvent(MainEvent.ReloadPressed)
            job.join()
            val deltas = times.zipWithNext { a, b -> b - a }
            deltas.forEachIndexed { i, delta ->
                assertTrue("Sample $i vs ${i+1}: $delta", delta < duration)
            }
            assertTrue(times.last() < duration * 1.5)
        }
    }

    private fun MainState.allFieldsArePopulated() =
        charOfInterest != null && charsOfPeriodicInterest != null && wordsCount != null

    private class MockPageProvider(
        val paragraph: String,
        var delayTime: Duration = 500.milliseconds
    ) : PageProvider {
        override suspend fun getPage(url: String): Result<String> {
            delay(delayTime)
            return Result.success("<html><body>A$paragraph</body></html>")
        }
    }
}

