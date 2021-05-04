package com.example.academyhomework.domain.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.academyhomework.domain.data.database.entities.ActorEntity
import com.example.academyhomework.domain.data.database.entities.MovieDetailsEntity
import com.example.academyhomework.domain.data.database.entities.MovieEntity


@Database(entities = [MovieEntity::class, MovieDetailsEntity::class, ActorEntity::class],version = 1)
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