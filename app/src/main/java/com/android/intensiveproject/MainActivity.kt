package com.android.intensiveproject

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.intensiveproject.databinding.ActivityMainBinding
import com.android.intensiveproject.databinding.FragmentMainBinding
import com.android.intensiveproject.extention.moveWithAnimation
import com.android.intensiveproject.fragment.MainFragment
import com.android.intensiveproject.fragment.MyStorageFragment
import com.android.intensiveproject.searchfragment.SearchFragment
import kotlinx.coroutines.flow.last

const val TAG = "project_test_log"

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setup()
    }

    private fun setup() {
        initBottomNavi()
    }

    private fun initBottomNavi() {
        setObserver()
        binding.bottomNavi.setOnItemSelectedListener { menuItem ->
            val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
            val childFrag = navHost.childFragmentManager.fragments.first()
            val prevDestination = navHost.navController.previousBackStackEntry?.destination?.id
            val mainFragment = navHost.navController.findDestination(R.id.menu_main)?.id
            val searchFragment = navHost.navController.findDestination(R.id.menu_search)?.id

            when (menuItem.itemId) {
                R.id.menu_main -> {
                    if (childFrag is SearchFragment && prevDestination == mainFragment) {
                        navHost.navController.popBackStack()
                        return@setOnItemSelectedListener true
                    }

                    if (childFrag is MyStorageFragment && prevDestination == mainFragment) {
                        navHost.navController.navigate(R.id.action_my_storage_to_main)
                        return@setOnItemSelectedListener true
                    }

                    if (childFrag is MyStorageFragment && prevDestination == searchFragment) {
                        navHost.navController.getBackStackEntry(R.id.menu_search).savedStateHandle.set("prev_frag", R.id.menu_my_storage)
                        navHost.navController.popBackStack()
                        return@setOnItemSelectedListener true
                    }
                }

                R.id.menu_my_storage -> {
                    if (childFrag is MainFragment) {
                        navHost.navController.navigate(R.id.action_main_to_my_storage)
                        return@setOnItemSelectedListener true
                    }

                    if (childFrag is SearchFragment) {
                        navHost.navController.navigate(R.id.action_search_to_my_storage)
                        return@setOnItemSelectedListener true
                    }
                }
            }
            return@setOnItemSelectedListener false
        }
    }

    private fun setObserver() {
        mainViewModel.toolBarState.observe(this) { visible ->
            val bottomNavigationView = listOf<View>(binding.bottomNavi, binding.bottomGradient)

            bottomNavigationView.forEach { it.moveWithAnimation(if (visible) 0f else 60f) }
        }
    }
}


class MyApp : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}

