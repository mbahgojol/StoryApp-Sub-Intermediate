package com.mbahgojol.storyapp.ui.login

import androidx.lifecycle.MutableLiveData
import com.mbahgojol.storyapp.data.model.LoginBody
import com.mbahgojol.storyapp.data.remote.Repository
import com.mbahgojol.storyapp.utils.BaseViewModel
import com.mbahgojol.storyapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {

    val resultStateLogin = MutableLiveData<ResultState>()

    fun login(loginBody: LoginBody) {
        repository.login(loginBody)
            .doOnSubscribe {
                resultStateLogin.postValue(ResultState.Loading(true))
            }.subscribe({
                resultStateLogin.postValue(ResultState.Loading(false))
                resultStateLogin.value = it
            }, {
                resultStateLogin.postValue(ResultState.Loading(false))
                resultStateLogin.value = ResultState.Error(it)
            }).autoDispose()
    }
}