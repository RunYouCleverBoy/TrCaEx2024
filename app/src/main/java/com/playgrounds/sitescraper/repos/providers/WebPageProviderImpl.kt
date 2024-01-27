package com.playgrounds.sitescraper.repos.providers

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import javax.inject.Inject

class WebPageProviderImpl @Inject constructor(
    private val client: HttpClient
) : PageProvider {
    override suspend fun getPage(url: String): Result<String> {
        val response: HttpResponse = client.get(url)
        return if (response.status.isSuccess()) {
            Result.success(response.bodyAsText())
        } else {
            Result.failure(Exception("Error fetching HTML"))
        }
    }
}