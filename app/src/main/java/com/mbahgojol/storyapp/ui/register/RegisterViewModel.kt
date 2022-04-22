package com.mbahgojol.storyapp.ui.register

import androidx.lifecycle.MutableLiveData
import com.mbahgojol.storyapp.data.model.RegisterBody
import com.mbahgojol.storyapp.data.remote.Repository
import com.mbahgojol.storyapp.utils.BaseViewModel
import com.mbahgojol.storyapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {

    val resultStateRegister = MutableLiveData<ResultState>()

    fun register(registerBody: RegisterBody) {
        repository.register(registerBody)
            .doOnSubscribe {
                resultStateRegister.value = ResultState.Loading(true)
            }
            .doAfterTerminate {
                resultStateRegister.value = ResultState.Loading(false)
            }
            .subscribe({
                resultStateRegister.value = it
            }, {
                resultStateRegister.value = ResultState.Error(it)
            }).autoDispose()
    }
}