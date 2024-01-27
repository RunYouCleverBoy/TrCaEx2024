package com.playgrounds.sitescraper.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.playgrounds.sitescraper.R

@Composable
fun TopBar() {
    Text(text = stringResource(R.string.top_bar_title))
}
