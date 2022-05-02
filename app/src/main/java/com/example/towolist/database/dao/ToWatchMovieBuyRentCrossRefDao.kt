package com.example.towolist.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.towolist.database.entity.ToWatchMovieBuyRentCrossRefEntity

@Dao
interface ToWatchMovieBuyRentCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEntity(entity: ToWatchMovieBuyRentCrossRefEntity)

    @Query("DELETE FROM ToWatchMovieBuyRentCrossRefEntity WHERE movieId = :id")
    fun deleteByMovieId(id: Long)
}