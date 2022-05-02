package com.example.towolist.repository

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import com.example.towolist.R
import com.example.towolist.data.MovieItem
import com.example.towolist.database.ToWoDatabase
import com.example.towolist.database.dao.*
import com.example.towolist.database.entity.ToWatchMovieBuyRentCrossRefEntity
import com.example.towolist.database.entity.ToWatchMovieWatchNowCrossRefEntity
import com.example.towolist.database.entity.WatchedMovieBuyRentCrossRefEntity
import com.example.towolist.database.entity.WatchedMovieWatchNowCrossRefEntity
import com.example.towolist.utils.toast
import com.example.towolist.webservice.RetrofitUtil
import com.example.towolist.webservice.ToWoListApi
import com.example.towolist.webservice.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository(
    private val context: Context,
    private val database: ToWoDatabase = ToWoDatabase.create(context),
    private val toWatchMovieDao: ToWatchMovieDao = database.toWatchMovieDao(),
    private val watchedMovieDao: WatchedMovieDao = database.watchedMovieDao(),
    private val watchNowDao: WatchNowDao = database.watchNowDao(),
    private val buyRentDao: BuyRentDao = database.buyRentDao(),
    private val toWatchMovieWatchNowCrossRefDao: ToWatchMovieWatchNowCrossRefDao = database.toWatchMovieWatchNowCrossRefDao(),
    private val toWatchMovieBuyRentCrossRefDao: ToWatchMovieBuyRentCrossRefDao = database.toWatchMovieBuyRentCrossRefDao(),
    private val watchedMovieWatchNowCrossRefDao: WatchedMovieWatchNowCrossRefDao = database.watchedMovieWatchNowCrossRefDao(),
    private val watchedMovieBuyRentCrossRefDao: WatchedMovieBuyRentCrossRefDao = database.watchedMovieBuyRentCrossRentDao(),
    private val toWoListApi: ToWoListApi = RetrofitUtil.createAqiWebService()
) {
    private val apiKey = "7d983af93fb311150ed909fbc0873210"
    private val language = "en-US"

    fun getPopularMovies(page: Int, onSuccess: (List<MovieItem>) -> Unit, onFailure: (Throwable) -> Unit) {
        toWoListApi.getPopularMovies(apiKey, language, page)
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

    fun getPopularTvShows(page: Int, onSuccess: (List<MovieItem>) -> Unit, onFailure: (Throwable) -> Unit) {
        toWoListApi.getPopularTvShows(apiKey, language, page)
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

    fun getTopRatedMovies(page: Int, onSuccess: (List<MovieItem>) -> Unit, onFailure: (Throwable) -> Unit) {
        toWoListApi.getTopRatedMovies(apiKey, language, page)
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

    fun getTopRatedTvShows(page: Int, onSuccess: (List<MovieItem>) -> Unit, onFailure: (Throwable) -> Unit) {
        toWoListApi.getTopRatedTvShows(apiKey, language, page)
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

    fun searchMovies(page: Int, query: String, onSuccess: (List<MovieItem>) -> Unit, onFailure: (Throwable) -> Unit) {
        toWoListApi.searchMovies(apiKey, language, query, page)
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

    fun searchTvShows(page: Int, query: String, onSuccess: (List<MovieItem>) -> Unit, onFailure: (Throwable) -> Unit) {
        toWoListApi.searchTvShows(apiKey, language, query, page)
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
        toWatchMovieDao.getAll()
            .map { entity ->
                entity.toMovieItem()
            }

    fun updateToWatchMovies(item: MovieItem) {
        if (item.isToWatch) {
            toWatchMovieDao.saveEntity(item.toToWatchMovieEntity()!!)
            toWatchSaveProviders(item)
        } else {
            toWatchMovieDao.deleteById(item.id)
            toWatchMovieWatchNowCrossRefDao.deleteByMovieId(item.id)
            toWatchMovieBuyRentCrossRefDao.deleteByMovieId(item.id)
        }
    }

    fun getWatchedMovies(): List<MovieItem> =
        watchedMovieDao.getAll()
            .map { entity ->
                entity.toMovieItem()
            }

    fun updateWatchedMovies(item: MovieItem) {
        if (item.isWatched) {
            watchedMovieDao.saveEntity(item.toWatchedMovieEntity()!!)
            watchedSaveProviders(item)
        } else {
            watchedMovieDao.deleteById(item.id)
            watchedMovieWatchNowCrossRefDao.deleteByMovieId(item.id)
            watchedMovieBuyRentCrossRefDao.deleteByMovieId(item.id)
        }
    }

    fun getToWatchByMovieId(id: Long): Boolean {
        val movie = toWatchMovieDao.getById(id)
        return movie != null
    }

    fun getWatchedByMovieId(id: Long): Boolean {
        val movie = watchedMovieDao.getById(id)
        return movie != null
    }

    private fun toWatchSaveProviders(item: MovieItem) {
        item.watchNow.forEach {
            val watchNowEntity = watchNowDao.getById(it.id)

            if (watchNowEntity == null) {
                watchNowDao.saveEntity(it.toWatchNowEntity())
            }
            toWatchMovieWatchNowCrossRefDao.saveEntity(ToWatchMovieWatchNowCrossRefEntity(item.id, it.id))
        }

        item.buyRent.forEach {
            val buyRentEntity = buyRentDao.getById(it.id)

            if (buyRentEntity == null) {
                buyRentDao.saveEntity(it.toBuyRentEntity())
            }
            toWatchMovieBuyRentCrossRefDao.saveEntity(ToWatchMovieBuyRentCrossRefEntity(item.id, it.id))
        }
    }

    private fun watchedSaveProviders(item: MovieItem) {
        item.watchNow.forEach {
            val watchNowEntity = watchNowDao.getById(it.id)

            if (watchNowEntity == null) {
                watchNowDao.saveEntity(it.toWatchNowEntity())
            }
            watchedMovieWatchNowCrossRefDao.saveEntity(WatchedMovieWatchNowCrossRefEntity(item.id, it.id))
        }

        item.buyRent.forEach {
            val buyRentEntity = buyRentDao.getById(it.id)

            if (buyRentEntity == null) {
                buyRentDao.saveEntity(it.toBuyRentEntity())
            }
            watchedMovieBuyRentCrossRefDao.saveEntity(WatchedMovieBuyRentCrossRefEntity(item.id, it.id))
        }
    }
}