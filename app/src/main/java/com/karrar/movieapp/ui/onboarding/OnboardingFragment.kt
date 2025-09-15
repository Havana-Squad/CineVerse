package com.karrar.movieapp.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentOnboardingBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.onboarding.adapter.OnboardingContentAdapter
import com.karrar.movieapp.ui.onboarding.adapter.OnboardingImageAdapter
import com.karrar.movieapp.utilities.setSystemBarsColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>() {
    override val layoutIdFragment: Int = R.layout.fragment_onboarding
    override val viewModel: OnboardingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupUI()

        val imageAdapter = OnboardingImageAdapter(viewModel.imagePages)
        val textAdapter = OnboardingContentAdapter(viewModel.textPages)

        binding.onboardingImagePager.adapter = imageAdapter
        binding.onboardingContentPager.adapter = textAdapter

        binding.onboardingImagePager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.onboardingContentPager.currentItem = position
                    updateUiForPage(position, imageAdapter.itemCount)
                }
            }
        )

        binding.btnGoBack.setOnClickListener {
            val current = binding.onboardingImagePager.currentItem
            if (current > 0) {
                binding.onboardingImagePager.currentItem = current - 1
            }
        }

        binding.btnNextContainer.setOnClickListener {
            val current = binding.onboardingImagePager.currentItem
            if (current < imageAdapter.itemCount - 1) {
                binding.onboardingImagePager.currentItem = current + 1
            } else {
                viewModel.completeOnboarding()
                onEvent(OnboardingEvent.HomeEvent)
            }
        }
    }

    private fun setupUI() {
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        setTitle(false, getString(R.string.profile))
        setSystemBarsColor(R.color.background_screen)
    }

    private fun updateUiForPage(position: Int, totalPages: Int) {
        binding.btnGoBack.visibility = if (position > 0) View.VISIBLE else View.GONE

        if (position == totalPages - 1) {
            binding.textLastPage.visibility = View.VISIBLE
        } else {
            binding.textLastPage.visibility = View.GONE
        }
    }


    private fun onEvent(event: OnboardingEvent) {
        when (event) {
            OnboardingEvent.HomeEvent -> {
                val action = OnboardingFragmentDirections.actionOnboardingFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        }
    }
}
