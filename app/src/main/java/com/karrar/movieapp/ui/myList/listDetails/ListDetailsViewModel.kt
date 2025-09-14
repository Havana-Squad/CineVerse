package com.karrar.movieapp.ui.myList.listDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.usecases.mylist.DeleteMovieFromMyListUseCase
import com.karrar.movieapp.domain.usecases.mylist.GetMyMediaListDetailsUseCase
import com.karrar.movieapp.domain.usecases.tip.CloseCategoryTipUseCase
import com.karrar.movieapp.domain.usecases.tip.GetCategoryTipStatusUseCase
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.ui.category.uiState.ErrorUIState
import com.karrar.movieapp.ui.myList.listDetails.listDetailsUIState.ListDetailsUIEvent
import com.karrar.movieapp.ui.myList.listDetails.listDetailsUIState.ListDetailsUIState
import com.karrar.movieapp.ui.myList.listDetails.listDetailsUIState.SavedMediaUIState
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ListDetailsViewModel @Inject constructor(
    private val getMyMediaListDetailsUseCase: GetMyMediaListDetailsUseCase,
    private val getCategoryTipStatusUseCase: GetCategoryTipStatusUseCase,
    private val closeCategoryTipUseCase: CloseCategoryTipUseCase,
    private val mediaUIStateMapper: MediaUIStateMapper,
    private val deleteMovieFromMyListUseCase: DeleteMovieFromMyListUseCase,
    saveStateHandle: SavedStateHandle
) : BaseViewModel(), ListDetailsInteractionListener {

    val args = ListDetailsFragmentArgs.fromSavedStateHandle(saveStateHandle)

    private val _listDetailsUIState = MutableStateFlow(ListDetailsUIState())
    val listDetailsUIState = _listDetailsUIState.asStateFlow()

    private val _listDetailsUIEvent = MutableStateFlow<Event<ListDetailsUIEvent?>>(Event(null))
    val listDetailsUIEvent = _listDetailsUIEvent.asStateFlow()

    init {
        getData()
    }

    override fun getData() {
        _listDetailsUIState.update {
            it.copy(isLoading = true, isEmpty = false, error = emptyList())
        }
        viewModelScope.launch {
            try {
                val tip = getCategoryTipStatusUseCase()
                val result =
                    getMyMediaListDetailsUseCase(args.id).map { mediaUIStateMapper.map(it) }
                _listDetailsUIState.update {
                    it.copy(
                        isLoading = false,
                        isEmpty = result.isEmpty(),
                        savedMedia = result,
                        isTipShown = tip
                    )
                }

            } catch (t: Throwable) {
                _listDetailsUIState.update {
                    it.copy(
                        isLoading = false, error = listOf(
                            ErrorUIState(0, t.message.toString())
                        )
                    )
                }
            }
        }
    }

    override fun onItemClick(item: SavedMediaUIState) {
        _listDetailsUIEvent.update { Event(ListDetailsUIEvent.OnItemSelected(item)) }
    }

    override fun onDeleteBtnClick(item: Int) {
        viewModelScope.launch {
            try {
                deleteMovieFromMyListUseCase(args.id, item)
                val currentList = _listDetailsUIState.value.savedMedia.toMutableList()
                currentList.removeAll { it.mediaID == item }

                _listDetailsUIState.update {
                    it.copy(
                        savedMedia = currentList,
                        isEmpty = currentList.isEmpty(),
                        error = emptyList()
                    )
                }
            }catch (t: Throwable){
                _listDetailsUIState.update {
                    it.copy(
                        error = listOf(
                            ErrorUIState(0, t.message.toString())
                        )
                    )
                }
            }
        }
    }

    fun closeTip(){
        viewModelScope.launch {
            try {
                closeCategoryTipUseCase()
                _listDetailsUIState.update { it.copy(isTipShown = false) }
            }catch (t: Throwable){
                _listDetailsUIState.update {
                    it.copy(
                        error = listOf(
                            ErrorUIState(0, t.message.toString())
                        )
                    )
                }
            }
        }
    }
}

