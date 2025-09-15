package com.karrar.movieapp.ui.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.karrar.movieapp.databinding.ItemOnboardingImageBinding

class OnboardingImageAdapter(
    private val images: List<Int>
) : RecyclerView.Adapter<OnboardingImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(private val binding: ItemOnboardingImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageRes: Int) {
            binding.pagerImage.setImageResource(imageRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemOnboardingImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size
}
