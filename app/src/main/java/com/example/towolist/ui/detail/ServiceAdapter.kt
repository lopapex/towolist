package com.example.towolist.ui.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.example.towolist.R
import com.example.towolist.data.ServiceItem
import com.example.towolist.databinding.ServiceListBinding
import com.example.towolist.databinding.ServiceListBinding.*


class ServiceAdapter(context: Activity, private val items: List<ServiceItem>)
    : ArrayAdapter<ServiceItem>(context, R.layout.service_list, items) {

    private lateinit var binding: ServiceListBinding

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        binding = inflate(LayoutInflater.from(parent.context), parent, false)


        binding.serviceName.text = items[position].name
        binding.options.text = items[position].name

        Glide.with(parent.context)
            .load(items[position].iconSource)
            .placeholder(R.drawable.empty_image)
            .error(R.drawable.empty_image)
            .fallback(R.drawable.empty_image)
            .into(binding.serviceIcon)

        return binding.root
    }
}