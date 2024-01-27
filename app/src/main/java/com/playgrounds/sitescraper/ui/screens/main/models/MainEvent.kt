package com.playgrounds.sitescraper.ui.screens.main.models

/**
 * Events from main screen
 */
sealed class MainEvent {
    data object ReloadPressed: MainEvent()
}