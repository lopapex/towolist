package com.example.towolist.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieItem(
    val id: Long,
    val imageSource: String,
    val name: String,
    val releaseDate: String?,
    val popularity: Float,
    val voteAverage: Float,
    var watchNow: List<ServiceItem>,
    var buyRent: List<ServiceItem>,
    val isToWatch: Boolean,
    val isWatched: Boolean
): Parcelable