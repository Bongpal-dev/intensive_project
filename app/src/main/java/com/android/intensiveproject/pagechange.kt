//package com.android.intensiveproject
//
//import android.util.Log
//import androidx.viewpager2.widget.ViewPager2
//
//private fun updateCurrentItemCallback(items: List<VideoItem>): ViewPager2.OnPageChangeCallback {
//    return object : ViewPager2.OnPageChangeCallback() {
//        val elements = with(binding) {
//            listOf(
//                tvTopContentChannel,
//                tvTopTitle,
//                tvTopContentBody,
//                tvTopContentPlayCount,
//                tvTopContentUploadTime,
//                ivTopContentPlayCount,
//                ivTopContentUploadTime
//            )
//        }
//        var currentState = -1
//        var currentPositon = -1
//
//        override fun onPageScrollStateChanged(state: Int) {
//            currentState = state
//            super.onPageScrollStateChanged(state)
//        }
//
//        override fun onPageSelected(position: Int) {
//            currentPositon = position
//            super.onPageSelected(position)
//        }
//
//        override fun onPageScrolled(
//            position: Int,
//            positionOffset: Float,
//            positionOffsetPixels: Int
//        ) {
//            val lastIdx = topContentsAdapter.itemCount
//
//            if (position == 0 && positionOffset <= 0 && currentState == 1) {
//                binding.vpTopContentImage.setCurrentItem(lastIdx, false)
//            }
//
//            if (position + 1 == lastIdx && positionOffset <= 0 && currentState == 1) {
//                binding.vpTopContentImage.setCurrentItem(0, false)
//            }
//
//            var currentItem = topContentsAdapter.currentList[position]
//
//            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//
//            val alphaValue = if (positionOffset < 0.5f) {
//                (0.5f - positionOffset) * 2
//            } else {
//                (positionOffset - 0.5f) * 2
//            }
//
//
//            elements.forEach { it.alpha = alphaValue }
//
//            Log.i(TAG, "alpha: ${alphaValue}")
//            if (alphaValue in 0.1f .. 0.3f) {
//                with(currentItem) {
//                    elements[0].changeText("${info.channelTitle}")
//                    elements[1].changeText("- ${info.title}")
//                    elements[2].changeText("- ${info.description}")
//                    elements[3].changeText("${stack.viewCount}회")
//                    elements[4].changeText(info.publishedAt)
//                }
//            }
//            if (positionOffset > 0 && position >= currentPositon) {
//                currentItem = topContentsAdapter.currentList[position + 1]
//            }
//            Log.i(TAG, "currentPosition: ${currentPositon}, position: ${position}")
//        }
//    }
//}