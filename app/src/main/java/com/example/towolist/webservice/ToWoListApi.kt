package com.example.towolist.webservice

import com.example.towolist.webservice.response.ListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ToWoListApi {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<ListResponse>

    @GET("tv/latest")
    fun getLatTvShows(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): Call<ListResponse>
}