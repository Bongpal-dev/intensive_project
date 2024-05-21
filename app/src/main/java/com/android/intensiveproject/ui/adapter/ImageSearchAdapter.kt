package com.android.intensiveproject.ui.adapter

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.airbnb.lottie.LottieAnimationView
import com.android.intensiveproject.R
import com.android.intensiveproject.data.ImageModel
import com.android.intensiveproject.data.ImageRDSImpl.KAKAO
import com.android.intensiveproject.data.ImageRDSImpl.NAVER
import com.android.intensiveproject.databinding.ItemSerchRecyclerBinding
import com.android.intensiveproject.util.extention.gone
import com.android.intensiveproject.util.extention.visible
import com.android.intensiveproject.util.Animation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ImageSearchAdapter : ListAdapter<ImageModel, ViewHolder>(diffUtil) {
    var favoriteClickListener: FavoriteClickListener? = null

    inner class SearchItemViewHolder(binding: ItemSerchRecyclerBinding) : ViewHolder(binding.root) {
        val ivImage = binding.ivSearchImage
        val ivFavorite = binding.ivFavorite
        val ivTypeTag = binding.ivTypeTag
        val tvSize = binding.tvImageSize
        val btnFavorite = binding.btnFavorite
        val lottieFavorite = binding.lottieFavorite

        fun bind(item: ImageModel) {
            ivImage.load(item.url)
            ivTypeTag.load(getEngineIcon(item.engine))
            tvSize.text = "H: ${item.height} * W: ${item.width}"
            ivFavorite.setOnClickListener { favoriteClickListener?.onClick(item) }
        }
    }

    private fun getEngineIcon(type: Int): Int {
        return when (type) {
            NAVER -> R.drawable.icon_naver
            KAKAO -> R.drawable.icon_kakao
            else -> throw IllegalArgumentException("Unknown engine")
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val imageItem = ItemSerchRecyclerBinding.inflate(inflater, parent, false)

        return SearchItemViewHolder(imageItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        (holder as SearchItemViewHolder).bind(item)

//        val gestureDetector =
//            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
//                override fun onDown(e: MotionEvent) = true
//
//                override fun onDoubleTap(e: MotionEvent): Boolean {
//                    if (!storageItem.contains(item)) {
//                        val lottie = (holder as SearchItemViewHolder).lottieFavorite
//
//                        lottie.showLottie()
//                    }
//                    onClick?.onClick(item)
//                    return super.onDoubleTap(e)
//                }
//            })
//
//
//            with(holder) {
//                extracted(item)
//                itemView.setOnTouchListener { _, event ->
//                    return@setOnTouchListener gestureDetector.onTouchEvent(event)
//                }
//            }
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
        val diffUtil = object : DiffUtil.ItemCallback<ImageModel>() {
            override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface FavoriteClickListener {
        fun onClick(item: ImageModel)
    }
}