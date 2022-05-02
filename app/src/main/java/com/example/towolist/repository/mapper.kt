package com.example.towolist.repository

import com.example.towolist.data.MovieItem
import com.example.towolist.data.ServiceItem
import com.example.towolist.database.entity.*
import com.example.towolist.database.entity.relation.ToWatchMovieWithServices
import com.example.towolist.database.entity.relation.WatchedMovieWithServices
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

fun WatchNowEntity.toServiceItem(): ServiceItem =
    ServiceItem(
        id = this.serviceId,
        iconSource = this.iconSource,
        name = this.name
    )

fun BuyRentEntity.toServiceItem(): ServiceItem =
    ServiceItem(
        id = this.serviceId,
        iconSource = this.iconSource,
        name = this.name
    )

fun ServiceItem.toWatchNowEntity(): WatchNowEntity =
    WatchNowEntity(
        serviceId = this.id,
        iconSource = this.iconSource,
        name = this.name
    )

fun ServiceItem.toBuyRentEntity(): BuyRentEntity =
    BuyRentEntity(
        serviceId = this.id,
        iconSource = this.iconSource,
        name = this.name
    )

fun MovieItem.toToWatchMovieEntity(): ToWatchMovieEntity? =
    this.releaseDate?.let {
        ToWatchMovieEntity(
        movieId = this.id,
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
        movieId = this.id,
        savedAt = System.currentTimeMillis(),
        imageSource = this.imageSource,
        name = this.name,
        isMovie = this.isMovie,
        releaseDate = it,
        popularity = this.popularity,
        voteAverage = this.voteAverage
    )
    }

fun ToWatchMovieWithServices.toMovieItem(): MovieItem =
    MovieItem(
        id = this.movie.movieId,
        savedAt = this.movie.savedAt,
        imageSource = this.movie.imageSource,
        name = this.movie.name,
        isMovie = this.movie.isMovie,
        releaseDate = this.movie.releaseDate,
        popularity = this.movie.popularity,
        voteAverage = this.movie.voteAverage,
        watchNow = this.watchNow.map { it.toServiceItem() },
        buyRent = this.buyRent.map { it.toServiceItem() },
        isToWatch = true,
        isWatched = false
    )

fun WatchedMovieWithServices.toMovieItem(): MovieItem =
    MovieItem(
        id = this.movie.movieId,
        savedAt = this.movie.savedAt,
        imageSource = this.movie.imageSource,
        name = this.movie.name,
        isMovie = this.movie.isMovie,
        releaseDate = this.movie.releaseDate,
        popularity = this.movie.popularity,
        voteAverage = this.movie.voteAverage,
        watchNow = this.watchNow.map { it.toServiceItem() },
        buyRent = this.buyRent.map { it.toServiceItem() },
        isToWatch = false,
        isWatched = true
    )
