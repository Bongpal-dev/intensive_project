package com.android.intensiveproject.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.android.intensiveproject.R
import com.android.intensiveproject.data.Contents
import com.android.intensiveproject.databinding.ItemSerchRecyclerBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ImageSearchAdapter(var storageItem: MutableList<Contents>) :
    ListAdapter<Contents, ViewHolder>(diffUtil) {

    inner class SearchItemViewHolder(binding: ItemSerchRecyclerBinding) : ViewHolder(binding.root) {
        val image = binding.ivSearchImage
        val time = binding.tvPostingTime
        val site = binding.tvPostingSite
        val size = binding.tvImageSize
        val btnFavorite = binding.btnFavorite
        val ivFavorite = binding.ivFavorite
        val ivTypeTag = binding.ivTypeTag
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
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")

        if (item is Contents.ImageItem) {
            val postTime =
                LocalDateTime.parse(item.datetime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

            with(holder as SearchItemViewHolder) {
                image.load(item.imageUrl)
                site.text = if (item.siteName.isNotEmpty()) "[${item.siteName}]" else "[알 수 없음]"
                size.text = "${item.width} x ${item.height}"
                time.text = "${postTime.format(dateTimeFormatter)}"
                ivTypeTag.load(R.drawable.tag_image)
                ivFavorite.load(if (storageItem.contains(item)) R.drawable.btn_favorite_on else R.drawable.btn_favorite_off)
                btnFavorite.setOnClickListener {
                    onClick?.onClick(item)
                }
            }
        }

        if (item is Contents.VideoItem) {
            val postTime =
                LocalDateTime.parse(item.datetime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

            with(holder as SearchItemViewHolder) {
                image.load(item.imageUrl)
                site.text = if (item.title.isNotEmpty()) "[${item.title}]" else "[알 수 없음]"
                time.text = "${postTime.format(dateTimeFormatter)}"
                size.text = ""
                ivTypeTag.load(R.drawable.tag_video)
                ivFavorite.load(if (storageItem.contains(item)) R.drawable.btn_favorite_on else R.drawable.btn_favorite_off)
                btnFavorite.setOnClickListener {
                    onClick?.onClick(item)
                }
            }
        }

    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Contents>() {
            override fun areItemsTheSame(
                oldItem: Contents,
                newItem: Contents
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Contents,
                newItem: Contents
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface ClickFavoriteListener {
        fun onClick(item: Contents)
    }
}