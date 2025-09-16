package com.karrar.movieapp.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentProfileBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.profile.editProfile.EditProfileBottomSheetFragment
import com.karrar.movieapp.ui.profile.language.LanguageBottomSheetFragment
import com.karrar.movieapp.ui.profile.logout.LogoutBottomSheetFragment
import com.karrar.movieapp.utilities.Constants
import com.karrar.movieapp.utilities.collectLast
import com.karrar.movieapp.utilities.setSystemBarsColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override val layoutIdFragment: Int = R.layout.fragment_profile
    override val viewModel: ProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()
        observeViewModel()
    }


    private fun setupUI() {
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        setTitle(false, getString(R.string.profile))
        setSystemBarsColor(R.color.background_screen)
    }

    private fun observeViewModel() {
        viewModel.getData()

        collectLast(viewModel.profileUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }

        collectLast(viewModel.darkMode) { darkMode ->
            if (binding.switchTheme.isChecked != darkMode) {
                binding.switchTheme.isChecked = darkMode
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.logoutEvent.collect {
                    onEvent(ProfileUIEvent.LoginEvent)
                }
            }
        }
    }

    private fun setupListeners(){

        binding.switchTheme.setOnCheckedChangeListener { _, isEnabled ->
            viewModel.toggleThemeSwitch(isEnabled = isEnabled)
        }

        binding.logoutSelector.setOnClickListener {
            val bottomSheet = LogoutBottomSheetFragment()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

        binding.profileSelector.setOnClickListener {
            if (viewModel.profileDetailsUIState.value.isLoggedIn) {
                val bottomSheet = EditProfileBottomSheetFragment()
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
            } else {
                onEvent(ProfileUIEvent.LoginEvent)
            }
        }

        binding.languageSelector.setOnClickListener {
            val bottomSheet = LanguageBottomSheetFragment()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }
    }
    private fun onEvent(event: ProfileUIEvent) {
        when (event) {
            ProfileUIEvent.OpenEditProfileWebsite -> {
                val intent = Intent(Intent.ACTION_VIEW, WEBSITE_URL.toUri())
                startActivity(intent)
                return
            }
            ProfileUIEvent.LoginEvent -> {
                val action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment(Constants.PROFILE)
                findNavController().navigate(action)
            }
            ProfileUIEvent.RatedMoviesEvent -> {
                val action = ProfileFragmentDirections.actionProfileFragmentToRatedMoviesFragment()
                findNavController().navigate(action)
            }
            ProfileUIEvent.WatchHistoryEvent -> {
                val action = ProfileFragmentDirections.actionProfileFragmentToWatchHistoryFragment()
                findNavController().navigate(action)
            }
            ProfileUIEvent.MyCollectionEvent -> {
                val action = ProfileFragmentDirections.actionProfileFragmentToMyCollectionFragment()
                findNavController().navigate(action)
            }
        }
    }


    companion object {
        private const val WEBSITE_URL = "https://www.themoviedb.org/settings/profile"
    }


}