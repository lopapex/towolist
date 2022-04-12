package com.example.towolist.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceItem(
    val id: Long,
    val iconSource: String,
    val name: String
): Parcelable