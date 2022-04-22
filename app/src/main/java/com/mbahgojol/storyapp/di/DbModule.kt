package com.mbahgojol.storyapp.di

import android.content.Context
import com.mbahgojol.storyapp.data.local.SharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context): SharedPref = SharedPref(context)
}