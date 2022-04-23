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
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.bumptech.glide.Glide
import com.example.towolist.R
import com.example.towolist.data.MovieItem
import com.example.towolist.data.ServiceInfo
import com.example.towolist.databinding.FragmentDetailMovieBinding
import com.example.towolist.databinding.ServiceListBinding
import com.example.towolist.utils.getFormattedDateString
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DetailMovieFragment() : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDetailMovieBinding
    private var services : Map<String, ServiceInfo> = mapOf(
        "Netflix" to ServiceInfo.Netflix,
        "HBO MAX" to ServiceInfo.HBO
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = DetailMovieFragmentArgs.fromBundle(requireArguments()).item

        binding.mediaName.text = item.name

        Glide.with(view.context)
            .load(item.imageSource)
            .placeholder(R.drawable.empty_image)
            .error(R.drawable.empty_image)
            .fallback(R.drawable.empty_image)
            .into(binding.poster)

        updateImageButtonColor(binding.watchedIcon, item.isWatched, view)
        updateImageButtonColor(binding.toWatchIcon, item.isToWatch, view)

        updateBasedOnServices(item)
    }

    private fun updateImageButtonColor(btn: ImageButton, isActive: Boolean, view: View) {
        val color = if (isActive) R.color.secondaryLight else R.color.secondary
        btn.imageTintList = ContextCompat.getColorStateList(view.context, color)
    }

    private fun updateBasedOnServices(item: MovieItem) {
        if (item.watchNow.isNotEmpty()) {
            val watchNowListAdapter = ServiceAdapter(context as Activity, item.watchNow)
            binding.watchNowList.onItemClickListener = OnItemClickListener { parent, v, position, id ->
                val service = services[watchNowListAdapter.items[position].name]
                try {
                    val intent = service?.packageName?.let {
                        v.context.packageManager.getLaunchIntentForPackage(
                            it
                        )
                    }
                    startActivity(intent)
                } catch (e: Exception) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(service?.url)
                    startActivity(intent)
                }
            }
            binding.watchNowList.adapter = watchNowListAdapter
        } else {
            binding.watchNow.visibility = View.GONE
        }

        if (item.buyRent.isNotEmpty()) {
            val rentBuyListAdapter = ServiceAdapter(context as Activity, item.buyRent)
            binding.rentBuyList.adapter = rentBuyListAdapter
        } else {
            binding.rentBuy.visibility = View.GONE
        }

        if (item.buyRent.isEmpty() && item.watchNow.isEmpty()) {
            binding.theater.visibility = View.VISIBLE
            binding.theaterText.text = item.release_date.getFormattedDateString()
            binding.theaterText.visibility = View.VISIBLE

            binding.calendarIcon.visibility = View.VISIBLE
        }
    }
}