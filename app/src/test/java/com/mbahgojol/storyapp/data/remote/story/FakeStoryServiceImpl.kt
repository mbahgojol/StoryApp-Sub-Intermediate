package com.mbahgojol.storyapp.data.remote.story

import com.mbahgojol.storyapp.data.model.GetAllStoryResponse
import com.mbahgojol.storyapp.data.model.LoginBody
import com.mbahgojol.storyapp.data.model.RegisterBody
import com.mbahgojol.storyapp.utils.ResultState
import com.mbahgojol.storyapp.utils.getResultAllStory
import com.mbahgojol.storyapp.utils.successResult
import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody

class FakeStoryServiceImpl : StoryService {
    override fun login(loginBody: LoginBody): Single<ResultState> =
        successResult

    override fun register(registerBody: RegisterBody): Single<ResultState> =
        successResult

    override fun addStory(body: RequestBody): Single<ResultState> =
        successResult

    override fun getAllStory(options: Map<String, String>): Single<GetAllStoryResponse> =
        getResultAllStory
}