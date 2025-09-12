package com.karrar.movieapp.ui.explore

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentExploringBinding
import com.karrar.movieapp.ui.adapters.LoadUIStateAdapter
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.explore.exploreUIState.ExploreViewMode
import com.karrar.movieapp.ui.explore.exploreUIState.ExploringUIEvent
import com.karrar.movieapp.ui.explore.exploreUIState.MediaUIState
import com.karrar.movieapp.utilities.Constants
import com.karrar.movieapp.utilities.collect
import com.karrar.movieapp.utilities.collectLast
import com.karrar.movieapp.utilities.setSpanSize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.exp


@AndroidEntryPoint
class ExploringFragment : BaseFragment<FragmentExploringBinding>() {
    override val layoutIdFragment: Int = R.layout.fragment_exploring
    override val viewModel: ExploringViewModel by viewModels()
    private val exploreGridAdapter by lazy { ExploreGridAdapter(viewModel) }
    private val exploreListAdapter by lazy { ExploreListAdapter(viewModel) }
    private lateinit var genreAdapter: GenreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMediaAdapter()
        setGenreAdapter()
        setToggleButtons()
        collectEvent()
        collectData()
    }

    private fun setToggleButtons() {
        binding.viewGridButton.setOnClickListener {
            viewModel.onClickViewMode(ExploreViewMode.GRID)
        }

        binding.viewListButton.setOnClickListener {
            viewModel.onClickViewMode(ExploreViewMode.LIST)
        }
    }

    private fun collectData() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                genreAdapter.setItems(state.genres)
                genreAdapter.setSelectedGenre(state.selectedCategoryID)
                collectLast(viewModel.uiState.value.media) {
                    if(state.selectedViewMode == ExploreViewMode.GRID) {
                        exploreGridAdapter.submitData(it)
                    } else {
                        exploreListAdapter.submitData(it)
                    }
                }
            }
        }
    }

    private fun setMediaAdapter(viewMode: ExploreViewMode = ExploreViewMode.GRID) {
        if(viewMode == ExploreViewMode.GRID) {
            val footerAdapter = LoadUIStateAdapter(exploreGridAdapter::retry)
            binding.recyclerMedia.adapter = exploreGridAdapter.withLoadStateFooter(footerAdapter)
            val layoutManager = GridLayoutManager(requireContext(), 2)
            binding.recyclerMedia.layoutManager = layoutManager
            layoutManager.setSpanSize(footerAdapter, exploreGridAdapter, layoutManager.spanCount)
            collect(
                flow = exploreGridAdapter.loadStateFlow,
                action = { viewModel.setErrorUiState(it) })

            lifecycleScope.launch {
                exploreGridAdapter.submitData(viewModel .uiState.value.media.first())
            }
        } else {
            val footerAdapter = LoadUIStateAdapter(exploreListAdapter::retry)
            binding.recyclerMedia.adapter = exploreListAdapter.withLoadStateFooter(footerAdapter)
            val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            binding.recyclerMedia.layoutManager = layoutManager
            collect(
                flow = exploreListAdapter.loadStateFlow,
                action = { viewModel.setErrorUiState(it) }
            )
            lifecycleScope.launch {
                exploreListAdapter.submitData(viewModel.uiState.value.media.first())
            }
        }
    }

    private fun setGenreAdapter() {
        genreAdapter = GenreAdapter(emptyList(), viewModel)

        binding.rvGenres.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = genreAdapter
        }
    }

    private fun collectEvent() {
        collectLast(viewModel.exploringUIEvent) {
            it?.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: ExploringUIEvent) {
        when (event) {
            ExploringUIEvent.RetryEvent -> exploreGridAdapter.retry()
            ExploringUIEvent.SearchEvent -> navigateToSearch()
            is ExploringUIEvent.SelectedCategory -> viewModel.getMediaList(selectedCategory = event.categoryID)
            is ExploringUIEvent.SelectedMediaType -> viewModel.getMediaList(selectedMediaType = event.mediaTypeID)
            is ExploringUIEvent.ClickMediaEvent -> navigateToMediaDetails(event.mediaItem)
            is ExploringUIEvent.SelectedViewMode -> setMediaAdapter(event.viewMode)
        }
    }

    private fun navigateToSearch() {
        val extras = FragmentNavigatorExtras(binding.inputSearch to "search_box")
        Navigation.findNavController(binding.root)
            .navigate(
                ExploringFragmentDirections.actionExploringFragmentToSearchFragment(),
                extras
            )
    }

    private fun navigateToMediaDetails(item: MediaUIState) {
        when (item.mediaType) {
            Constants.MOVIE -> {
                findNavController().navigate(
                    ExploringFragmentDirections.actionExploringFragmentToMovieDetailFragment(
                        item.mediaID
                    )
                )
            }
            Constants.TV_SHOWS -> {
                findNavController().navigate(
                    ExploringFragmentDirections.actionExploringFragmentToTvShowDetailsFragment(
                        item.mediaID
                    )
                )
            }
        }
    }

}