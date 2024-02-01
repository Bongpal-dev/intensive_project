package com.android.intensiveproject.fragment

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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.intensiveproject.MainViewModel
import com.android.intensiveproject.R
import com.android.intensiveproject.TAG
import com.android.intensiveproject.adapter.ImageSearchAdapter
import com.android.intensiveproject.data.Contents
import com.android.intensiveproject.databinding.FragmentMyStoragyBinding

class MyStorageFragment : Fragment() {
    private val binding by lazy { FragmentMyStoragyBinding.inflate(layoutInflater) }
    private val imageSearchAdapter by lazy { ImageSearchAdapter(mainViewModel.getAllPrefItems()) }
    lateinit var backPressedCallback: OnBackPressedCallback
    private val mainViewModel by lazy { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        onBackPressed()
    }

    private fun onBackPressed() {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().findNavController(R.id.nav_host).popBackStack(R.id.search, true)
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
        initObserver()
        initView()
    }

    private fun initView() {
        initRecyclerView()
        initClickFavorite()
    }

    private fun initObserver() {
        with(mainViewModel) {
            myStorages.observe(viewLifecycleOwner) {
                imageSearchAdapter.submitList(it.toList())
            }
        }
    }

    private fun initRecyclerView() {
        with(binding.rvMyFavorite) {
            adapter = imageSearchAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initClickFavorite() {
        object : ImageSearchAdapter.ClickFavoriteListener {
            override fun onClick(item: Contents) {
                mainViewModel.togglePreferenceItem(item)
            }
        }.also { imageSearchAdapter.onClick = it }
    }

    override fun onResume() {
        super.onResume()
        checkBackStack(parentFragmentManager)
        imageSearchAdapter.submitList(mainViewModel.getAllPrefItems().toList())
    }
}

fun checkBackStack(fragmentManager: FragmentManager) {
    val backstackCounter = fragmentManager.backStackEntryCount
    Log.i(TAG, "backcount: ${backstackCounter}")
    for (i in 0 until backstackCounter) {
        val entry = fragmentManager.getBackStackEntryAt(i)
        Log.i(TAG, "BackStack Entry $i: ${entry.name}")
    }
}