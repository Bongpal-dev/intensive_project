package com.android.intensiveproject.view.adapter

import android.animation.ValueAnimator
import android.content.Context
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.airbnb.lottie.LottieAnimationView
import com.android.intensiveproject.R
import com.android.intensiveproject.data.Contents
import com.android.intensiveproject.databinding.ItemSerchRecyclerBinding
import com.android.intensiveproject.util.extention.gone
import com.android.intensiveproject.util.extention.visible
import com.android.intensiveproject.util.Animation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ImageSearchAdapter(val context: Context, var storageItem: MutableList<Contents>) :
    ListAdapter<Contents, ViewHolder>(diffUtil) {
    var onClick: ClickFavoriteListener? = null

    inner class SearchItemViewHolder(binding: ItemSerchRecyclerBinding) : ViewHolder(binding.root) {
        val ivImage = binding.ivSearchImage
        val ivFavorite = binding.ivFavorite
        val ivTypeTag = binding.ivTypeTag
        val tvTime = binding.tvPostingTime
        val tvSite = binding.tvPostingSite
        val tvSize = binding.tvImageSize
        val btnFavorite = binding.btnFavorite
        val lottieFavorite = binding.lottieFavorite
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val imageItem = ItemSerchRecyclerBinding.inflate(inflater, parent, false)

        return SearchItemViewHolder(imageItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
        val gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent) = true

                override fun onDoubleTap(e: MotionEvent): Boolean {
                    if (!storageItem.contains(item)) {
                        val lottie = (holder as SearchItemViewHolder).lottieFavorite

                        lottie.showLottie()
                    }
                    onClick?.onClick(item)
                    return super.onDoubleTap(e)
                }
            })

        if (item is Contents.ImageItem) {
            val postTime =
                LocalDateTime.parse(item.datetime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

            with(holder as SearchItemViewHolder) {
                ivImage.load(item.imageUrl)
                tvSite.text = if (item.siteName.isNotEmpty()) "[${item.siteName}]" else "[알 수 없음]"
                tvSize.text = "${item.width} x ${item.height}"
                tvTime.text = "${postTime.format(dateTimeFormatter)}"
                ivTypeTag.load(R.drawable.tag_image)
                ivFavorite.load(if (storageItem.contains(item)) R.drawable.btn_favorite_on else R.drawable.btn_favorite_off)
                btnFavorite.setOnClickListener {
                    if (!storageItem.contains(item)) {
                        lottieFavorite.showLottie()
                    }
                    onClick?.onClick(item)
                }
                itemView.setOnTouchListener { _, event ->
                    return@setOnTouchListener gestureDetector.onTouchEvent(event)
                }
            }
        }

        if (item is Contents.VideoItem) {
            val postTime =
                LocalDateTime.parse(item.datetime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

            with(holder as SearchItemViewHolder) {
                ivImage.load(item.imageUrl)
                tvSite.text = if (item.title.isNotEmpty()) "[${item.title}]" else "[알 수 없음]"
                tvTime.text = "${postTime.format(dateTimeFormatter)}"
                tvSize.text = ""
                ivTypeTag.load(R.drawable.tag_video)
                ivFavorite.load(if (storageItem.contains(item)) R.drawable.btn_favorite_on else R.drawable.btn_favorite_off)
                btnFavorite.setOnClickListener {
                    onClick?.onClick(item)
                }
                itemView.setOnTouchListener { _, event ->
                    return@setOnTouchListener gestureDetector.onTouchEvent(event)
                }
            }
        }
    }

    private fun LottieAnimationView.showLottie() {
        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(2400)

        animator.addUpdateListener { animation: ValueAnimator ->
            progress = animation.animatedValue as Float
        }
        animator.start()
        CoroutineScope(Dispatchers.Main).launch {
            startAnimation(Animation.fadeIn)
            visible()
            playAnimation()
            delay(800)
            startAnimation(Animation.fadeOut)
            gone()
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Contents>() {
            override fun areItemsTheSame(oldItem: Contents, newItem: Contents): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Contents, newItem: Contents): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface ClickFavoriteListener {
        fun onClick(item: Contents)
    }
}