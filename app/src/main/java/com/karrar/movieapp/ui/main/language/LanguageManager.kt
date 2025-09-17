package com.karrar.movieapp.ui.main.language

import android.app.Activity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class LanguageManager(private val activity: Activity) {

    fun observeLanguage(languageFlow: StateFlow<String>) {
        (activity as? androidx.appcompat.app.AppCompatActivity)?.lifecycleScope?.launch {
            languageFlow.collect { lang ->
                updateLocale(lang)
            }
        }
    }

    private fun updateLocale(language: String) {
        val current = activity.resources.configuration.locales.get(0).language
        if (current == language) {
            return
        }

        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = activity.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
        activity.recreate()
    }
}