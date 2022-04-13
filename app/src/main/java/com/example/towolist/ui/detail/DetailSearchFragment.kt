package com.example.towolist.ui.detail

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.towolist.R
import com.example.towolist.databinding.FragmentDetailMovieBinding
import com.example.towolist.databinding.FragmentDetailSearchBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailSearchFragment() : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDetailSearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}