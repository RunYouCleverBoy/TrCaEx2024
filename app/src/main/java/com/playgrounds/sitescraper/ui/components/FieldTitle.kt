package com.playgrounds.sitescraper.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FieldTitle(titleText: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = "$titleText:",
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.titleLarge.copy(textDecoration = TextDecoration.Underline)
    )
}

@Preview(showBackground = true)
@Composable
fun FieldTitlePreview() {
    FieldTitle("This is a field title")
}
