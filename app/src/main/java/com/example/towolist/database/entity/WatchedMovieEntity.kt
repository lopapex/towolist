package com.example.towolist.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WatchedMovieEntity(
    @PrimaryKey
    val id: Long,
    val savedAt: Long,
    val imageSource: String,
    val name: String,
    val isMovie: Boolean,
    val releaseDate: String,
    val popularity: Float,
    val voteAverage: Float
)