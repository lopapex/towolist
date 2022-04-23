package com.example.towolist.repository

import android.content.Context
import com.example.towolist.data.MovieItem
import com.example.towolist.data.ServiceItem
import com.example.towolist.webservice.RetrofitUtil
import com.example.towolist.webservice.ToWoListApi
import com.example.towolist.webservice.response.MovieListResponse
import com.example.towolist.webservice.response.TvShowListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository(
    context: Context,
    private val toWoListApi: ToWoListApi = RetrofitUtil.createAqiWebService()
) {
    private val rootApiImg = "https://image.tmdb.org/t/p/original"

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
                        onFailure(IllegalStateException("Response was not successful"))
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
                        onFailure(IllegalStateException("Response was not successful"))
                    }
                }

                override fun onFailure(call: Call<TvShowListResponse>, t: Throwable) {
                    onFailure(t)
                }
            })
    }

    fun getMockedData(count: Int = 10): List<MovieItem> =
        mutableListOf<MovieItem>().apply {
            repeat(count) { it ->
                val item = MovieItem(
                    id = it.toLong(),
                    name = "The Batman $it",
                    imageSource = "${rootApiImg}/74xTEgt7R36Fpooo50r9T25onhq.jpg",
                    watchNow = mutableListOf<ServiceItem>().apply {
                        repeat(3) {
                            val item = ServiceItem(
                                id = it.toLong() * count,
                                name = "Netflix $it",
                                iconSource = "${rootApiImg}/t2yyOv40HZeVlLjYsCsPHnWLk4W.jpg",
                            )
                            add(item)
                        }
                    },
                    buyRent = mutableListOf<ServiceItem>().apply {
                        repeat(2) {
                            val item = ServiceItem(
                                id = it.toLong() * count,
                                name = "Google Play Movies $it",
                                iconSource = "${rootApiImg}/peURlLlr8jggOwK53fJ5wdQl05y.jpg",
                            )
                            add(item)
                        }
                    }
                )
                add(item)
            }
        }

}