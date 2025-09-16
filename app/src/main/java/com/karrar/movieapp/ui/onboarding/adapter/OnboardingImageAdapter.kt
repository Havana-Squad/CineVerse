package com.karrar.movieapp.ui.onboarding.adapter

import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseInteractionListener

class OnboardingImageAdapter(
    items: List<Int>,
    listener: BaseInteractionListener
) : BaseAdapter<Int>(items, listener) {

    override val layoutID: Int = R.layout.item_onboarding_image

    override fun areItemsSame(oldItem: Int, newItem: Int): Boolean = oldItem == newItem

    override fun areContentSame(oldPosition: Int, newPosition: Int): Boolean = oldPosition == newPosition
}
