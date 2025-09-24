package com.karrar.movieapp.ui.main

import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.ActivityMainBinding
import com.karrar.movieapp.ui.main.language.LanguageManager
import com.karrar.movieapp.ui.main.navigation.NavigationManager
import com.karrar.movieapp.ui.main.theme.ThemeManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private lateinit var navigationManager: NavigationManager
    private lateinit var themeManager: ThemeManager
    private lateinit var languageManager: LanguageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setupUI()
        initializeManagers()
        observeViewModel()
    }

    private fun setupUI() {
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
        setTheme(R.style.Theme_MovieApp)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initializeManagers() {
        navigationManager = NavigationManager(this, binding.bottomNavigation)
        themeManager = ThemeManager(this)
        languageManager = LanguageManager(this)
    }

    private fun observeViewModel() {
        navigationManager.setupNavigation(viewModel.isFirstLaunch)
        themeManager.observeTheme(viewModel.darkMode)
        languageManager.observeLanguage(viewModel.language)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigationManager.navigateUp() || super.onSupportNavigateUp()
    }
}