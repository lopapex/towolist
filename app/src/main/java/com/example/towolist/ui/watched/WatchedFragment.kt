package com.example.towolist.ui.watched

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.text.SimpleDateFormat
import java.util.*
import com.example.towolist.databinding.FragmentWatchedBinding

class WatchedFragment : Fragment() {

    private lateinit var binding: FragmentWatchedBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWatchedBinding.inflate(layoutInflater, container, false)
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