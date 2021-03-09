package com.example.academyhomework.domain.data.database

import androidx.room.*

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY release_date desc")
    suspend fun getAll():List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movies:List<MovieEntity>)

    @Query("DELETE FROM movie")
    suspend fun clear()

    @Query("SELECT * FROM movie_details WHERE _id = :id")
    suspend fun getDetailsById(id:Long):MovieDetailsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetails(details:MovieDetailsEntity)

    @Query("SELECT * FROM actors WHERE id = :id")
    suspend fun getActorById(id:Long):ActorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActors(actors:List<ActorEntity>)
}