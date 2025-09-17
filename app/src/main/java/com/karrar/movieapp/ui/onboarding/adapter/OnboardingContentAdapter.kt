package com.karrar.movieapp.ui.onboarding.adapter

import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseInteractionListener
import com.karrar.movieapp.ui.onboarding.OnboardingText

class OnboardingContentAdapter(
    items: List<OnboardingText>,
    listener: BaseInteractionListener
) : BaseAdapter<OnboardingText>(items, listener) {

    override val layoutID: Int = R.layout.item_onboarding_content
}
