package com.mbahgojol.storyapp.data.remote.story

import com.mbahgojol.storyapp.data.model.GetAllStoryResponse
import com.mbahgojol.storyapp.data.model.LoginBody
import com.mbahgojol.storyapp.data.model.RegisterBody
import com.mbahgojol.storyapp.utils.ResultState
import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody

interface StoryService {
    fun login(loginBody: LoginBody): Single<ResultState>
    fun register(registerBody: RegisterBody): Single<ResultState>
    fun addStory(
        body: RequestBody
    ): Single<ResultState>

    fun getAllStory(options: Map<String, String>): Single<GetAllStoryResponse>
}