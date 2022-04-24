package com.example.towolist.repository

import android.content.Context
import com.example.towolist.R
import com.example.towolist.data.MovieItem
import com.example.towolist.data.ServiceItem
import com.example.towolist.database.ToWoDatabase
import com.example.towolist.database.dao.ToWatchMovieDao
import com.example.towolist.database.dao.WatchedMovieDao
import com.example.towolist.database.entity.ToWatchMovieEntity
import com.example.towolist.webservice.RetrofitUtil
import com.example.towolist.webservice.ToWoListApi
import com.example.towolist.webservice.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository(
    context: Context,
    private val database: ToWoDatabase = ToWoDatabase.create(context),
    private val toWatchedMovieDao: ToWatchMovieDao = database.toWatchMovieDao(),
    private val watchedMovieDao: WatchedMovieDao = database.watchedMovieDao(),
    private val toWoListApi: ToWoListApi = RetrofitUtil.createAqiWebService()
) {
    private val apiKey = "7d983af93fb311150ed909fbc0873210"
    private val language = "en-US"

    fun getPopularMovies(onSuccess: (List<MovieItem>) -> Unit, onFailure: (Throwable) -> Unit) {
        toWoListApi.getPopularMovies(apiKey, language, 1)
            .enqueue(object : Callback<MovieListResponse> {

                override fun onResponse(call: Call<MovieListResponse>, response: Response<MovieListResponse>) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        onSuccess(responseBody.results.map { movieListItem ->
                            movieListItem.toMovieItem()
                        })
                    } else {
                        onFailure(IllegalStateException(R.string.response_error.toString()))
                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    onFailure(t)
                }
            })
    }

    fun getPopularTvShows(onSuccess: (List<MovieItem>) -> Unit, onFailure: (Throwable) -> Unit) {
        toWoListApi.getPopularTvShows(apiKey, language, 1)
            .enqueue(object : Callback<TvShowListResponse> {

                override fun onResponse(call: Call<TvShowListResponse>, response: Response<TvShowListResponse>) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        onSuccess(responseBody.results.map { tvShowListItem ->
                            tvShowListItem.toMovieItem()
                        })
                    } else {
                        onFailure(IllegalStateException(R.string.response_error.toString()))
                    }
                }

                override fun onFailure(call: Call<TvShowListResponse>, t: Throwable) {
                    onFailure(t)
                }
            })
    }

    fun getTopRatedMovies(onSuccess: (List<MovieItem>) -> Unit, onFailure: (Throwable) -> Unit) {
        toWoListApi.getTopRatedMovies(apiKey, language, 1)
            .enqueue(object : Callback<MovieListResponse> {

                override fun onResponse(call: Call<MovieListResponse>, response: Response<MovieListResponse>) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        onSuccess(responseBody.results.map { movieListItem ->
                            movieListItem.toMovieItem()
                        })
                    } else {
                        onFailure(IllegalStateException(R.string.response_error.toString()))
                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    onFailure(t)
                }
            })
    }

    fun getTopRatedTvShows(onSuccess: (List<MovieItem>) -> Unit, onFailure: (Throwable) -> Unit) {
        toWoListApi.getTopRatedTvShows(apiKey, language, 1)
            .enqueue(object : Callback<TvShowListResponse> {

                override fun onResponse(call: Call<TvShowListResponse>, response: Response<TvShowListResponse>) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        onSuccess(responseBody.results.map { tvShowListItem ->
                            tvShowListItem.toMovieItem()
                        })
                    } else {
                        onFailure(IllegalStateException(R.string.response_error.toString()))
                    }
                }

                override fun onFailure(call: Call<TvShowListResponse>, t: Throwable) {
                    onFailure(t)
                }
            })
    }

    fun getWatchProvidersByMovieId(movieId: Long, onSuccess: (WatchProviderByStateResponse?) -> Unit, onFailure: (Throwable) -> Unit) {
        toWoListApi.getWatchProvidersByMovieId(movieId, apiKey)
            .enqueue(object : Callback<WatchProviderListResponse> {

                override fun onResponse(call: Call<WatchProviderListResponse>, response: Response<WatchProviderListResponse>) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        onSuccess(responseBody.results.CZ)
                    } else {
                        onFailure(IllegalStateException(R.string.response_error.toString()))
                    }
                }

                override fun onFailure(call: Call<WatchProviderListResponse>, t: Throwable) {
                    onFailure(t)
                }
            })
    }

    fun getWatchProvidersByTvId(tvId: Long, onSuccess: (WatchProviderByStateResponse?) -> Unit, onFailure: (Throwable) -> Unit) {
        toWoListApi.getWatchProvidersByTvShowId(tvId, apiKey)
            .enqueue(object : Callback<WatchProviderListResponse> {

                override fun onResponse(call: Call<WatchProviderListResponse>, response: Response<WatchProviderListResponse>) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        onSuccess(responseBody.results.CZ)
                    } else {
                        onFailure(IllegalStateException(R.string.response_error.toString()))
                    }
                }

                override fun onFailure(call: Call<WatchProviderListResponse>, t: Throwable) {
                    onFailure(t)
                }
            })
    }

    fun searchMovies(query: String, onSuccess: (List<MovieItem>) -> Unit, onFailure: (Throwable) -> Unit) {
        toWoListApi.searchMovies(apiKey, language, query, 1)
            .enqueue(object : Callback<MovieListResponse> {

                override fun onResponse(call: Call<MovieListResponse>, response: Response<MovieListResponse>) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        onSuccess(responseBody.results.map { movieListItem ->
                            movieListItem.toMovieItem()
                        })
                    } else {
                        onFailure(IllegalStateException(R.string.response_error.toString()))
                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    onFailure(t)
                }
            })
    }

    fun searchTvShows(query: String, onSuccess: (List<MovieItem>) -> Unit, onFailure: (Throwable) -> Unit) {
        toWoListApi.searchTvShows(apiKey, language, query, 1)
            .enqueue(object : Callback<TvShowListResponse> {

                override fun onResponse(call: Call<TvShowListResponse>, response: Response<TvShowListResponse>) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        onSuccess(responseBody.results.map { tvShowListItem ->
                            tvShowListItem.toMovieItem()
                        })
                    } else {
                        onFailure(IllegalStateException(R.string.response_error.toString()))
                    }
                }

                override fun onFailure(call: Call<TvShowListResponse>, t: Throwable) {
                    onFailure(t)
                }
            })
    }

    fun getToWatchMovies(): List<MovieItem> =
        toWatchedMovieDao.getAll()
            .map { entity ->
                entity.toMovieItem()
            }

    fun getWatchedMovies(): List<MovieItem> =
        watchedMovieDao.getAll()
            .map { entity ->
                entity.toMovieItem()
            }
}