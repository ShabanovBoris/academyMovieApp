package com.example.academyhomework.domain.data.database

import android.provider.BaseColumns
import com.example.academyhomework.model.Genre

object DbContract {

    const val DATABASE_NAME = "movie.db"


    object Movie{
        const val TABLE_NAME = "movie"

        const val COLUMN_ID = BaseColumns._ID
        const val TITLE = "movie_title"
        const val GENRES = "genres"
        const val RATING = "rating"
        const val IMAGE_URL = "image_url"
        const val RELEASE_DATE = "release_date"
    }

}