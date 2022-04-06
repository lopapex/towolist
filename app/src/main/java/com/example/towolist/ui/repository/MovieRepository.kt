package com.example.towolist.ui.repository

import com.example.towolist.ui.data.MovieItem

class MovieRepository {

    fun getMockedData(count: Int = 10): List<MovieItem> =
        mutableListOf<MovieItem>().apply {
            repeat(count) {
                val item = MovieItem(
                    id = it.toLong(),
                    name = "The Batman $it",
                    imageSource = "https://image.tmdb.org/t/p/w300_and_h450_bestv2/74xTEgt7R36Fpooo50r9T25onhq.jpg",
                )
                add(item)
            }
        }

}