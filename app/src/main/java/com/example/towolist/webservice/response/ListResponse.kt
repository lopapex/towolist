package com.example.towolist.webservice.response

import com.squareup.moshi.Json

data class ListResponse(
    val page: Int,
    val results: List<MovieListItem>,
    @field:Json(name = "total_results")
    val totalResults: Int,
    @field:Json(name = "total_pages")
    val totalPages: Int
)

data class MovieListItem(
    @field:Json(name = "poster_path")
    val posterPath: String?,
    val adult: Boolean,
    val overview: String,
    @field:Json(name = "release_date")
    val releaseDate: String,
    @field:Json(name = "genre_ids")
    val genreIds: List<Int>,
    val id: Int,
    @field:Json(name = "original_title")
    val originalTitle: String,
    @field:Json(name = "original_language")
    val originalLanguage: String,
    val title: String,
    @field:Json(name = "backdrop_path")
    val backdropPath: String?,
    val popularity: Float,
    @field:Json(name = "vote_count")
    val voteCount: Int,
    val video: Boolean,
    @field:Json(name = "vote_average")
    val voteAverage: Float
)