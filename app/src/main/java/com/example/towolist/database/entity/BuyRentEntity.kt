package com.example.towolist.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BuyRentEntity(
    @PrimaryKey
    val serviceId: Long,
    val iconSource: String,
    val name: String
)
