package com.example.academyhomework.domain.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.academyhomework.domain.data.database.entities.ActorEntity
import com.example.academyhomework.domain.data.database.entities.MovieDetailsEntity
import com.example.academyhomework.domain.data.database.entities.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY popularity desc")
    suspend fun getAll():List<MovieEntity>


    /**
     * Observes list of films.
     *
     * @return all movies.
     * unused still
     */
    @Query("SELECT * FROM movie ORDER BY popularity desc")
    fun getAllLive(): LiveData<List<MovieEntity>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movies:List<MovieEntity>)

    @Query("DELETE FROM movie")
    suspend fun clear()

    @Query("SELECT * FROM movie_details WHERE _id = :id")
    suspend fun getDetailsById(id:Long): MovieDetailsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetails(details: MovieDetailsEntity)

    @Query("SELECT * FROM actors WHERE id = :id")
    suspend fun getActorById(id:Long): ActorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActors(actors:List<ActorEntity>)
}