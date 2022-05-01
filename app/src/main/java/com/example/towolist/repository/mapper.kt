package com.example.towolist.repository

import com.example.towolist.data.MovieItem
import com.example.towolist.data.ServiceItem
import com.example.towolist.database.entity.ToWatchMovieEntity
import com.example.towolist.database.entity.WatchedMovieEntity
import com.example.towolist.webservice.response.MovieListItem
import com.example.towolist.webservice.response.TvShowListItem
import com.example.towolist.webservice.response.WatchProviderInfoResponse

private val rootApiImg = "https://image.tmdb.org/t/p/original"

fun MovieListItem.toMovieItem(): MovieItem =
    MovieItem(
        id = this.id.toLong(),
        savedAt = 0,
        imageSource = "${rootApiImg}${this.posterPath.toString()}",
        name = this.title,
        isMovie = true,
        releaseDate = this.releaseDate,
        popularity = this.popularity,
        voteAverage = this.voteAverage,
        watchNow = mutableListOf(),
        buyRent = mutableListOf(),
        isToWatch = false,
        isWatched = false
    )

fun TvShowListItem.toMovieItem(): MovieItem =
    MovieItem(
        id = this.id.toLong(),
        savedAt = 0,
        imageSource = "${rootApiImg}${this.posterPath.toString()}",
        name = this.name,
        isMovie = false,
        releaseDate = this.firstAirDate,
        popularity = this.popularity,
        voteAverage = this.voteAverage,
        watchNow = mutableListOf(),
        buyRent = mutableListOf(),
        isToWatch = false,
        isWatched = false
    )

fun WatchProviderInfoResponse.toServiceItem(): ServiceItem =
    ServiceItem(
        id = this.providerId.toLong(),
        iconSource = "${rootApiImg}${this.logoPath}",
        name = this.providerName
    )

fun MovieItem.toToWatchMovieEntity(): ToWatchMovieEntity? =
    this.releaseDate?.let {
        ToWatchMovieEntity(
        id = this.id,
        savedAt = System.currentTimeMillis(),
        imageSource = this.imageSource,
        name = this.name,
        isMovie = this.isMovie,
        releaseDate = it,
        popularity = this.popularity,
        voteAverage = this.voteAverage
    )
    }

fun MovieItem.toWatchedMovieEntity(): WatchedMovieEntity? =
    this.releaseDate?.let {
        WatchedMovieEntity(
        id = this.id,
        savedAt = System.currentTimeMillis(),
        imageSource = this.imageSource,
        name = this.name,
        isMovie = this.isMovie,
        releaseDate = it,
        popularity = this.popularity,
        voteAverage = this.voteAverage
    )
    }

fun ToWatchMovieEntity.toMovieItem(): MovieItem =
    MovieItem(
        id = this.id,
        savedAt = this.savedAt,
        imageSource = this.imageSource,
        name = this.name,
        isMovie = this.isMovie,
        releaseDate = this.releaseDate,
        popularity = this.popularity,
        voteAverage = this.voteAverage,
        watchNow = mutableListOf(),
        buyRent = mutableListOf(),
        isToWatch = true,
        isWatched = false
    )

fun WatchedMovieEntity.toMovieItem(): MovieItem =
    MovieItem(
        id = this.id,
        savedAt = this.savedAt,
        imageSource = this.imageSource,
        name = this.name,
        isMovie = this.isMovie,
        releaseDate = this.releaseDate,
        popularity = this.popularity,
        voteAverage = this.voteAverage,
        watchNow = mutableListOf(),
        buyRent = mutableListOf(),
        isToWatch = false,
        isWatched = true
    )
