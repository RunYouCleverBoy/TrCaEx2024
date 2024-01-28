package com.playgrounds.sitescraper.repos

import com.playgrounds.sitescraper.models.ResultsModel
import kotlinx.coroutines.flow.StateFlow

interface HtmlRepository {
    enum class LoadJobType {
        SINGLE_CHAR, PERIODIC_CHAR, WORD_COUNT
    }

    val stateFlow: StateFlow<ResultsModel>
    suspend fun clearState()
    suspend fun refresh(type: LoadJobType, url: String): Result<Any?>
}