package com.example.academyhomework.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule(appContext: Context) {

    val context = appContext
    @Provides
    fun provideContext():Context = context
}