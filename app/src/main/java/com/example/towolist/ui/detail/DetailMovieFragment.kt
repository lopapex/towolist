package com.example.towolist.ui.detail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageButton
import android.widget.ListView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.bumptech.glide.Glide
import com.example.towolist.R
import com.example.towolist.data.MovieItem
import com.example.towolist.data.ServiceItem
import com.example.towolist.data.services
import com.example.towolist.databinding.FragmentDetailMovieBinding
import com.example.towolist.repository.MovieRepository
import com.example.towolist.ui.list.ListFragment
import com.example.towolist.utils.getFormattedDateString
import com.example.towolist.utils.toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailMovieFragment() : BottomSheetDialogFragment() {

    private val movieRepository: MovieRepository by lazy {
        MovieRepository(requireContext())
    }

    private lateinit var binding: FragmentDetailMovieBinding
    private lateinit var item: MovieItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        item = DetailMovieFragmentArgs.fromBundle(requireArguments()).item

        binding.mediaName.text = item.name

        Glide.with(view.context)
            .load(item.imageSource)
            .placeholder(R.drawable.empty_image)
            .error(R.drawable.empty_image)
            .fallback(R.drawable.empty_image)
            .into(binding.poster)

        updateImageButtonColor(binding.toWatchIcon, movieRepository.getToWatchByMovieId(item.id), view)
        updateImageButtonColor(binding.watchedIcon, movieRepository.getWatchedByMovieId(item.id), view)
        val fragment = parentFragmentManager.findFragmentById(R.id.nav_host_fragment)

        toWatchOnClick(fragment, view)
        watchedOnClick(fragment, view)

        updateBasedOnServices(item)
    }

    private fun updateImageButtonColor(btn: ImageButton, isActive: Boolean, view: View) {
        val color = if (isActive) R.color.secondary else R.color.secondaryLight
        btn.imageTintList = ContextCompat.getColorStateList(view.context, color)
    }

    private fun updateBasedOnServices(item: MovieItem) {
        if (item.watchNow.isNotEmpty()) {
            val watchNowListAdapter = ServiceAdapter(context as Activity, item.watchNow)
            onClickOpen(watchNowListAdapter.items, binding.watchNowList)
            binding.watchNowList.adapter = watchNowListAdapter
        } else {
            binding.watchNow.visibility = View.GONE
        }

        if (item.buyRent.isNotEmpty()) {
            val rentBuyListAdapter = ServiceAdapter(context as Activity, item.buyRent)
            onClickOpen(rentBuyListAdapter.items, binding.rentBuyList)
            binding.rentBuyList.adapter = rentBuyListAdapter
        } else {
            binding.rentBuy.visibility = View.GONE
        }

        if (item.buyRent.isEmpty() && item.watchNow.isEmpty()) {
            binding.theater.visibility = View.VISIBLE
            binding.theaterText.text =
                if (item.releaseDate != null && item.releaseDate.isNotEmpty())
                    item.releaseDate.getFormattedDateString()
                else
                    context?.getString(R.string.unknown)

            binding.theaterText.visibility = View.VISIBLE
            binding.calendarIcon.visibility = View.VISIBLE
        }
    }

    private fun watchedOnClick(
        fragment: Fragment?,
        view: View
    ) {
        binding.watchedIcon.setOnClickListener {
            setFragmentResult("updateWatched", bundleOf(Pair("item", item)))
            item = item.copy(isWatched = !item.isWatched)
            if (item.isWatched && item.isToWatch) {
                setFragmentResult("updateToWatch", bundleOf(Pair("item", item)))
                item = item.copy(isToWatch = false)

                movieRepository.updateToWatchMovies(item)
                updateImageButtonColor(binding.toWatchIcon, item.isToWatch, view)
                if (fragment !is ListFragment) {
                    dismiss()
                }
            }

            if (!item.isWatched && fragment !is ListFragment) {
                dismiss()
            }
            movieRepository.updateWatchedMovies(item)
            updateImageButtonColor(binding.watchedIcon, item.isWatched, view)
        }
    }

    private fun toWatchOnClick(
        fragment: Fragment?,
        view: View
    ) {
        binding.toWatchIcon.setOnClickListener {
            setFragmentResult("updateToWatch", bundleOf(Pair("item", item)))
            item = item.copy(isToWatch = !item.isToWatch)
            if (item.isWatched && item.isToWatch) {

                setFragmentResult("updateWatched", bundleOf(Pair("item", item)))
                item = item.copy(isWatched = false)

                movieRepository.updateWatchedMovies(item)
                updateImageButtonColor(binding.watchedIcon, item.isWatched, view)
                if (fragment !is ListFragment) {
                    dismiss()
                }
            }

            if (!item.isToWatch && fragment !is ListFragment) {
                dismiss()
            }
            movieRepository.updateToWatchMovies(item)
            updateImageButtonColor(binding.toWatchIcon, item.isToWatch, view)
        }
    }

    private fun onClickOpen(items: List<ServiceItem>, list: ListView) {
        list.onItemClickListener = OnItemClickListener { _, v, position, _ ->
            val service = services[items[position].name]
            if (service != null) {
                try {
                    val intent =
                        v.context.packageManager.getLaunchIntentForPackage(service.packageName)
                    startActivity(intent)
                } catch (e: Exception) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(service.url)
                    startActivity(intent)
                }
            }
        }
    }
}