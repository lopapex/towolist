package com.example.towolist.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ToWatchMovieWithServices(
    @Embedded
    val movie: ToWatchMovieEntity,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "serviceId",
        associateBy = Junction(ToWatchMovieWatchNowCrossRefEntity::class)
    )
    val watchNow: List<WatchNowEntity>,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "serviceId",
        associateBy = Junction(ToWatchMovieBuyRentCrossRefEntity::class)
    )
    val buyRent: List<BuyRentEntity>
)
