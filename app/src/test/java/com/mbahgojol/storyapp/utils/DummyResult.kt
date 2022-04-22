package com.mbahgojol.storyapp.utils

import com.mbahgojol.storyapp.data.model.GetAllStoryResponse
import io.reactivex.rxjava3.core.Single

val successResult: Single<ResultState> = Single.just(ResultState.Success(""))

val getResultAllStory: Single<GetAllStoryResponse> =
    Single.just(GetAllStoryResponse(false, mutableListOf(), ""))