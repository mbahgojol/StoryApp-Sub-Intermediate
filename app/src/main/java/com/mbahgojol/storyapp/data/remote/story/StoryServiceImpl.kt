package com.mbahgojol.storyapp.data.remote.story

import com.mbahgojol.storyapp.data.local.SharedPref
import com.mbahgojol.storyapp.data.model.GetAllStoryResponse
import com.mbahgojol.storyapp.data.model.LoginBody
import com.mbahgojol.storyapp.data.model.RegisterBody
import com.mbahgojol.storyapp.utils.ResultState
import com.mbahgojol.storyapp.utils.singleIo
import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody

class StoryServiceImpl constructor(
    private val apiStory: ApiStory,
    private val sharedPref: SharedPref
) : StoryService {
    override fun login(loginBody: LoginBody): Single<ResultState> = apiStory.login(loginBody)
        .singleIo()
        .map {
            sharedPref.token = "Bearer ".plus(it.loginResult.token)
            it
        }
        .map(ResultState::Success)

    override fun register(registerBody: RegisterBody): Single<ResultState> =
        apiStory.register(registerBody)
            .singleIo()
            .map(ResultState::Success)

    override fun addStory(
        body: RequestBody
    ): Single<ResultState> = apiStory.addStory(sharedPref.token, body)
        .singleIo()
        .map(ResultState::Success)

    override fun getAllStory(options: Map<String, String>): Single<GetAllStoryResponse> =
        apiStory.getAllStory(sharedPref.token, options)
            .singleIo()
}