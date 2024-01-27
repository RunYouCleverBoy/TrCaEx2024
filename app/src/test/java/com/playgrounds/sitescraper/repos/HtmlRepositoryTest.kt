package com.playgrounds.sitescraper.repos

import com.playgrounds.sitescraper.models.LoadConfiguration
import com.playgrounds.sitescraper.models.MatchedParagraph
import com.playgrounds.sitescraper.models.TaskConfiguration
import com.playgrounds.sitescraper.repos.processors.PeriodicCharTextProcessor
import com.playgrounds.sitescraper.repos.processors.SingleCharTextProcessor
import com.playgrounds.sitescraper.repos.processors.TextProcessors
import com.playgrounds.sitescraper.repos.processors.WordSplitterTextProcessor
import com.playgrounds.sitescraper.repos.providers.PageProvider
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.time.Duration.Companion.milliseconds

@RunWith(RobolectricTestRunner::class)
class HtmlRepositoryTest {
    private val provider: MockPageProvider = MockPageProvider()
    private val htmlRepository = HtmlRepositoryImpl(
        provider, TextProcessors(
            MockSingleTextProcessor(1),
            MockPeriodicTextProcessor(2),
            MockWordSplitterProcessor(3)
        )
    )

    @Before
    fun setUp() {
        provider.text = "One|Two|Three"
    }

    @Test
    fun getLoadJobs() {
        val configuration = LoadConfiguration(
            TaskConfiguration.SingleCharTask("url1", 0),
            TaskConfiguration.PeriodicCharTask("url2", 0),
            TaskConfiguration.WordSplitTask("url3")
        )
        assertEquals(htmlRepository.getLoadJobs(configuration).size, 3)
        htmlRepository.getLoadJobs(configuration)[0].let {
            assertEquals(it.url, "url1")
            assertEquals(1, (it.textProcessor as? MockSingleTextProcessor)?.id)
        }
        htmlRepository.getLoadJobs(configuration)[1].let {
            assertEquals(it.url, "url2")
            assertEquals(2, (it.textProcessor as? MockPeriodicTextProcessor)?.id)
        }
        htmlRepository.getLoadJobs(configuration)[2].let {
            assertEquals(it.url, "url3")
            assertEquals(3, (it.textProcessor as? MockWordSplitterProcessor)?.id)
        }
    }

    @Test
    fun refresh() {
        runTest {
            val response = CompletableDeferred<List<String>>()
            htmlRepository.refresh(HtmlRepository.LoadJob("url", MockSingleTextProcessor(1)) {
                response.complete(it)
            })
            val result = response.await()
            assertArrayEquals(provider.text.split("|").toTypedArray(), result.toTypedArray())
        }
    }

    private class MockPageProvider : PageProvider {
        var text = "One|Two|Three"
        override suspend fun getPage(url: String): Result<String> {
            delay(100.milliseconds)
            return Result.success(text)
        }
    }

    private class MockSingleTextProcessor(val id: Int) : SingleCharTextProcessor {
        override fun processText(text: String): List<String> {
            return text.split("|")
        }
    }
    private class MockPeriodicTextProcessor(val id: Int) : PeriodicCharTextProcessor {
        override fun processText(text: String): List<String> {
            return text.split("|")
        }
    }
    private class MockWordSplitterProcessor(val id: Int) : WordSplitterTextProcessor {
        override fun processText(text: String): List<MatchedParagraph> {
            return text.split("|").mapIndexed { index, s -> MatchedParagraph("A", s, "-A") }
        }
    }
}