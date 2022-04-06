package com.example.towolist.ui.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieItem(
    val id: Long,
    val imageSource: String,
    val name: String
): Parcelable