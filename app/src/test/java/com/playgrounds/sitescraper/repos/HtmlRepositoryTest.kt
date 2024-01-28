package com.playgrounds.sitescraper.repos

import com.playgrounds.sitescraper.models.ResultsModel
import com.playgrounds.sitescraper.repos.processors.PeriodicCharTextProcessor
import com.playgrounds.sitescraper.repos.processors.SingleCharTextProcessor
import com.playgrounds.sitescraper.repos.processors.WordCounterTextProcessor
import com.playgrounds.sitescraper.repos.providers.PageProvider
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
    private val mockSingleTextProcessor = MockSingleTextProcessor(1)

    private val mockPeriodicTextProcessor = MockPeriodicTextProcessor(2)

    private val mockWordCounterProcessor = MockWordCounterProcessor()

    private val htmlRepository = HtmlRepositoryImpl(
        provider,
        mockSingleTextProcessor,
        mockPeriodicTextProcessor,
        mockWordCounterProcessor
    )

    @Before
    fun setUp() {
        provider.text = "One|Two|Three"
    }

    @Test
    fun refresh() {
        runTest {
            val dummyUrl = "url"
            assertEquals(
                mockSingleTextProcessor.processText(provider.text),
                htmlRepository.refresh(HtmlRepository.LoadJobType.SINGLE_CHAR, dummyUrl).getOrThrow()
            )
            assertEquals(
                mockPeriodicTextProcessor.processText(provider.text),
                htmlRepository.refresh(HtmlRepository.LoadJobType.PERIODIC_CHAR, dummyUrl).getOrThrow()
            )
            assertEquals(
                mockWordCounterProcessor.processText(provider.text),
                htmlRepository.refresh(HtmlRepository.LoadJobType.WORD_COUNT, dummyUrl).getOrThrow()
            )
        }
    }

    @Test
    fun clearState() {
        runTest {
            htmlRepository.clearState()
            assertEquals(ResultsModel(), htmlRepository.stateFlow.value)
        }
    }

    private class MockPageProvider : PageProvider {
        var text = ""
        override suspend fun getPage(url: String): Result<String> {
            delay(100.milliseconds)
            return Result.success(text)
        }
    }

    private class MockSingleTextProcessor(private val index: Int) : SingleCharTextProcessor {
        override fun processText(text: String): Char? {
            return if (index in text.indices) text[index] else null
        }
    }

    private class MockPeriodicTextProcessor(private val period: Int) : PeriodicCharTextProcessor {
        override fun processText(text: String): List<Char> {
            val str = text.split("|").joinToString(" ")
            return (period-1 until str.length step period).map { str[it] }
        }
    }

    private class MockWordCounterProcessor : WordCounterTextProcessor {
        override fun processText(text: String): Int {
            return text.split("|").count()
        }
    }
}