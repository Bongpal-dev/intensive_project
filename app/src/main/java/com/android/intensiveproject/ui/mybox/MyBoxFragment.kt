package com.android.intensiveproject.ui.mybox

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.intensiveproject.R
import com.android.intensiveproject.data.model.ImageModel
import com.android.intensiveproject.databinding.FragmentMyBoxBinding
import com.android.intensiveproject.ui.common.BaseFragment
import com.android.intensiveproject.ui.common.ImageAdapter
import com.android.intensiveproject.ui.main.MainViewModel
import com.android.intensiveproject.ui.main.TAG
import com.android.intensiveproject.util.ItemDeco
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyStorageFragment : BaseFragment<FragmentMyBoxBinding>(inflate = FragmentMyBoxBinding::inflate) {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val imageAdapter by lazy { ImageAdapter() }
    lateinit var backPressedCallback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressed()
    }

    private fun onBackPressed() {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navi).selectedItemId = R.id.menu_main
            }
        }.also { backPressedCallback = it }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initView()
        mainViewModel.loadFavoriteImages()
    }

    private fun initView() {
        initRecyclerView()
        initFavoriteButton()
    }

    private fun initObserver() {
        with(mainViewModel) {
            favorites.observe(viewLifecycleOwner) {
                imageAdapter.submitList(it.toList())
            }
        }
    }

    private fun initRecyclerView() {
        with(binding.rvMyFavorite) {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ItemDeco(context))
            addOnScrollListener(getScrollListener())
        }
    }

    private fun getScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                changeToolbarVisibleWithState(newState)
            }
        }
    }

    private fun changeToolbarVisibleWithState(state: Int) {
        lifecycleScope.launch {
            if (state == RecyclerView.SCROLL_STATE_IDLE) {
                delay(1500)
                mainViewModel.showToolBar(true)
                return@launch
            }
            mainViewModel.showToolBar(false)
        }
        return
    }

    private fun initFavoriteButton() {
        object : ImageAdapter.FavoriteClickListener {
            override fun onClick(item: ImageModel) {
                mainViewModel.toggleFavoriteImage(item)
            }
        }.also { imageAdapter.favoriteClickListener = it }
    }

//    override fun onResume() {
//        super.onResume()
//        checkBackStack(parentFragmentManager)
//        imageSearchAdapter.submitList(mainViewModel.getAllPrefItems().toList())
//    }
}

fun checkBackStack(fragmentManager: FragmentManager) {
    val backstackCounter = fragmentManager.backStackEntryCount
    Log.i(TAG, "backcount: ${backstackCounter}")
    for (i in 0 until backstackCounter) {
        val entry = fragmentManager.getBackStackEntryAt(i)
        Log.i(TAG, "BackStack Entry $i: ${entry.id}")
    }
}