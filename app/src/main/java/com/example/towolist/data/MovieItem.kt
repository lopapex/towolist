package com.example.towolist.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieItem(
    val id: Long,
    val imageSource: String,
    val name: String,
    val isMovie : Boolean,
    val releaseDate: String?,
    val popularity: Float,
    val voteAverage: Float,
    var watchNow: List<ServiceItem>,
    var buyRent: List<ServiceItem>,
    var isToWatch: Boolean,
    var isWatched: Boolean
): Parcelable