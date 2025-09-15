package com.karrar.movieapp.ui.main

import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
        setTheme(R.style.Theme_MovieApp)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupStartDestination()
        observeAppTheme()
        observeAppLanguage()

        installSplashScreen()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun observeAppTheme() {
        lifecycleScope.launch {
            viewModel.darkMode.collectLatest { darkMode ->
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

    private fun observeAppLanguage() {
        lifecycleScope.launch {
            viewModel.language.collect { lang ->
                updateLocale(lang)
            }
        }
    }

    private fun updateLocale(language: String) {
        val current = resources.configuration.locales.get(0).language
        if (current == language) {
            return
        }

        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        resources.updateConfiguration(config, resources.displayMetrics)

        recreate()
    }


    private fun setupStartDestination() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        lifecycleScope.launch {
            viewModel.isFirstLaunch.collect { firstLaunch ->
                val graph = navController.navInflater.inflate(R.navigation.movie_navigation)
                graph.setStartDestination(
                    if (firstLaunch) R.id.onboardingFragment else R.id.homeFragment
                )
                navController.graph = graph
                setupBottomNav(navController)
            }
        }
    }


    private fun setupBottomNav(navController: NavController) {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.exploringFragment,
                R.id.myListFragment,
                R.id.profileFragment,
            )
        )
        binding.bottomNavigation.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        setBottomNavigationVisibility(navController)
        setNavigationController(navController)
    }

    private fun setBottomNavigationVisibility(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.isVisible = destination.id !in listOf(
                R.id.loginFragment,
                R.id.onboardingFragment
            )
        }
    }

    private fun setNavigationController(navController: NavController) {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, navController)
            navController.popBackStack(item.itemId, inclusive = false)
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp()
    }
}