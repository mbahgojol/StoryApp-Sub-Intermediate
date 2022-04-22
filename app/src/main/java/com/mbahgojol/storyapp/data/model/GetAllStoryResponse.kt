package com.mbahgojol.storyapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class GetAllStoryResponse(
    val error: Boolean,
    val listStory: MutableList<Story>,
    val message: String
) {
    @Parcelize
    data class Story(
        val description: String,
        val id: String,
        val name: String,
        val photoUrl: String,
        val lat: Double = 0.0,
        val lon: Double = 0.0,
    ) : Parcelable
}