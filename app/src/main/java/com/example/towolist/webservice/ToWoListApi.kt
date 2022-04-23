package com.example.towolist.webservice

import com.example.towolist.webservice.response.MovieListResponse
import com.example.towolist.webservice.response.TvShowListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ToWoListApi {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MovieListResponse>

    @GET("tv/popular")
    fun getPopularTvShows(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TvShowListResponse>
}