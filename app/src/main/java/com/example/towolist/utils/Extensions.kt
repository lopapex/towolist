package com.example.towolist.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun String.getFormattedDateString(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd")
    val formatter = SimpleDateFormat("MMM.dd, yyyy")
    return formatter.format(parser.parse(this) as Date)
}