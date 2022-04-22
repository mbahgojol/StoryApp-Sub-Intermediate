package com.mbahgojol.storyapp.di

import android.app.Application
import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.mbahgojol.storyapp.BuildConfig
import com.mbahgojol.storyapp.data.local.SharedPref
import com.mbahgojol.storyapp.data.remote.Repository
import com.mbahgojol.storyapp.data.remote.RepositoryImpl
import com.mbahgojol.storyapp.data.remote.story.ApiStory
import com.mbahgojol.storyapp.data.remote.story.StoryServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    var baseUrl = BuildConfig.API_URL

    @Provides
    fun provideOkHttpClient(cache: Cache, @ApplicationContext context: Context): OkHttpClient {
        val chuckerCollector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )

        val chuckerInterceptor = ChuckerInterceptor.Builder(context)
            .collector(chuckerCollector)
            .alwaysReadResponseBody(true)
            .build()

        return OkHttpClient.Builder()
            .cache(cache)
            .apply {
                if (BuildConfig.DEBUG) {
                    val loggingInterceptor = HttpLoggingInterceptor()
                    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(loggingInterceptor)
                    addInterceptor(chuckerInterceptor)
                }
            }.readTimeout(25, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun provideApiStory(retrofit: Retrofit) =
        retrofit.create<ApiStory>()

    @Provides
    fun provideStoryService(apiStory: ApiStory, sharedPref: SharedPref) =
        StoryServiceImpl(apiStory, sharedPref)

    @Provides
    fun provideRepository(
        service: StoryServiceImpl
    ): Repository = RepositoryImpl(service)
}