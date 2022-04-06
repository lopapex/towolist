package com.example.towolist.ui.list


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.towolist.databinding.FragmentListBinding
import java.text.SimpleDateFormat
import java.util.*

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(view.context)
            .load("https://image.tmdb.org/t/p/w300_and_h450_bestv2/74xTEgt7R36Fpooo50r9T25onhq.jpg")
            .into(binding.imageView)

    }
}