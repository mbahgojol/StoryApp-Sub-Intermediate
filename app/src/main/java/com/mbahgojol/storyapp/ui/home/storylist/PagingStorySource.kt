package com.mbahgojol.storyapp.ui.home.storylist

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.mbahgojol.storyapp.data.model.GetAllStoryResponse
import com.mbahgojol.storyapp.data.remote.story.StoryService
import io.reactivex.rxjava3.core.Single

const val PAGE_INDEX = 1

class PagingStorySource constructor(
    private val api: StoryService
) : RxPagingSource<Int, GetAllStoryResponse.Story>() {
    private fun toLoadResult(
        data: MutableList<GetAllStoryResponse.Story>?,
        position: Int
    ): LoadResult<Int, GetAllStoryResponse.Story> =
        LoadResult.Page(
            data = data.orEmpty(),
            prevKey = if (position == 1) null else position.minus(1),
            nextKey = if (data?.isNullOrEmpty() == true) null else position.plus(1)
        )

    override fun getRefreshKey(state: PagingState<Int, GetAllStoryResponse.Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, GetAllStoryResponse.Story>> {
        val position = params.key ?: PAGE_INDEX
        return api.getAllStory(
            hashMapOf(
                "page" to position.toString(),
            )
        ).map {
            toLoadResult(it.listStory, position)
        }.onErrorReturn { LoadResult.Error(it) }
    }
}