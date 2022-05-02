package com.example.towolist.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WatchNowEntity (
    @PrimaryKey
    val serviceId: Long,
    val iconSource: String,
    val name: String
)