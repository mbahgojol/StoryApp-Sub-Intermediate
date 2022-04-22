package com.mbahgojol.storyapp.data.remote.story

import com.mbahgojol.storyapp.data.model.*
import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiStory {
    @POST("login")
    fun login(@Body loginBody: LoginBody): Single<LoginResponse>

    @POST("register")
    fun register(@Body registerBody: RegisterBody): Single<GlobalResponse>

    @POST("stories")
    fun addStory(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Single<GlobalResponse>

    @GET("stories")
    fun getAllStory(
        @Header("Authorization") auth: String,
        @QueryMap options: Map<String, String>
    ): Single<GetAllStoryResponse>
}