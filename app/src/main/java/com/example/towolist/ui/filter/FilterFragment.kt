package com.example.towolist.ui.filter

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.towolist.data.MovieItem
import com.example.towolist.databinding.FragmentFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.logging.Logger

class FilterFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chipListener(binding.chipAll, listOf(binding.chipMovies, binding.chipSeries))
        chipListener(binding.chipMovies, listOf(binding.chipAll, binding.chipSeries))
        chipListener(binding.chipSeries, listOf(binding.chipMovies, binding.chipAll))

        chipGroupListener(binding.chipWatchGroup)
        chipGroupListener(binding.chipBuyrentGroup)

        binding.filterResetButton.setOnClickListener {
            setDefaultState()
        }

        binding.filterConfirmButton.setOnClickListener {
            setFragmentResult("filterFragment", bundleOf(Pair("predicate", predicate())))
        }

        binding.voteAverageSlider.isTickVisible = false
    }

    private fun chipListener(clicked: Chip, toUncheck: List<Chip>) {
        clicked.setOnClickListener {
            clicked.isChecked = true
            toUncheck.forEach {
                it.isChecked = false
            }
        }
    }

    private fun chipGroupListener(clicked: ChipGroup) {
        clicked.setOnClickListener {
            clicked.clearCheck()
        }
    }

    private fun setDefaultState() {
        binding.chipTypeGroup.clearCheck()
        binding.chipAll.isChecked = true

        listOf(
            binding.chipWatchAmazon,
            binding.chipWatchHbo,
            binding.chipWatchNetflix,
        ).forEach {
            it.isChecked = true
        }

        listOf(
            binding.chipBuyrentApple,
            binding.chipBuyrentGoogle
        ).forEach {
            it.isChecked = true
        }

        binding.voteAverageSlider.setValues(0F, 10F)
    }

    private fun predicate(): (MovieItem) -> Boolean {
        return {item: MovieItem ->
            val voteFrom = binding.voteAverageSlider.valueFrom
            val voteTo = binding.voteAverageSlider.valueTo

            var result = true

            if (item.voteAverage < voteFrom || voteTo < item.voteAverage) {
                result = false
                Log.i("FILTER", "vote average out of scope!")
            }

            if (!binding.chipAll.isChecked) {
                if (binding.chipMovies.isChecked && !item.isMovie) {
                    result = false
                    Log.i("FILTER", "not a movie!")
                }

                if (binding.chipSeries.isChecked && item.isMovie) {
                    result = false
                    Log.i("FILTER", "not a series!")
                }
            }

            val watchSelection = retrieveWatchOptions()

            if (item.watchNow
                    .map { it.name }
                    .intersect(listOf(watchSelection))
                    .isNotEmpty()
            ) {
                Log.i("FILTER", "watch selection out of scope!")
                result = false
            }

            val rentBuySelection = retrieveRentBuyOptions()

            if (item.buyRent
                    .map { it.name }
                    .intersect(listOf(rentBuySelection))
                    .isNotEmpty()
            ) {
                Log.i("FILTER", "buy rent selection out of scope!")
                result = false
            }

            result
        }
    }

    private fun retrieveWatchOptions(): MutableList<String> {
        val watchSelection = mutableListOf<String>()

        if (binding.chipWatchNetflix.isChecked)
            watchSelection.add("Netflix")

        if (binding.chipWatchAmazon.isChecked)
            watchSelection.add("Amazon Prime Video")

        if (binding.chipWatchHbo.isChecked)
            watchSelection.add("HBO Max")

        return watchSelection
    }

    private fun retrieveRentBuyOptions(): MutableList<String> {
        val watchSelection = mutableListOf<String>()

        if (binding.chipBuyrentApple.isChecked)
            watchSelection.add("Apple iTunes")

        if (binding.chipBuyrentGoogle.isChecked)
            watchSelection.add("Google Play Movies")

        return watchSelection
    }
}