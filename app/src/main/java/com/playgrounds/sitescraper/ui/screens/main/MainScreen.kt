package com.playgrounds.sitescraper.ui.screens.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.playgrounds.sitescraper.R
import com.playgrounds.sitescraper.ui.components.TextCell
import com.playgrounds.sitescraper.ui.screens.main.models.MainState

@Composable
fun MainScreen(state: MainState) {
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .padding(all = 16.dp)) {
        item {
            TextCell(
                stringResource(R.string.char_of_interest),
                state.charOfInterest ?: "",
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        item {
            TextCell(
                stringResource(R.string.chars_of_periodic_interest),
                state.charsOfPeriodicInterest ?: "",
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        item {
            TextCell(
                stringResource(R.string.word_count),
                state.wordsCount ?: "",
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

