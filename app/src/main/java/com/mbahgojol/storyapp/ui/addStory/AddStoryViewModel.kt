package com.mbahgojol.storyapp.ui.addStory

import androidx.lifecycle.MutableLiveData
import com.mbahgojol.storyapp.data.remote.Repository
import com.mbahgojol.storyapp.utils.BaseViewModel
import com.mbahgojol.storyapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {

    val resultStateStory = MutableLiveData<ResultState>()

    fun upload(body: MultipartBody) {
        repository.addStory(body)
            .doOnSubscribe {
                resultStateStory.postValue(ResultState.Loading(true))
            }
            .subscribe({
                resultStateStory.postValue(ResultState.Loading(false))
                resultStateStory.value = it
            }, {
                resultStateStory.postValue(ResultState.Loading(false))
                resultStateStory.value = ResultState.Error(it)
            })
            .autoDispose()
    }
}