package com.playgrounds.sitescraper.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.playgrounds.sitescraper.R
import com.playgrounds.sitescraper.ui.screens.main.models.MainState

@Composable
fun MainScreen(state: MainState) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                FieldTitle(stringResource(R.string.char_of_interest))
                Text(text = state.charOfInterest)
            }
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                FieldTitle(stringResource(R.string.chars_of_periodic_interest))
                Text(text = state.charsOfPeriodicInterest)
            }
        }
        item {
            FieldTitle(stringResource(R.string.split_words))
        }
        items(state.splitWords.size) {i ->
            Text(text = state.splitWords[i])
        }
    }
}

@Composable
private fun FieldTitle(titleText: String) {
    Text(
        text = "$titleText:",
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.Underline)
    )
}
