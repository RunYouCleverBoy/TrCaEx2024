package com.playgrounds.sitescraper.repos.providers

import android.content.res.AssetManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AssetPageProviderImpl @Inject constructor(private val assetsManager: AssetManager):
    PageProvider {
    override suspend fun getPage(url: String): Result<String> {
        val data = withContext(Dispatchers.IO) {
            assetsManager.open("t.html").bufferedReader().use {
                it.readText()
            }
        }
        return Result.success(data)
    }
}