package com.karrar.movieapp.ui.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.karrar.movieapp.databinding.ItemOnboardingContentBinding
import com.karrar.movieapp.ui.onboarding.OnboardingText

class OnboardingContentAdapter(
    private val pages: List<OnboardingText>
) : RecyclerView.Adapter<OnboardingContentAdapter.PageViewHolder>() {

    class PageViewHolder(private val binding: ItemOnboardingContentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(page: OnboardingText) {
            binding.title.text = itemView.context.getString(page.title)
            binding.description.text = itemView.context.getString(page.description)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val binding = ItemOnboardingContentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount(): Int = pages.size
}
