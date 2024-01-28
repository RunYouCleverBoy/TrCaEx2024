package com.playgrounds.sitescraper.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TextCell(title: String, fieldText: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        FieldTitle(title)
        Text(text = fieldText, modifier = Modifier.fillMaxWidth().animateContentSize())
    }
}

@Preview(showBackground = true)
@Composable
fun TextCellPreview() {
    TextCell("The Battle of Epping Forest", "The Battle of Epping forest is a song by Genesis, inspired by an article about a gang war in East London.")
}