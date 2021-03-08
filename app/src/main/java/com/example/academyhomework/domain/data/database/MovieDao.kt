package com.example.academyhomework.domain.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY rating desc")
    suspend fun getAll():List<MovieEntity>

    @Insert(onConflict = 1)
    suspend fun insert(movie:MovieEntity)


}