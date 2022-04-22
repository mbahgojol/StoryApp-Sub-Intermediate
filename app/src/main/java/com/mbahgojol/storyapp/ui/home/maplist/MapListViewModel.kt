package com.mbahgojol.storyapp.ui.home.maplist

import androidx.lifecycle.MutableLiveData
import com.mbahgojol.storyapp.data.remote.Repository
import com.mbahgojol.storyapp.utils.BaseViewModel
import com.mbahgojol.storyapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapListViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {

    val storyListState = MutableLiveData<ResultState>()

    fun getListStory() {
        repository.getAllStory(
            hashMapOf(
                "location" to "1"
            )
        )
            .doOnSubscribe {
                storyListState.postValue(ResultState.Loading(true))
            }
            .subscribe({
                storyListState.postValue(ResultState.Loading(false))
                storyListState.value = ResultState.Success(it)
            }, {
                storyListState.postValue(ResultState.Loading(false))
                storyListState.value = ResultState.Error(it)
            })
            .autoDispose()
    }
}