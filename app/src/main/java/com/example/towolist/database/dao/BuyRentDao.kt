package com.example.towolist.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.towolist.database.entity.BuyRentEntity
import com.example.towolist.database.entity.WatchedMovieEntity

@Dao
interface BuyRentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEntity(entity: BuyRentEntity)

    @Query("SELECT * FROM BuyRentEntity WHERE serviceId = :id")
    fun getById(id: Long) : BuyRentEntity
}