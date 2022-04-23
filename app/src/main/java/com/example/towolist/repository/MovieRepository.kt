package com.example.towolist.repository

import com.example.towolist.R
import com.example.towolist.data.MovieItem
import com.example.towolist.data.ServiceItem

class MovieRepository {
    private val rootApiImg = "https://image.tmdb.org/t/p/original"

    fun getMockedData(count: Int = 10): List<MovieItem> =
        mutableListOf<MovieItem>().apply {
            repeat(count) { it ->
                val item = MovieItem(
                    id = it.toLong(),
                    name = "The Batman $it",
                    release_date = "2005-06-10",
                    imageSource = "${rootApiImg}/74xTEgt7R36Fpooo50r9T25onhq.jpg",
                    rating = if (it % 2 == 0) R.string.r else R.string.pg,
                    watchNow = if (it % 8 == 0) mutableListOf<ServiceItem>().apply {
                        repeat(3) {
                            val item = ServiceItem(
                                id = it.toLong() * count,
                                name = "Netflix",
                                iconSource = "${rootApiImg}/t2yyOv40HZeVlLjYsCsPHnWLk4W.jpg",
                            )
                            add(item)
                        }
                    } else mutableListOf<ServiceItem>(),
                    buyRent = if (it % 4 == 0) mutableListOf<ServiceItem>().apply {
                        repeat(2) {
                            val item = ServiceItem(
                                id = it.toLong() * count,
                                name = "Google Play Movies",
                                iconSource = "${rootApiImg}/peURlLlr8jggOwK53fJ5wdQl05y.jpg",
                            )
                            add(item)
                        }
                    } else mutableListOf<ServiceItem>(),
                    isToWatch = it%2 == 0,
                    isWatched = it%3 == 0
                )
                add(item)
            }
        }
}