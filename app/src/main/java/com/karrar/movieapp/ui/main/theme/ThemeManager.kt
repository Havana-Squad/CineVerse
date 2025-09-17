package com.karrar.movieapp.ui.main.theme

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ThemeManager(private val activity: AppCompatActivity) {

    fun observeTheme(darkModeFlow: StateFlow<Boolean>) {
        activity.lifecycleScope.launch {
            darkModeFlow.collectLatest { darkMode ->
                val newMode = if (darkMode) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }

                if (AppCompatDelegate.getDefaultNightMode() != newMode) {
                    AppCompatDelegate.setDefaultNightMode(newMode)
                }
            }
        }
    }
}