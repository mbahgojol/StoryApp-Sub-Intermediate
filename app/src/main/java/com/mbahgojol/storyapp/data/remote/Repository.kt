package com.mbahgojol.storyapp.data.remote

import androidx.paging.PagingData
import com.mbahgojol.storyapp.data.model.GetAllStoryResponse
import com.mbahgojol.storyapp.data.remote.story.StoryService
import io.reactivex.rxjava3.core.Flowable

interface Repository : StoryService {
    fun getAllStory(): Flowable<PagingData<GetAllStoryResponse.Story>>
}