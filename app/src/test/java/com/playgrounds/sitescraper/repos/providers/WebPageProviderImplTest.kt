package com.playgrounds.sitescraper.repos.providers

import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WebPageProviderImplTest {
    val provider = WebPageProviderImpl(HttpClient())

    @Test
    fun getPage() {
        runTest{
            val result = provider.getPage("https://www.google.com")
            if (result.isSuccess) {
                assertTrue(result.getOrNull()?.contains("<html") ?: false)
            } else {
                assertTrue(result.exceptionOrNull() != null)
            }
        }
    }

    @Test
    fun failedPage() {
        runTest{
            val result = provider.getPage("https://aaa/404")
            assertTrue(result.exceptionOrNull() is PageProvider.PageProviderException)
        }
    }
}