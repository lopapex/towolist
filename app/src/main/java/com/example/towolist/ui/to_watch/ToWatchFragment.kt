package com.example.towolist.ui.to_watch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.towolist.R
import com.example.towolist.databinding.FragmentListBinding
import com.example.towolist.databinding.FragmentToWatchBinding
import com.example.towolist.databinding.FragmentWatchedBinding
import java.text.SimpleDateFormat
import java.util.*

class ToWatchFragment : Fragment() {

    private lateinit var binding: FragmentToWatchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentToWatchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.timeButton.setOnClickListener {
            binding.timeTextView.text = giveMeTime(binding.timeTextView.text.toString())
        }
    }

    private fun giveMeTime(previousText: String): String {
        val df = SimpleDateFormat("dd.mm.YYYY")

        val timeLong = System.currentTimeMillis()
        val date = Date(timeLong)

        return "$previousText ${df.format(date)},"
    }
}