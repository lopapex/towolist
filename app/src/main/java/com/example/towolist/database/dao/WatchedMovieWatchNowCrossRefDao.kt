package com.example.towolist.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.towolist.database.entity.WatchedMovieWatchNowCrossRefEntity

@Dao
interface WatchedMovieWatchNowCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEntity(entity: WatchedMovieWatchNowCrossRefEntity)

    @Query("DELETE FROM WatchedMovieWatchNowCrossRefEntity WHERE movieId = :id")
    fun deleteByMovieId(id: Long)
}