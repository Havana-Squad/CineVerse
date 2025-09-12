package com.karrar.movieapp.ui.explore

import androidx.recyclerview.widget.DiffUtil
import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.*
import com.karrar.movieapp.ui.explore.exploreUIState.MediaUIState


class ExploreGridAdapter(listener: ExploreInteractionListener)
    : BasePagingAdapter<MediaUIState>(MediaComparator, listener){
    override val layoutID: Int = R.layout.item_explore

    object MediaComparator : DiffUtil.ItemCallback<MediaUIState>() {
        override fun areItemsTheSame(oldItem: MediaUIState, newItem: MediaUIState) =
            oldItem.mediaID == newItem.mediaID

        override fun areContentsTheSame(oldItem: MediaUIState, newItem: MediaUIState) =
            oldItem == newItem
    }
}

