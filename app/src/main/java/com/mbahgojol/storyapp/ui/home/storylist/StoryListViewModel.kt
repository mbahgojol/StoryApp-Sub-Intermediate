@file:OptIn(ExperimentalCoroutinesApi::class)

package com.mbahgojol.storyapp.ui.home.storylist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.mbahgojol.storyapp.data.model.GetAllStoryResponse
import com.mbahgojol.storyapp.data.remote.Repository
import com.mbahgojol.storyapp.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class StoryListViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel() {

    val resultStory = MutableLiveData<PagingData<GetAllStoryResponse.Story>>()

    fun getListStory() {
        repository.getAllStory()
            .cachedIn(viewModelScope)
            .subscribe {
                resultStory.value = it
            }.autoDispose()
    }
}