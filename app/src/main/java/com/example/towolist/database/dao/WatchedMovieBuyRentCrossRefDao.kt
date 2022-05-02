package com.example.towolist.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.towolist.database.entity.WatchedMovieBuyRentCrossRefEntity

@Dao
interface WatchedMovieBuyRentCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEntity(entity: WatchedMovieBuyRentCrossRefEntity)

    @Query("DELETE FROM WatchedMovieBuyRentCrossRefEntity WHERE movieId = :id")
    fun deleteByMovieId(id: Long)
}