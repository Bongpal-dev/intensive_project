package com.android.intensiveproject.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.android.intensiveproject.ui.mainactivity.MainViewModel
import com.android.intensiveproject.R
import com.android.intensiveproject.ui.mainactivity.TAG
//import com.android.intensiveproject.view.adapter.ImageSearchAdapter
import com.android.intensiveproject.databinding.FragmentMyStoragyBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyStorageFragment : Fragment() {
    private val binding by lazy { FragmentMyStoragyBinding.inflate(layoutInflater) }
//    private val imageSearchAdapter by lazy { ImageSearchAdapter(requireContext(), mainViewModel.getAllPrefItems()) }
    lateinit var backPressedCallback: OnBackPressedCallback
    private val mainViewModel by lazy { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initObserver()
        initView()
    }

    private fun initView() {
//        initRecyclerView()
        initClickFavorite()
        initSearchBar()
    }

    private fun initSearchBar() {
//        binding.etSearchBar.doAfterTextChanged {
//            mainViewModel.filterWithKeyword(it.toString())
//        }
    }
//    private fun initObserver() {
//        with(mainViewModel) {
//            myStorages.observe(viewLifecycleOwner) {
//                imageSearchAdapter.submitList(it.toList())
//            }
//            toolBarState.observe(viewLifecycleOwner) { visible ->
//                if (visible) {
//                    binding.layoutSearchBar.moveWithAnimation(0f)
//                } else {
//                    binding.layoutSearchBar.moveWithAnimation(-60f)
//                }
//            }
//
//            searchResult.observe(viewLifecycleOwner) {
//                it.forEach { Log.i(com.android.intensiveproject.view.mainactivity.TAG, "${it}") }
//                imageSearchAdapter.submitList(it.toList())
//            }
//        }
//    }

//    private fun initRecyclerView() {
//        with(binding.rvMyFavorite) {
//            adapter = imageSearchAdapter
//            layoutManager = LinearLayoutManager(context)
//            addItemDecoration(ItemDeco(context))
//            addOnScrollListener(getScrollListener())
//        }
//    }

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

    private fun initClickFavorite() {
//        object : ImageSearchAdapter.ClickFavoriteListener {
//            override fun onClick(item: Contents) {
//                mainViewModel.togglePreferenceItem(item)
//            }
//        }.also { imageSearchAdapter.onClick = it }
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