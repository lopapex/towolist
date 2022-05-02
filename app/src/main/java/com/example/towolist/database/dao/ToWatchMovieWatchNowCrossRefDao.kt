package com.example.towolist.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.towolist.database.entity.ToWatchMovieWatchNowCrossRefEntity

@Dao
interface ToWatchMovieWatchNowCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEntity(entity: ToWatchMovieWatchNowCrossRefEntity)

    @Query("DELETE FROM ToWatchMovieWatchNowCrossRefEntity WHERE movieId = :id")
    fun deleteByMovieId(id: Long)
}