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
                    imageSource = "${rootApiImg}/74xTEgt7R36Fpooo50r9T25onhq.jpg",
                    flatRate = mutableListOf<ServiceItem>().apply {
                        repeat(3) {
                            val item = ServiceItem(
                                id = it.toLong() * count,
                                name = "Netflix $it",
                                iconSource = "${rootApiImg}/t2yyOv40HZeVlLjYsCsPHnWLk4W.jpg",
                            )
                            add(item)
                        }
                    }
                )
                add(item)
            }
        }

}