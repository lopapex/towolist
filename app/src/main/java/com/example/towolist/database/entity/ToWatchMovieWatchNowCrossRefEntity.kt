package com.example.towolist.database.entity

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "serviceId"])
data class ToWatchMovieWatchNowCrossRefEntity (
    val movieId: Long,
    val serviceId: Long
)