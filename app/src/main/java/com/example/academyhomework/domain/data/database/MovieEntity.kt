package com.example.academyhomework.domain.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = DbContract.Movie.TABLE_NAME,
    indices = [Index(DbContract.Movie.COLUMN_ID)]
)
data class MovieEntity(

    @PrimaryKey//(autoGenerate = true)
    @ColumnInfo(name = DbContract.Movie.COLUMN_ID)
        val id:Long = 0,

    @ColumnInfo(name = DbContract.Movie.TITLE)
        val title:String,

    @ColumnInfo(name = DbContract.Movie.GENRES)
        val genres:String,

    @ColumnInfo(name = DbContract.Movie.RATING)
        val rating:Double,

    @ColumnInfo(name = DbContract.Movie.IMAGE_URL)
        val imageUrl:String,

    @ColumnInfo(name = DbContract.Movie.RELEASE_DATE)
        val releaseDate:String
)