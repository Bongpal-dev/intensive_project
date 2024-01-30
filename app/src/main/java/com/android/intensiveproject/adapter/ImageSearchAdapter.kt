package com.android.intensiveproject.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.android.intensiveproject.R
import com.android.intensiveproject.databinding.ItemSerchRecyclerBinding
import com.android.intensiveproject.model.retrofit.ImageItemDetail
import com.android.intensiveproject.model.PreferenceRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ImageSearchAdapter(val context: Context) :
    ListAdapter<ImageItemDetail, ViewHolder>(diffUtil) {

    inner class SearchItemViewHolder(binding: ItemSerchRecyclerBinding) : ViewHolder(binding.root) {
        val image = binding.ivSearchImage
        val time = binding.tvPostingTime
        val site = binding.tvPostingSite
        val size = binding.tvImageSize
        val btnFavorite = binding.btnFavorite
        val ivFavorite = binding.ivFavorite
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val imageItem = ItemSerchRecyclerBinding.inflate(inflater, parent, false)

        return SearchItemViewHolder(imageItem)
    }

    var onClick: ClickFavoriteListener? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val postTime = LocalDateTime.parse(item.datetime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
        val favoriteList = PreferenceRepository(context).getAll()

        with(holder as SearchItemViewHolder) {
            image.load(item.imageUrl)
            site.text = if (item.siteName.isNotEmpty()) "[${item.siteName}]" else "[알 수 없음]"
            size.text = "${item.width} x ${item.height}"
            time.text = "${postTime.format(dateTimeFormatter)}"
            ivFavorite.load(if (favoriteList.contains(item)) R.drawable.btn_favorite_on else R.drawable.btn_favorite_off)
            btnFavorite.setOnClickListener {
                onClick?.onClick(item)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ImageItemDetail>() {
            override fun areItemsTheSame(
                oldItem: ImageItemDetail,
                newItem: ImageItemDetail
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ImageItemDetail,
                newItem: ImageItemDetail
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface ClickFavoriteListener {
        fun onClick(item: ImageItemDetail)
    }
}


