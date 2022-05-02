package com.example.towolist.database.dao

import androidx.room.*
import com.example.towolist.database.entity.WatchedMovieEntity
import com.example.towolist.database.entity.relation.WatchedMovieWithServices

@Dao
interface WatchedMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEntity(entity: WatchedMovieEntity)

    @Query("DELETE FROM WatchedMovieEntity WHERE movieId = :id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM WatchedMovieEntity WHERE movieId = :id")
    fun getById(id: Long) : WatchedMovieWithServices

    @Query("SELECT * FROM WatchedMovieEntity")
    fun getAll(): List<WatchedMovieWithServices>
}