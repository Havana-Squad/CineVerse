package com.karrar.movieapp.ui.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.transition.ChangeTransform
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentSearchBinding
import com.karrar.movieapp.ui.adapters.LoadUIStateAdapter
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.search.adapters.*
import com.karrar.movieapp.ui.search.mediaSearchUIState.*
import com.karrar.movieapp.utilities.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    override val layoutIdFragment: Int = R.layout.fragment_search
    override val viewModel: SearchViewModel by viewModels()
    private lateinit var speechRecognitionLauncher: ActivityResultLauncher<Intent>

    private val mediaSearchAdapter by lazy { MediaSearchAdapter(viewModel) }
    private val actorSearchAdapter by lazy { ActorSearchAdapter(viewModel) }

    private val oldValue = MutableStateFlow(MediaSearchUIState())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedElementEnterTransition = ChangeTransform()
        setTitle(false)
        setSearchHistoryAdapter()
        setSearchSuggestionAdapter()
        setSearchTextField()
        setupSpeechRecognition()
        getSearchResultsBySearchTerm()

        // Observe UI state changes to update the RecyclerView
        observeUIState()

        collectLast(viewModel.searchUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun observeUIState() {
        // Observe search type changes to setup RecyclerView
        lifecycleScope.launch {
            viewModel.uiState
                .map { it.searchTypes to it.isSearchResultVisible }
                .distinctUntilChanged()
                .collectLatest { (searchType, isVisible) ->
                    if (isVisible) {
                        when (searchType) {
                            MediaTypes.ACTOR -> setupActorRecyclerView()
                            else -> setupMediaRecyclerView()
                        }
                    }
                }
        }

        // Observe search results data separately
        lifecycleScope.launch {
            viewModel.uiState
                .map { it.searchResult to it.isSearchResultVisible }
                .distinctUntilChanged()
                .collectLatest { (searchResult, isVisible) ->
                    if (isVisible) {
                        searchResult.collectLatest { pagingData ->
                            when (viewModel.uiState.value.searchTypes) {
                                MediaTypes.ACTOR -> actorSearchAdapter.submitData(pagingData)
                                else -> mediaSearchAdapter.submitData(pagingData)
                            }
                        }
                    }
                }
        }
    }

    private fun setSearchTextField() {
        binding.inputSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                viewModel.onLoadSearchResults()
                val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                true
            } else {
                false
            }
        }
    }

    private fun setSearchSuggestionAdapter() {
        binding.recyclerSearchSuggestions.adapter = SearchSuggestionsAdapter(mutableListOf(), viewModel)
    }

    private fun setupSpeechRecognition() {
        speechRecognitionLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            handleSpeechRecognitionResult(result)
        }
    }

    private fun startSpeechRecognition(
        maxResults: Int = 1
    ) {
        val currentLocale = resources.configuration.locales[0]

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                currentLocale.toLanguageTag()
            )
            putExtra(RecognizerIntent.EXTRA_PROMPT, requireContext().getString(R.string.speak_now))
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, maxResults)
        }

        try {
            speechRecognitionLauncher.launch(intent)
        } catch (e: Exception) {
            onSpeechRecognitionError()
        }
    }

    private fun handleSpeechRecognitionResult(result: ActivityResult) {
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                val results = result.data
                    ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    ?: emptyList()
                onSpeechRecognitionSuccess(results)
            }
            else -> onSpeechRecognitionError()
        }
    }

    private fun onSpeechRecognitionSuccess(results: List<String>) {
        if (results.isNotEmpty()) {
            val recognizedText = results[0]
            viewModel.onSearchInputChange(recognizedText)
        }
    }

    private fun onSpeechRecognitionError() {
        Toast.makeText(requireContext(),
            getString(R.string.speech_recognition_failed), Toast.LENGTH_SHORT).show()
    }

    private fun setSearchHistoryAdapter() {
        val inputMethodManager =
            binding.inputSearch.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.inputSearch, InputMethodManager.SHOW_IMPLICIT)

        binding.recyclerSearchHistory.adapter = SearchHistoryAdapter(mutableListOf(), viewModel)
    }

    @OptIn(FlowPreview::class)
    private fun getSearchResultsBySearchTerm() {
        lifecycleScope.launch {
            viewModel.uiState.debounce(500).distinctUntilChanged { old, new ->
                old.searchInput == new.searchInput
            }.collectLatest { searchTerm ->
                Log.e("SearchSuggestionBug", "${searchTerm.searchInput} -- ${viewModel.uiState.value.isSearchSuggestionClicked}")
                if (viewModel.uiState.value.isSearchSuggestionClicked.not() &&
                    searchTerm.searchInput.isNotBlank()
                    && oldValue.value.searchInput != viewModel.uiState.value.searchInput
                    || oldValue.value.searchTypes != viewModel.uiState.value.searchTypes) {
                    viewModel.addSearchHistoryItem()
                    viewModel.getSearchSuggestions()
                    oldValue.emit(viewModel.uiState.value)
                } else if(viewModel.uiState.value.isSearchSuggestionClicked) {
                    viewModel.resetSearchSuggestionFlag()
                }
            }
        }
    }

    private fun onEvent(event: SearchUIEvent) {
        when (event) {
            is SearchUIEvent.ClickActorEvent -> {
                navigateToActorDetails(event.actorID)
            }
            SearchUIEvent.ClickBackEvent -> {
                popFragment()
            }
            is SearchUIEvent.ClickMediaEvent -> {
                when (event.mediaUIState.mediaTypes) {
                    Constants.MOVIE -> navigateToMovieDetails(event.mediaUIState.mediaID)
                    Constants.TV_SHOWS -> navigateToSeriesDetails(event.mediaUIState.mediaID)
                }
            }
            SearchUIEvent.ClickRetryEvent -> {
                actorSearchAdapter.retry()
                mediaSearchAdapter.retry()
            }

            SearchUIEvent.ClickMicrophoneEvent -> startSpeechRecognition()
        }
    }

    private fun navigateToMovieDetails(movieId: Int) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToMovieDetailFragment(
                movieId
            )
        )
    }

    private fun navigateToSeriesDetails(seriesId: Int) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToTvShowDetailsFragment(
                seriesId
            )
        )
    }

    private fun navigateToActorDetails(actorId: Int) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToActorDetailsFragment(
                actorId
            )
        )
    }

    private fun setupMediaRecyclerView() {
        val footerAdapter = LoadUIStateAdapter(mediaSearchAdapter::retry)
        binding.recyclerMedia.adapter = mediaSearchAdapter.withLoadStateFooter(footerAdapter)
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerMedia.layoutManager = layoutManager
        layoutManager.setSpanSize(footerAdapter, mediaSearchAdapter, layoutManager.spanCount)

        collect(flow = mediaSearchAdapter.loadStateFlow,
            action = { viewModel.setErrorUiState(it, mediaSearchAdapter.itemCount) })
    }

    private fun setupActorRecyclerView() {
        val footerAdapter = LoadUIStateAdapter(actorSearchAdapter::retry)
        binding.recyclerMedia.adapter = actorSearchAdapter.withLoadStateFooter(footerAdapter)
        binding.recyclerMedia.layoutManager = GridLayoutManager(this@SearchFragment.context, 3)
        setSpanSize(footerAdapter)

        collect(flow = actorSearchAdapter.loadStateFlow,
            action = { viewModel.setErrorUiState(it, actorSearchAdapter.itemCount) })
    }

    private fun setSpanSize(footerAdapter: LoadUIStateAdapter) {
        val mManager = binding.recyclerMedia.layoutManager as GridLayoutManager
        mManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if ((position == actorSearchAdapter.itemCount)
                    && footerAdapter.itemCount > 0
                ) {
                    mManager.spanCount
                } else {
                    1
                }
            }
        }
    }

    private fun popFragment() {
        findNavController().popBackStack()
    }
}