package com.playgrounds.sitescraper.ui.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun InfoRow(title: String, fieldValue: String) {
    val verticalScroll = rememberScrollState()

    Row(
        modifier = Modifier
            .scrollable(verticalScroll, orientation = Orientation.Vertical)
            .fillMaxWidth()
            .heightIn(max = 100.dp)
    ) {
        Text(modifier = Modifier.width(100.dp), text = title, textAlign = TextAlign.End)
        Text(text = fieldValue)
    }
}