package com.example.towolist.ui.filter

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        Toast.makeText(context, "Dialog ending", Toast.LENGTH_LONG).show()
    }
}