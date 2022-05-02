package com.example.towolist.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.towolist.database.entity.WatchNowEntity

@Dao
interface WatchNowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEntity(entity: WatchNowEntity)

    @Query("SELECT * FROM WatchNowEntity WHERE serviceId = :id")
    fun getById(id: Long) : WatchNowEntity
}