package com.example.viewmodel

data class BrowserTab(
    val id: String,
    val url: String = "aura:home",
    val title: String = "New Tab",
    val progress: Int = 0,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
    val isIncognito: Boolean = false,
    val desktopMode: Boolean = false
)
