package com.playgrounds.sitescraper.repos.providers

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.isSuccess
import javax.inject.Inject

class WebPageProviderImpl @Inject constructor(
    private val client: HttpClient
) : PageProvider {
    override suspend fun getPage(url: String): Result<String> {
        val response: HttpResponse = try {
            client.get(url)
        } catch (e: Exception) {
            return Result.failure(PageProvider.PageProviderException(e))
        }
        return if (response.status.isSuccess()) {
            Result.success(response.bodyAsText())
        } else {
            Result.failure(response.status.description.let {
                PageProvider.PageProviderException(it)
            })
        }
    }
}