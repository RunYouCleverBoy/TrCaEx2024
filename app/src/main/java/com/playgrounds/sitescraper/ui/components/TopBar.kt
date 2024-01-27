package com.playgrounds.sitescraper.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.playgrounds.sitescraper.R

@Composable
fun TopBar(onRefresh: () -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = stringResource(R.string.top_bar_title))
        IconButton(onClick = onRefresh) {
            Icon(painter = painterResource(id = R.drawable.baseline_refresh_24), contentDescription = stringResource(
                id = R.string.refresh_button_description)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar()
}
