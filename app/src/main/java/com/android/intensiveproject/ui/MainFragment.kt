package com.android.intensiveproject.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import coil.load
import com.android.intensiveproject.ui.mainactivity.MainViewModel
import com.android.intensiveproject.R
import com.android.intensiveproject.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

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