package com.playgrounds.sitescraper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.playgrounds.sitescraper.ui.components.TopBar
import com.playgrounds.sitescraper.ui.screens.main.MainScreen
import com.playgrounds.sitescraper.ui.screens.main.MainViewModel
import com.playgrounds.sitescraper.ui.screens.main.models.MainEvent
import com.playgrounds.sitescraper.ui.theme.SiteScraperTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel by viewModels<MainViewModel>()
            val state by viewModel.stateFlow.collectAsState()
            SiteScraperTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = { TopBar { viewModel.dispatchEvent(MainEvent.ReloadPressed) } }
                    ) { paddingValues ->
                        Box(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize()
                        ) {
                            MainScreen(state)
                            if (state.isLoading) {
                                LinearProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.TopCenter)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}


