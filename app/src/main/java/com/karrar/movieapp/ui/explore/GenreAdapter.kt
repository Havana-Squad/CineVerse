package com.karrar.movieapp.ui.explore

import com.google.android.material.chip.Chip
import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.explore.exploreUIState.GenreUIState

class GenreAdapter(
    genres: List<GenreUIState>,
    listener: ExploreInteractionListener
) : BaseAdapter<GenreUIState>(genres, listener) {

    override val layoutID: Int = R.layout.chip_item_explore_category
    private var selectedId: Int = FAKE_GENRE_ID

    fun setSelectedGenre(newSelectedId: Int) {
        if (selectedId == newSelectedId) return
        selectedId = newSelectedId
        notifyDataSetChanged()
    }

    override fun bind(holder: ItemViewHolder, position: Int) {
        super.bind(holder, position)
        val item = getItemAt(position)
        val chip = holder.binding.root.findViewById<Chip>(R.id.explore_chip)
        chip?.isChecked = (item.genreID == selectedId)
    }

    companion object {
        private const val FAKE_GENRE_ID = -1
    }
}