package com.example.towolist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.towolist.database.dao.*
import com.example.towolist.database.entity.*

@Database(
    entities = [ToWatchMovieEntity::class,
                WatchedMovieEntity::class,
                WatchNowEntity::class,
                BuyRentEntity::class,
                ToWatchMovieWatchNowCrossRefEntity::class,
                ToWatchMovieBuyRentCrossRefEntity::class],
    version = 1
)
abstract class ToWoDatabase : RoomDatabase() {

    companion object {
        private const val NAME = "toWo.db"

        fun create(context: Context): ToWoDatabase =
            Room.databaseBuilder(context, ToWoDatabase::class.java, NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }

    abstract fun toWatchMovieDao(): ToWatchMovieDao
    abstract fun watchedMovieDao(): WatchedMovieDao
    abstract fun watchNowDao(): WatchNowDao
    abstract fun buyRentDao() : BuyRentDao
    abstract fun toWatchMovieWatchNowCrossRefDao(): ToWatchMovieWatchNowCrossRefDao
    abstract fun toWatchMovieBuyRentCrossRefDao() : ToWatchMovieBuyRentCrossRefDao
}