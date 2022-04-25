package com.example.towolist.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.towolist.databinding.FragmentFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class FilterFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFilterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFilterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chipListener(binding.chipAll, listOf(binding.chipMovies, binding.chipSeries))
        chipListener(binding.chipMovies, listOf(binding.chipAll, binding.chipSeries))
        chipListener(binding.chipSeries, listOf(binding.chipMovies, binding.chipAll))

        chipListener(binding.chipAny, listOf(binding.chipPg, binding.chipR))
        chipListener(binding.chipPg, listOf(binding.chipAny, binding.chipR))
        chipListener(binding.chipR, listOf(binding.chipPg, binding.chipAny))
    }

    private fun chipListener(clicked: Chip, disabled: List<Chip>) {
        clicked.setOnClickListener {
            clicked.isChecked = true
            disabled.forEach { chip ->
                chip.isClickable = false
            }
        }
    }
}