package com.example.towolist.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.towolist.database.entity.ToWatchMovieEntity

@Dao
interface ToWatchMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEntity(entity: ToWatchMovieEntity)

    @Query("DELETE FROM ToWatchMovieEntity WHERE id = :id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM ToWatchMovieEntity")
    fun getAll(): List<ToWatchMovieEntity>
}