package com.karrar.movieapp.ui.search.adapters

import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseInteractionListener
import com.karrar.movieapp.ui.search.mediaSearchUIState.SearchSuggestionUiState

class SearchSuggestionsAdapter(items: List<SearchSuggestionUiState>, listener: SearchSuggestionsInteractionListener)
    : BaseAdapter<SearchSuggestionUiState>(items,listener){
    override val layoutID: Int = R.layout.item_search_suggestion
}

interface SearchSuggestionsInteractionListener : BaseInteractionListener{
    fun onClickSearchSuggestion(name: String)
}