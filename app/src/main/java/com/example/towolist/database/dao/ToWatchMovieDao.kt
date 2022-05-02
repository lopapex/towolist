package com.example.towolist.database.dao

import androidx.room.*
import com.example.towolist.database.entity.ToWatchMovieEntity
import com.example.towolist.database.entity.relation.ToWatchMovieWithServices

@Dao
interface ToWatchMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEntity(entity: ToWatchMovieEntity)

    @Query("DELETE FROM ToWatchMovieEntity WHERE movieId = :id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM ToWatchMovieEntity WHERE movieId = :id")
    fun getById(id: Long) : ToWatchMovieWithServices

    @Query("SELECT * FROM ToWatchMovieEntity")
    fun getAll(): List<ToWatchMovieWithServices>
}