package com.example.towolist.database.entity.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.towolist.database.entity.*

data class WatchedMovieWithServices(
    @Embedded
    val movie: ToWatchMovieEntity,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "serviceId",
        associateBy = Junction(WatchedMovieWatchNowCrossRefEntity::class)
    )
    val watchNow: List<WatchNowEntity>,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "serviceId",
        associateBy = Junction(WatchedMovieBuyRentCrossRefEntity::class)
    )
    val buyRent: List<BuyRentEntity>
)
