package com.android.intensiveproject.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import coil.load
import com.android.intensiveproject.ui.common.MainViewModel
import com.android.intensiveproject.R
import com.android.intensiveproject.databinding.FragmentHomeBinding
import com.android.intensiveproject.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        binding.bgMain.load(R.drawable.bg_main)
//        checkBackStack(parentFragmentManager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnToSearchFrag.setOnClickListener {
            val extras = FragmentNavigatorExtras(it to "shared_search_bar")

            findNavController().navigate(R.id.action_main_to_search, null, null, extras)
        }
    }
}