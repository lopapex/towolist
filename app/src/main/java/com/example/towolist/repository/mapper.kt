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
        imageSource = "${rootApiImg}${this.posterPath.toString()}",
        name = this.title,
        releaseDate = this.releaseDate,
        popularity = this.popularity,
        voteAverage = this.voteAverage,
        watchNow = mutableListOf(),
        buyRent = mutableListOf(),
        isToWatch = true,
        isWatched = false
    )

fun TvShowListItem.toMovieItem(): MovieItem =
    MovieItem(
        id = this.id.toLong(),
        imageSource = "${rootApiImg}${this.posterPath.toString()}",
        name = this.name,
        releaseDate = this.firstAirDate,
        popularity = this.popularity,
        voteAverage = this.voteAverage,
        watchNow = mutableListOf(),
        buyRent = mutableListOf(),
        isToWatch = true,
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
        imageSource = this.imageSource,
        name = this.name,
        releaseDate = it,
        popularity = this.popularity,
        voteAverage = this.voteAverage
    )
    }

fun MovieItem.toWatchedMovieEntity(): WatchedMovieEntity? =
    this.releaseDate?.let {
        WatchedMovieEntity(
        id = this.id,
        imageSource = this.imageSource,
        name = this.name,
        releaseDate = it,
        popularity = this.popularity,
        voteAverage = this.voteAverage
    )
    }

fun ToWatchMovieEntity.toMovieItem(): MovieItem =
    MovieItem(
        id = this.id,
        imageSource = this.imageSource,
        name = this.name,
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
        imageSource = this.imageSource,
        name = this.name,
        releaseDate = this.releaseDate,
        popularity = this.popularity,
        voteAverage = this.voteAverage,
        watchNow = mutableListOf(),
        buyRent = mutableListOf(),
        isToWatch = false,
        isWatched = true
    )
