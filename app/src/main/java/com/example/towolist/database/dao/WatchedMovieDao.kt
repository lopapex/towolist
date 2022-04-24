package com.example.towolist.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.towolist.database.entity.WatchedMovieEntity

@Dao
interface WatchedMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEntity(entity: WatchedMovieEntity)

    @Query("DELETE FROM WatchedMovieEntity WHERE id = :id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM WatchedMovieEntity")
    fun getAll(): List<WatchedMovieEntity>
}