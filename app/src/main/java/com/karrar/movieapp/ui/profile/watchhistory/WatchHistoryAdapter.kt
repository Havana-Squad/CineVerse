package com.karrar.movieapp.ui.profile.watchhistory

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseInteractionListener

class WatchHistoryAdapter(
    items: List<MediaHistoryUiState>,
    listener: WatchHistoryInteractionListener,
) : BaseAdapter<MediaHistoryUiState>(items, listener) {
    override val layoutID: Int = R.layout.item_watch_history
}

interface WatchHistoryInteractionListener : BaseInteractionListener {
    fun onClickMovie(item: MediaHistoryUiState)
    fun onClickExplore()
    fun onSwipeToDelete(item: MediaHistoryUiState)
}

@BindingAdapter("genresList")
fun setGenresList(textView: TextView, genres: List<String>?) {
    textView.text = genres?.joinToString(", ") ?: ""
}