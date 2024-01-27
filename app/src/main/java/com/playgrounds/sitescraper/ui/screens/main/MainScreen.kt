package com.playgrounds.sitescraper.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.playgrounds.sitescraper.R
import com.playgrounds.sitescraper.ui.components.InfoRow
import com.playgrounds.sitescraper.ui.screens.main.models.MainState

@Composable
fun MainScreen(state: MainState) {
    Column(modifier = Modifier.fillMaxWidth()) {
        InfoRow(stringResource(R.string.char_of_interest), state.charOfInterest)
        InfoRow(stringResource(R.string.chars_of_periodic_interest), state.charsOfPeriodicInterest)
        InfoRow(stringResource(R.string.split_words), state.splitWords)
    }
}
