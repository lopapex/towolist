package com.example.towolist.repository

import com.example.towolist.data.MovieItem
import com.example.towolist.data.ServiceItem
import com.example.towolist.webservice.response.MovieListItem

private val rootApiImg = "https://image.tmdb.org/t/p/original"

fun MovieListItem.toMovieItem(): MovieItem =
    MovieItem(
        id = this.id.toLong(),
        imageSource = "${rootApiImg}${this.posterPath.toString()}",
        name = this.title,
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
        }
    )