package com.example.academyhomework.domain.data.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [MovieEntity::class],version = 1)
abstract class DataBaseMovie:RoomDatabase() {

    abstract val movieDao: MovieDao

    companion object{
        fun create(applicationContext: Context):DataBaseMovie = Room.databaseBuilder(
            applicationContext,
            DataBaseMovie::class.java,
            DbContract.DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }
}