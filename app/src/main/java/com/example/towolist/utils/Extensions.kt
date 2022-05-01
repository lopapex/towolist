package com.example.towolist.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun String.getFormattedDateString(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd")
    val formatter = SimpleDateFormat("MMM.dd, yyyy")
    return formatter.format(parser.parse(this) as Date)
}

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun ChipGroup.addChip(context: Context, label: String){
    Chip(context).apply {
        id = View.generateViewId()
        text = label
        isChecked = true
        isClickable = true
        isCheckable = true
        isCheckedIconVisible = false
        isFocusable = true
        addView(this)
    }
}