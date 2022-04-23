package com.example.towolist.repository

import com.example.towolist.data.MovieItem
import com.example.towolist.data.ServiceItem
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
        watchNow = mutableListOf<ServiceItem>().apply {
            repeat(3) {
                val item = ServiceItem(
                    id = it.toLong() * 10,
                    name = "Netflix $it",
                    iconSource = "${rootApiImg}/t2yyOv40HZeVlLjYsCsPHnWLk4W.jpg",
                )
                add(item)
            }
        },
        buyRent = mutableListOf<ServiceItem>().apply {
            repeat(2) {
                val item = ServiceItem(
                    id = it.toLong() * 10,
                    name = "Google Play Movies $it",
                    iconSource = "${rootApiImg}/peURlLlr8jggOwK53fJ5wdQl05y.jpg",
                )
                add(item)
            }
        },
        isToWatch = true,
        isWatched = false
    )

fun WatchProviderInfoResponse.toServiceItem(): ServiceItem =
    ServiceItem(
        id = this.providerId.toLong(),
        iconSource = "${rootApiImg}${this.logoPath}",
        name = this.providerName
    )