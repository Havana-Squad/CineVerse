package com.karrar.movieapp.ui.main.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.karrar.movieapp.R
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NavigationManager(
    private val activity: AppCompatActivity,
    private val bottomNavigation: BottomNavigationView
) {
    private val navController: NavController by lazy {
        val navHostFragment = activity.supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    private val appBarConfiguration = AppBarConfiguration(
        setOf(
            R.id.homeFragment,
            R.id.exploringFragment,
            R.id.myListFragment,
            R.id.profileFragment,
        )
    )

    fun setupNavigation(isFirstLaunchFlow: StateFlow<Boolean?>) {
        activity.lifecycleScope.launch {
            isFirstLaunchFlow.collect { firstLaunch ->
                firstLaunch?.let {
                    val graph = navController.navInflater.inflate(R.navigation.movie_navigation)
                    graph.setStartDestination(
                        if (it) R.id.onboardingFragment else R.id.homeFragment
                    )
                    navController.graph = graph
                    setupBottomNav()
                }
            }
        }
    }

    private fun setupBottomNav() {
        bottomNavigation.setupWithNavController(navController)
        activity.setupActionBarWithNavController(navController, appBarConfiguration)

        setupBottomNavVisibility()
        setupBottomNavClickListener()
    }

    private fun setupBottomNavVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavigation.isVisible = destination.id !in HIDDEN_BOTTOM_NAV_DESTINATIONS
        }
    }

    private fun setupBottomNavClickListener() {
        bottomNavigation.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, navController)
            navController.popBackStack(item.itemId, inclusive = false)
            true
        }
    }

    fun navigateUp(): Boolean = navController.navigateUp()

    companion object {
        private val HIDDEN_BOTTOM_NAV_DESTINATIONS = listOf(
            R.id.loginFragment,
            R.id.onboardingFragment
        )
    }
}