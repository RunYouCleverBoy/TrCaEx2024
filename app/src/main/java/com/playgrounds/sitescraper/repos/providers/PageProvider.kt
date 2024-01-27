package com.playgrounds.sitescraper.repos.providers

interface PageProvider {
    // TODO: In real life we would want to chunk the response and stream it, if the input is large
    suspend fun getPage(url: String): Result<String>
}