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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.intensiveproject.R
import com.android.intensiveproject.TAG
import com.android.intensiveproject.adapter.ImageSearchAdapter
import com.android.intensiveproject.databinding.FragmentMyStoragyBinding
import com.android.intensiveproject.retrofit.ImageItemDetail
import com.android.intensiveproject.searchfragment.MY_FAVORITE
import com.android.intensiveproject.util.PrefUtil
import com.google.gson.Gson

class MyStorageFragment : Fragment() {
    private val binding by lazy { FragmentMyStoragyBinding.inflate(layoutInflater) }
    private val listAdapter by lazy { ImageSearchAdapter(requireContext()) }
    private val preferenceUtil by lazy { PrefUtil(requireContext()) }
    lateinit var backPressedCallback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressed()
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
    }


    private fun initRecyclerView() {
        val pref = requireActivity().getSharedPreferences(MY_FAVORITE, Context.MODE_PRIVATE)
        val gson = Gson()

        with(binding.rvMyFavorite) {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
        }
        listAdapter.submitList(preferenceUtil.getAll())


        object : ImageSearchAdapter.ClickFavoriteListener {
            override fun onClick(item: ImageItemDetail) {
                preferenceUtil.togglePref(item)
                listAdapter.submitList(preferenceUtil.getAll())
            }
        }.also { listAdapter.onClick = it }
    }

    override fun onResume() {
        super.onResume()
        initRecyclerView()
        checkBackStack(parentFragmentManager)
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