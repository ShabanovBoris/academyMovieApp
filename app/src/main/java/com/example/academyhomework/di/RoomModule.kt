package com.example.academyhomework.di

import android.content.Context
import androidx.room.Room
import com.example.academyhomework.di.scopes.AppScope
import com.example.academyhomework.domain.data.MovieDatabase
import com.example.academyhomework.domain.data.database.MovieDatabaseImpl
import com.example.academyhomework.domain.data.database.DbContract
import com.example.academyhomework.domain.data.database.MovieDatabaseRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface RoomModule {

    @AppScope
    @Binds
    fun provideRepository(rep: MovieDatabaseRepository): MovieDatabase

    companion object {
        @Provides
        fun provideDBMovie(applicationContext: Context): MovieDatabaseImpl =
            Room.databaseBuilder(
                applicationContext,
                MovieDatabaseImpl::class.java,
                DbContract.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()


    }
}