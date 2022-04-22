package com.mbahgojol.storyapp.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.mbahgojol.storyapp.data.model.GetAllStoryResponse
import com.mbahgojol.storyapp.data.model.LoginBody
import com.mbahgojol.storyapp.data.model.RegisterBody
import com.mbahgojol.storyapp.data.remote.story.StoryService
import com.mbahgojol.storyapp.ui.home.storylist.PagingStorySource
import com.mbahgojol.storyapp.utils.ResultState
import com.mbahgojol.storyapp.utils.wrapEspressoIdlingResource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody

class RepositoryImpl constructor(
    private val service: StoryService
) : Repository {
    override fun login(loginBody: LoginBody): Single<ResultState> = service.login(loginBody)

    override fun register(registerBody: RegisterBody): Single<ResultState> =
        service.register(registerBody)

    override fun addStory(body: RequestBody): Single<ResultState> =
        service.addStory(body)

    override fun getAllStory(options: Map<String, String>): Single<GetAllStoryResponse> =
        service.getAllStory(options)

    override fun getAllStory(): Flowable<PagingData<GetAllStoryResponse.Story>> =
        wrapEspressoIdlingResource {
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                ), pagingSourceFactory =
                {
                    PagingStorySource(service)
                }
            ).flowable
        }
}