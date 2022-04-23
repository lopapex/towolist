package com.example.towolist.webservice

import com.example.towolist.webservice.response.MovieListResponse
import com.example.towolist.webservice.response.TvShowListResponse
import com.example.towolist.webservice.response.WatchProviderListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MovieListResponse>

    @GET("tv/top_rated")
    fun getTopRatedTvShows(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TvShowListResponse>

    @GET("movie/{movie_id}/watch/providers")
    fun getWatchProvidersByMovieId(
        @Path("movie_id") movieId: Long,
        @Query("api_key") apiKey: String
    ): Call<WatchProviderListResponse>

    @GET("tv/{tv_id}/watch/providers")
    fun getWatchProvidersByTvShowId(
        @Path("tv_id") tvId: Long,
        @Query("api_key") apiKey: String
    ): Call<WatchProviderListResponse>
}