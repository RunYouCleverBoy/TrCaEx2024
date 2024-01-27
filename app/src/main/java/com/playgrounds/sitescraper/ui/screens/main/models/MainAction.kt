package com.playgrounds.sitescraper.ui.screens.main.models

/**
 * Action for main screen
 */
sealed class MainAction {
    data class Error(val message: String) : MainAction()
}
