package com.example.towolist.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ToWatchMovieEntity(
    @PrimaryKey
    val id: Long,
    val imageSource: String,
    val name: String,
    val releaseDate: String,
    val popularity: Float,
    val voteAverage: Float
)