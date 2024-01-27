package com.playgrounds.sitescraper.ui.screens.main.models

/**
 * State of main screen
 */
data class MainState(
    val isLoading: Boolean = false,
    val charOfInterest: String = "",
    val charsOfPeriodicInterest: String = "",
    val splitWords: List<String> = emptyList(),
)

