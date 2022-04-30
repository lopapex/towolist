package com.example.towolist.ui.filter

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.towolist.data.MovieItem
import com.example.towolist.databinding.FragmentFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


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

    // This is far initial state after application start
    companion object {
        var moviesChecked = false
        var seriesChecked = false
        var allChecked = true

        var firstOpen = true

        var watchCheckStates = mutableListOf<Boolean>()
        var buyRentCheckStates = mutableListOf<Boolean>()

        var voteFrom = 0F
        var voteTo = 10F
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (firstOpen) {
            initChipStatesInCompanionObject()
            firstOpen = false
        }

        restoreState()

        chipListener(binding.chipAll, listOf(binding.chipMovies, binding.chipSeries))
        chipListener(binding.chipMovies, listOf(binding.chipAll, binding.chipSeries))
        chipListener(binding.chipSeries, listOf(binding.chipMovies, binding.chipAll))

        chipGroupListener(binding.chipWatchGroup)
        chipGroupListener(binding.chipBuyrentGroup)

        binding.filterResetButton.setOnClickListener {
            setDefaultState()
        }

        binding.filterConfirmButton.setOnClickListener {
            saveState()
            setFragmentResult("filterFragment", bundleOf(Pair("predicate", predicate())))
            dismiss()
        }

        binding.voteAverageSlider.isTickVisible = false
    }

    private fun restoreState() {
        binding.chipMovies.isChecked = moviesChecked
        binding.chipSeries.isChecked = seriesChecked
        binding.chipAll.isChecked = allChecked

        for (i in 0 until binding.chipWatchGroup.childCount) {
            val chip = binding.chipWatchGroup.getChildAt(i) as Chip
            chip.isChecked = watchCheckStates[i]
        }

        for (i in 0 until binding.chipBuyrentGroup.childCount) {
            val chip = binding.chipBuyrentGroup.getChildAt(i) as Chip
            chip.isChecked = buyRentCheckStates[i]
        }

        binding.voteAverageSlider.setValues(voteFrom, voteTo)
    }

    private fun saveState() {
        moviesChecked = binding.chipMovies.isChecked
        seriesChecked = binding.chipSeries.isChecked
        allChecked = binding.chipAll.isChecked

        for (i in 0 until binding.chipWatchGroup.childCount) {
            val chip = binding.chipWatchGroup.getChildAt(i) as Chip
            watchCheckStates[i] = chip.isChecked
        }

        for (i in 0 until binding.chipBuyrentGroup.childCount) {
            val chip = binding.chipBuyrentGroup.getChildAt(i) as Chip
            buyRentCheckStates[i] = chip.isChecked
        }

        voteFrom = binding.voteAverageSlider.values[0]
        voteTo = binding.voteAverageSlider.values[1]
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

        for (i in 0 until binding.chipWatchGroup.childCount) {
            val chip = binding.chipWatchGroup.getChildAt(i) as Chip
            chip.isChecked = true
        }

        for (i in 0 until binding.chipBuyrentGroup.childCount) {
            val chip = binding.chipBuyrentGroup.getChildAt(i) as Chip
            chip.isChecked = true
        }

        binding.voteAverageSlider.setValues(0F, 10F)
    }

    private fun predicate(): (MovieItem) -> Boolean {
        return {item: MovieItem ->
            var result = true

            if (item.voteAverage < voteFrom || voteTo < item.voteAverage) {
                result = false
            }

            if (!allChecked) {
                if (moviesChecked && !item.isMovie) {
                    result = false
                }

                if (seriesChecked && item.isMovie) {
                    result = false
                }
            }

            val selectedServicesNames = retrieveChipGroupOptions(binding.chipWatchGroup)
            /* replace this for
             * TODO item.watchNow.map { it.name }
             * when there will be present service items in watchNow attribute of MovieItem
             */
            var itemServicesNames = listOf("Netflix", "HBO Max", "Amazon Prime Video")

            if (!hasAnyCommonOptions(itemServicesNames, selectedServicesNames)) {
                result = false
            }

            val rentBuySelection = retrieveChipGroupOptions(binding.chipBuyrentGroup)
            /* replace this for
             * TODO item.buyRent.map { it.name }
             * when there will be present service items in buyRent attribute of MovieItem
             */
            itemServicesNames = listOf("Apple iTunes", "Google Play Movies")

            if (!hasAnyCommonOptions(itemServicesNames, rentBuySelection)) {
                result = false
            }

            result
        }
    }

    private fun retrieveChipGroupOptions(chipGroup: ChipGroup): MutableList<String> {
        val selection = mutableListOf<String>()

        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                selection.add(chip.text.toString())
            }
        }

        return selection
    }

    private fun hasAnyCommonOptions(
        itemServicesNames : List<String>,
        selectedServicesNames : List<String>
    ): Boolean {
        for (serviceNameFromApi in itemServicesNames) {
            // Since service names in filter are not equals with those returned from Movie DB API, we use contains method
            if (selectedServicesNames.map { it.lowercase() }.any {
                    serviceNameFromApi.lowercase().contains(it)
            })
                return true
        }

        return false
    }

    private fun initChipStatesInCompanionObject() {
        for (i in 0 until binding.chipWatchGroup.childCount) {
            watchCheckStates.add(true)
        }

        for (i in 0 until binding.chipBuyrentGroup.childCount) {
            buyRentCheckStates.add(true)
        }
    }
}